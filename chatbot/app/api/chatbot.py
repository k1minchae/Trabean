from fastapi import APIRouter
from pydantic import BaseModel
from fastapi.responses import JSONResponse

# 라우터 객체 생성
router = APIRouter(
    default_response_class=JSONResponse  # 여기에 기본 응답 클래스를 지정
)

# 요청 바디에서 사용될 모델 정의
class ChatbotRequest(BaseModel):
    userName: str

# 응답을 위한 옵션 데이터
options = [
    {"id": "location", "description": "여행 장소 추천"},
    {"id": "exchange", "description": "실시간 환율 조회"},
    {"id": "saving", "description": "저축 목표 관리"}
]

# /api/chatbot/start 엔드포인트 정의
@router.get("/start")
def start_chatbot():
    # 요청 받은 userName으로 응답 메시지 생성
    response = {
        "message": f"안녕하세요! 무엇을 도와드릴까요?",
        "options": options
    }
    return response
