from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import OpenAIEmbeddings
from langchain_pinecone import PineconeVectorStore
from langchain_community.document_loaders import Docx2txtLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain.chains import RetrievalQA
from langchain_openai import ChatOpenAI
from pinecone import Pinecone
from langchain import hub
import getpass
import os
from dotenv import load_dotenv
import docx2txt

# .env 파일에서 환경 변수를 로드
load_dotenv()

current_directory = os.path.dirname(os.path.abspath(__file__))  # 현재 파일 위치
project_root = os.path.abspath(os.path.join(current_directory, "../../../"))  # 최상위 디렉토리로 이동


def readData(fileName):
    file_path = os.path.join(project_root, "data", f"{fileName}.docx")
    introduction = Docx2txtLoader(file_path)
    return introduction

# 텍스트 스플리터 설정
text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=400,
    chunk_overlap=100,
)

loader = readData("trabean")
document_list = loader.load_and_split(text_splitter=text_splitter)

# PINECONE API 키 설정
if not os.getenv("PINECONE_API_KEY"):
    os.environ["PINECONE_API_KEY"] = getpass.getpass("Enter your Pinecone API key: ")
pinecone_api_key = os.environ.get("PINECONE_API_KEY")

# LangSmith API 키 설정
if not os.getenv("LANGSMITH_API_KEY"):
    os.environ["LANGSMITH_API_KEY"] = getpass.getpass("Enter your LangSmith API key: ")
langsmith_api_key = os.environ.get("LANGSMITH_API_KEY")

index_name = "chatbot-index"
pc = Pinecone(api_key=pinecone_api_key)

embedding = OpenAIEmbeddings(model='text-embedding-3-large')
PineconeVectorStore.from_documents(document_list, index_name=index_name, embedding=embedding)

print("vector DB에 저장 완료")