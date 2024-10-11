from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import OpenAIEmbeddings
from langchain_pinecone import PineconeVectorStore
from langchain_community.document_loaders import Docx2txtLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain.chains import RetrievalQA
from langchain_openai import ChatOpenAI
from langchain import hub
from pinecone import Pinecone
from dotenv import load_dotenv
import getpass
import os


# .env 파일에서 환경 변수를 로드
load_dotenv()

# API 키 설정
pinecone_api_key = os.environ.get("PINECONE_API_KEY")
langsmith_api_key = os.environ.get("LANGSMITH_API_KEY")


def get_retriever(question, k=4):
    index_name = "chatbot-index"
    embedding = OpenAIEmbeddings(model='text-embedding-3-large')
    database = PineconeVectorStore.from_existing_index(index_name=index_name, embedding=embedding)
    retriever = database.as_retriever(search_kwargs={'k': k})
    relevant_docs = retriever.invoke(question)

    # 검색된 문서 출력
    # for doc in relevant_docs:
    #     print(doc.page_content)

    # return retriever


# llm 모델 반환
def get_llm(model='gpt-4o'):
    llm = ChatOpenAI(model=model)
    return llm


# keyword 사전을 이용한 query 수정
def get_question(query):
    dictionary = ["사람을 나타내는 표현 -> 거주자", "Trabean -> bean"]
    prompt = ChatPromptTemplate.from_template(f"""
        사용자의 질문을 보고, 우리의 사전을 참고해서 사용자의 질문을 변경해주세요.
        만약 변경할 필요가 없다고 판단된다면, 사용자의 질문을 변경하지 않아도 됩니다.
        그런 경우에는 질문만 리턴해주세요
        사전: {dictionary}

        질문: {{question}}
    """)
    llm = get_llm()

    dictionary_chain = prompt | llm | StrOutputParser()
    question = dictionary_chain.invoke({"question": query, "dictionary": dictionary})
    return question


# 질문에 대한 LLM 답변 반환
def get_ai_message(query):
    prompt = hub.pull("rlm/rag-prompt")  # 알맞은 프롬프트 생성 모델 불러오기
    llm = get_llm()
    question = get_question(query)
    retriever = get_retriever(question, 4)

    qa_chain = RetrievalQA.from_chain_type(  # RetrievalQA 객체 생성
        llm,  # llm 모델
        retriever=retriever,
        chain_type_kwargs={"prompt": prompt}
    )

    answer = qa_chain.invoke({"query": question})
    return answer


print(get_ai_message("Trabean 환전 기능에 대해 알고 싶어"))