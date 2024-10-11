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
import json
import getpass
import os


# .env 파일에서 환경 변수를 로드
load_dotenv()
if not os.getenv("PINECONE_API_KEY"):
    os.environ["PINECONE_API_KEY"] = getpass.getpass("Enter your Pinecone API key: ")
pinecone_api_key = os.environ.get("PINECONE_API_KEY")
if not os.getenv("LANGSMITH_API_KEY"):
    os.environ["LANGSMITH_API_KEY"] = getpass.getpass("Enter your LangSmith API key: ")
langsmith_api_key = os.environ.get("LANGSMITH_API_KEY")

# 위치 설정
current_directory = os.path.dirname(os.path.abspath(__file__))  # 현재 파일 위치
project_root = os.path.abspath(os.path.join(current_directory, "../../../"))  # 최상위 디렉토리로 이동

class PineconeRepository:
    def __init__(self, model='gpt-4o', index_name="chatbot-index", emmodel='text-embedding-3-large', k=4):
      self.llm = ChatOpenAI(model=model)
      self.embedding = OpenAIEmbeddings(model='text-embedding-3-large')
      self.database = PineconeVectorStore.from_existing_index(index_name=index_name, embedding=self.embedding)
      self.retriever = self.database.as_retriever(search_kwargs={'k': k})
      self.prompt = hub.pull("rlm/rag-prompt") 
      
    def getDictionary(self, dict_name="trabeanDict"):
      file_path = os.path.join(project_root, "data", f"{dict_name}.json")
      if not os.path.exists(file_path):
          raise FileNotFoundError(f"사전 파일을 찾을 수 없습니다: {file_path}")
      with open(file_path, 'r', encoding='utf-8') as f:
          dictionary_dict = json.load(f)
      # "key -> value" 형식의 리스트로 변환
      dictionary_list = [f"{key} -> {value}" for key, value in dictionary_dict.items()]
      return dictionary_list
      
    def getRetriever(self, question):
      relevant_docs = self.retriever.invoke(question)
      # 검색된 문서 출력
      # for doc in relevant_docs:
      #     print(doc.page_content)
      return self.retriever