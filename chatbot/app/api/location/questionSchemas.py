from pydantic import BaseModel
from typing import List, Optional, Union, Dict

# 질문 옵션 모델 정의 (필요에 따라 수정)
class QuestionOption(BaseModel):
    question: str
    options: Union[List[str], int, Dict[str, List[str]], Dict[str, int], List[Dict[str, str]]]
    

class LocationRequest(BaseModel):
    country: str #여행 국가
    city: str #여행 도시
    days : int #여행 기간
    trans : str #이동 수단