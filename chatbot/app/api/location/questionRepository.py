# 국가, 도시, 스타일, 예산, 우선순위 등을 정의하는 파일
# 환경 변수 로드
from typing import List, Dict
from .googleRepository import GoogleAPI
import os
import json

# json 읽어오기
current_directory = os.path.dirname(os.path.abspath(__file__))  # 현재 파일 위치
project_root = os.path.abspath(os.path.join(current_directory, "../../../"))  # 최상위 디렉토리로 이동

# locatio Question 반환 class
class QuestionOption:
    def __init__(self):
        self.googleAPI = GoogleAPI()
    
    def readJson(self, fileName):
        file_path = os.path.join(project_root, "data", f"{fileName}.json")
        with open(file_path, "r", encoding="utf-8") as file:
            return json.load(file)

    # 국가 및 도시 목록 반환
    def getCountryCityMap(self):
        countryCityMap = self.readJson("countryCityMap")
        return countryCityMap

    # 여행 기간 옵션 반환
    def getTravelDurationOptions(self):
        travelDurationOptions = 5
        return travelDurationOptions

    # 이동 수단 옵션 반환
    def getTransportationsOptions(self):
        transportationsOptions = ["도보", "자전거", "자동차", "대중교통"]
        return transportationsOptions
    
    def getLodgingOptions(self):
        logingOptions = ["호텔", "글램핑/캠핑", "리조트", "공유숙박"]
        return logingOptions


    # 여행 테마 옵션 반환
    def getTravelThemeOptions(self):
        travelThemeOptions = list(self.readJson("categoryLists").keys())
        return travelThemeOptions


    # 여행 우선순위 옵션 반환
    def getPriorityOptions(self):
        priorityOptions = ["높은 평점", "많은 리뷰", "짧은 이동거리", "아이 동반"]
        return priorityOptions
    
    
    # 여행 반경 반환
    def getRadius(self, trans: str):
        radius = self.readJson("transRadiusList")[trans]
        return radius

    
    # 도시에 대한 위도와 경도를 반환하는 함수
    # Args : country(str) : 국가 , city(str) : 도시
    # return : {"lat" : float, "lon" : float}
    def getCityLocation(self, country: str, city: str) -> dict:
        locationData = self.readJson("locationList")
    
        if country in locationData:
            # 도시가 존재하는지 확인
            if city in locationData[country]:
                return locationData[country][city]
        return None  # 국가나 도시가 없으면 None 반환
        
        
    async def getAttractions(self, lat, lon, radius) -> List[Dict[str, str]]:
        includeType = ["tourist_attraction", "historical_landmark"],
        field = "places.id,places.displayName.text"
        attractions = await self.googleAPI.searchNearby(lat, lon, radius, 9, field, includeType, ["restaurant"])
        return attractions["places"]
        
    
    # 위,경도를 중심으로 radius 반경 안에 travelStyle에 맞는 관광지 목록을 google API를 통해 검색
    # args : lat - 위도 , lon - 경도, raduis - 반경, travelStyle - 여행 스타일
    # reutnrn  : 관광지 id, name 목록
    async def getAttractionOptions(self, lat, lon, radius) -> List[Dict[str, str]]:
        attractionOptions = await self.getAttractions(lat, lon, radius)
        if attractionOptions:
            newAttractionOptions = [{"id": option["id"], "name": option["displayName"]["text"]} for option in attractionOptions]
            return newAttractionOptions
        return []