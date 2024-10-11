from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import OpenAIEmbeddings
from langchain_pinecone import PineconeVectorStore
from langchain_community.document_loaders import Docx2txtLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain.chains import RetrievalQA
from langchain_openai import ChatOpenAI
from .promptSchemas import AnswerResponse
from .promptRepository import PineconeRepository
from langchain import hub
from pinecone import Pinecone
import re

from dotenv import load_dotenv
import getpass
import os

# .env 파일에서 환경 변수를 로드
load_dotenv()

# PINECONE API 키 설정
if not os.getenv("PINECONE_API_KEY"):
    os.environ["PINECONE_API_KEY"] = getpass.getpass("Enter your Pinecone API key: ")
pinecone_api_key = os.environ.get("PINECONE_API_KEY")

# LangSmith API 키 설정
if not os.getenv("LANGSMITH_API_KEY"):
    os.environ["LANGSMITH_API_KEY"] = getpass.getpass("Enter your LangSmith API key: ")
langsmith_api_key = os.environ.get("LANGSMITH_API_KEY")

class QuestionService:
  def __init__(self, LLMmodel='gpt-4o', index_name="chatbot-index", emmodel='text-embedding-3-large', ragPrompt="rlm/rag-prompt", k=4):
    self.repository = PineconeRepository()
    self.llm = ChatOpenAI(model=LLMmodel, max_tokens=216)
    self.prompt = hub.pull(ragPrompt)  # 알맞은 프롬프트 생성 모델 불러오기

  def isGreetingTerm(self, query):# 인사 관련 용어가 질문에 포함되어 있는지 확인
    greeting_terms = [
    "hello", "hi", "안녕", "안녕하세요", "하이", "헬로", "반가워", "안부", "고마워", "사랑해", 
    "잘 지내?", "오랜만이야", "어이", "여보세요", "굿모닝", "굿이브닝", "굿애프터눈", "미안해",
    "굿나잇", "좋은 아침", "잘 자", "안녕히 주무세요", "좋은 하루", "잘 있었어?", "별일 없지?", 
    "만나서", "반가워", "어떻게 지냈어?", "즐거운 하루", "또 봐", "다음에 봐", "행복해", "평안하길", 
    "환영해", "어서 와", "잘 있었니?", "살아있었네?", "기분 좋지?", "행복한 하루 보내", 
    ]
    return any(term in query.lower() for term in greeting_terms)

  def isTrabeanBankTerms(self, query):# 경제 용어가 질문에 포함되어 있는지 확인
    trabean_bank_terms = [
    "트래빈", "Trabean", "trabean", "트레빈", "이용 약관", "서비스 약관", "개인정보 처리방침", 
    "권리", "의무", "서비스 이용 조건","책임", "데이터 보호", "약관 변경", "서비스 종료", "정책", 
    "사용자 동의", "보안 정책","법적 책임", "사용자 권리", "보안", "이용 정책", "사용자 의무", 
    "계정 관리", "약관 동의", "환전", "이체", "예금", "대출", "통장 개설", "계좌 생성", "잔액 조회", "송금", 
    "수수료", "출금", "입금", "외화 환전", "이자", "비밀번호 변경", "ATM 이용", "카드 발급", "통장",
    "금융 상품", "적금", "예금 금리", "적금 금리", "입출금 한도", "여행 통장", "외환 서비스"]
    return any(term in query for term in trabean_bank_terms)
  
  def formatText(self, text: str) -> str:# 각 문장에 줄바꿈(\n)을 추가하여 반환
    sentences = re.split(r'(?<=[.!?]) +', text)
    formatted_text = "\n".join(sentences)
    return formatted_text
  
  def getQuestion(self, query):
    dictionary = self.repository.getDictionary()
    prompt = ChatPromptTemplate.from_template(f"""
      사용자의 질문을 보고, 우리의 사전을 참고해서 사용자의 질문을 변경해주세요.
      만약 변경할 필요가 없다고 판단된다면, 사용자의 질문을 변경하지 않아도 됩니다.
      그런 경우에는 질문만 리턴해주세요.
      사전: {dictionary}

      질문: {{question}}
  """)
    dictionary_chain = prompt | self.llm | StrOutputParser()
    question = dictionary_chain.invoke({"question": query, "dictionary": dictionary})
    return question
  
  def getAIMessage(self, query):
    isGreeting = self.isGreetingTerm(query)
    isBank = self.isTrabeanBankTerms(query)
    
    if(isGreeting and (not isBank)):
       res = self.llm.invoke(query)
       res = self.formatText(res.content)
       return AnswerResponse(answer=res)
    
    question = self.getQuestion(query)
    retriever = self.repository.getRetriever(question)
    qa_chain = RetrievalQA.from_chain_type(  # RetrievalQA 객체 생성
        self.llm,  # llm 모델
        retriever=retriever,
        chain_type_kwargs={"prompt": self.prompt}
    )
    answer_dict = qa_chain.invoke({"query": question})
    
    if isinstance(answer_dict, dict) and 'result' in answer_dict:
      res = self.formatText(answer_dict['result'])
      return AnswerResponse(answer=res)

    raise ValueError(f"Unexpected response format: {answer_dict}")

# service = QuestionService()
# print(service.getAIMessage("환전 기능에 대해 알고 싶어"))