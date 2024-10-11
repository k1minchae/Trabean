from fastapi import APIRouter, HTTPException
from fastapi.responses import JSONResponse
from .promptService import QuestionService
from .promptSchemas import AnswerResponse, QuestionRequest

# FastAPI 라우터 인스턴스 생성
promptRouter = APIRouter(default_response_class=JSONResponse)

# QueryService 객체 생성
questionService = QuestionService()

@promptRouter.post("/question", response_model=AnswerResponse)
async def questionAnswer(textQuestion: QuestionRequest):
  try:
    response = questionService.getAIMessage(textQuestion.question)
    return response
  except Exception as e:
    raise HTTPException(status_code=500, detail=str(e))