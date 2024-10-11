from fastapi import APIRouter, HTTPException, Query
from fastapi.responses import JSONResponse
from typing import Optional
from .questionSchemas import QuestionOption, LocationRequest
from .questionService import ChatBotQuestion

# APIRouter 객체를 정의
locationQuestionRouter = APIRouter(
    default_response_class=JSONResponse
)

# ChatBot 객체 생성
chatBot = ChatBotQuestion()
questionNum = len(chatBot.questions) - 1

# GET 요청을 처리하는 엔드포인트
@locationQuestionRouter.get("/location/{questionIndex}", response_model=QuestionOption)
async def getQuestion(
    questionIndex: int, 
    country: Optional[str] = Query(None, description="국가명을 입력하세요.")
):
    if questionIndex == questionNum:
        raise HTTPException(status_code=405, detail=f"{questionNum}은 POST 요청")
    question = await chatBot.getQuestion(questionIndex, country)
    if "error" in question:
        print(f"questionIndex: {questionIndex}, question: {question}, type: {type(question)}")
        raise HTTPException(status_code=400, detail=question["error"])
    return question


# POST 요청을 처리하는 엔드포인트 (questionIndex가 7일 때만)
@locationQuestionRouter.post("/location/{questionIndex}", response_model=QuestionOption)
async def postQuestion(
    questionIndex: int,
    requestBody: LocationRequest  # POST 요청에서 body 데이터를 처리
):
    if questionIndex != questionNum:  # POST 요청은 7만 허용
        raise HTTPException(status_code=405, detail=f"POST 요청은 {questionNum}만 가능")
    question = await chatBot.getQuestion(questionIndex, requestBody.country, requestBody)
    if "error" in question:
        raise HTTPException(status_code=400, detail=question["error"])
    return question