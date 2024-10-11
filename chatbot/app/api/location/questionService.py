from typing import List, Union, Dict, Optional
from .questionRepository import QuestionOption  # 필요한 모듈 임포트
from .questionSchemas import LocationRequest
import asyncio  # 비동기 함수 사용을 위한 모듈

class Question:
    def __init__(self, questionText: str, options: Union[List[str], List[Dict[str, str]], str, Dict[str, List[str]]] = None):
        self.questionText = questionText
        self.options = options if options else []  # 기본 옵션 값 설정

    def to_dict(self):
        return {
            "question": self.questionText,
            "options": self.options
        }

class ChatBotQuestion:
    def __init__(self):
        self.questionOption = QuestionOption()
        self.countryCityMap = self.questionOption.getCountryCityMap()
        self.countryMap = list(self.countryCityMap.keys())
        self.travelDurationOptions = self.questionOption.getTravelDurationOptions()
        self.transportationsOptions = self.questionOption.getTransportationsOptions()
        self.logingOptions = self.questionOption.getLodgingOptions()
        self.travelThemeOptions = self.questionOption.getTravelThemeOptions()
        self.priorityOptions = self.questionOption.getPriorityOptions()
        self.questions = [
            Question("어느 국가를 여행하시나요?", self.countryMap),  # 국가 목록을 미리 설정
            Question("어느 도시를 여행하시나요?", []),  # 국가 선택에 맞는 도시 목록 설정
            Question("여행 기간은 총 몇 일인가요?", self.travelDurationOptions),
            Question("주로 어떤 이동 수단을 이용하시나요?", self.transportationsOptions),
            Question("선호하는 숙박 시설이 있나요?", self.logingOptions),
            Question("여행의 테마는 무엇인가요? (중복 선택 가능)", self.travelThemeOptions),
            Question("여행 시 중요하게 생각하는 우선순위를 차례로 선택해주세요.", self.priorityOptions),
            Question("방문하고 싶은 관광 명소를 선택해주세요.", [])
        ]

    # 비동기 함수로 변경하여 country에 대한 주요 city 반환
    async def initializeQuestions(self, country: Optional[str] = None) -> List[Question]:
        await asyncio.sleep(0.1)
        city_options = self.countryCityMap.get(country, []) if country else []
        return city_options

    # 비동기적으로 getQuestion 메서드를 정의
    async def getQuestion(self, questionIndex: int, country: str = None, requestBody: LocationRequest = None) -> dict:
        
        questionNum = len(self.questions) - 1
        
        # 유효한 질문 인덱스인지 확인
        if questionIndex < 0 or questionIndex >= len(self.questions):
            return {"error": "Invalid question number"}
        
        question = self.questions[questionIndex] #question에 해당하는 question만 가져옴

        if questionIndex == 1:
            if not country:
                return "error : Country parameter is missing or empty"
            # 비동기적으로 county에 맞게 초기화된 질문 목록 설정
            question.options = await self.initializeQuestions(country)

        elif questionIndex == questionNum:
            # days를 이용해 질문을 설정 (예: 최대 선택 가능 개수)
            question.questionText = f"방문하고 싶은 관광 명소를 선택해주세요. (최대 {requestBody.days*2}개)"
            location = self.questionOption.getCityLocation(requestBody.country, requestBody.city)
            radius = self.questionOption.getRadius(requestBody.trans)
            question.options = await self.questionOption.getAttractionOptions(location["lat"], location["lon"], radius)
            
        return question.to_dict()