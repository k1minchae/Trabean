from pinecone import Pinecone
import getpass
import os
from dotenv import load_dotenv

# .env 파일에서 환경 변수를 로드
load_dotenv()

if not os.getenv("PINECONE_API_KEY"):
    os.environ["PINECONE_API_KEY"] = getpass.getpass("Enter your Pinecone API key: ")

pinecone_api_key = os.environ.get("PINECONE_API_KEY")

index_name = "chatbot-index"
pc = Pinecone(api_key=pinecone_api_key)


# 인덱스 연결
index = pc.Index(index_name)

# 인덱스에서 저장된 벡터 ID들 가져오기
vector_ids = index.describe_index_stats()["namespaces"]

# 모든 벡터 삭제
if vector_ids:
    index.delete(delete_all=True)
    print(f"{index_name} 인덱스의 모든 데이터가 삭제되었습니다.")
else:
    print(f"{index_name} 인덱스에 저장된 벡터가 없습니다.")