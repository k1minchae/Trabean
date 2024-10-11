from fastapi import FastAPI, APIRouter
from fastapi.responses import JSONResponse
from .answerSchemas import TravelRequest, TravelResponse
from .answerService import ChatBotAnswer

# FastAPI 애플리케이션 인스턴스 생성
locationAnswerRouter = APIRouter(
    default_response_class=JSONResponse  # 여기에 기본 응답 클래스를 지정
)

# ChatBot 객체 생성
chatBot = ChatBotAnswer()

# POST 요청에 대한 엔드포인트 정의
@locationAnswerRouter.post("/recommendLocation", response_model=TravelResponse)
async def recommendLocation(travelRequest: TravelRequest):
    travelPlans = await chatBot.getTravelRoutes(travelRequest)
    return travelPlans
