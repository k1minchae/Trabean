from pydantic import BaseModel

# 요청 데이터 스키마
class QuestionRequest(BaseModel):
    question: str

# 응답 데이터 스키마
class AnswerResponse(BaseModel):
    answer: str 
