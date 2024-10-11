import requests
from typing import List, Dict
from dotenv import load_dotenv
import os
import json
from .googleRepository import GoogleAPI


# 환경 변수 로드
load_dotenv()
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")

# json 읽어오기
current_directory = os.path.dirname(os.path.abspath(__file__))  # 현재 파일 위치
project_root = os.path.abspath(os.path.join(current_directory, "../../../"))  # 최상위 디렉토리로 이동


# 적당한 2N개의 관광, 식당 목록 반환
class PlaceFetcher:
    def __init__(self):
        self.googleAPI = GoogleAPI()
       
    def readJson(self, fileName):
        file_path = os.path.join(project_root, "data", f"{fileName}.json")
        with open(file_path, "r", encoding="utf-8") as file:
            return json.load(file)
        
    def getCityLocation(self, country: str, city: str) -> dict:
        locationData = self.readJson("locationList")
    
        if country in locationData:
            # 도시가 존재하는지 확인
            if city in locationData[country]:
                return locationData[country][city]
        return None  # 국가나 도시가 없으면 None 반환
        
    
    # 여행 반경 반환
    def getRadius(self, trans: str):
        radius = self.readJson("transRadiusList")[trans]
        return radius
        
        
    # id 기반으로 정보 받아서 넘겨주기
    async def getPlaceInfo(self, placeId):
        fields = "id,displayName,googleMapsUri,location,rating,userRatingCount,primaryType,goodForChildren,paymentOptions,formattedAddress,editorialSummary"
        info = await self.googleAPI.searchDetail(placeId, fields)
        return info
    

    def getLogingTypes(self, preferLoging):
        logingTypes = self.readJson("logingList")[preferLoging]
        return logingTypes
    
    
    # 여행 테마에 대한 카테고리 반환
    def getCategories(self, theme):
        categoryLists = self.readJson("categoryLists")
        categories = categoryLists[theme]
        return categories
    
    
    # 카테고리 기반으로 places검색하기
    async def getPlaces(self, lat, lon, num, radius, types):
        fields = "places.id,places.displayName,places.googleMapsUri,places.location,places.rating,places.userRatingCount,places.primaryType,places.goodForChildren,places.paymentOptions,places.formattedAddress,places.editorialSummary,places.priceLevel"
        places = await self.googleAPI.searchNearby(lat, lon, radius, num, fields, types)
        return places
        
    

# 이동 수단에 따른 반경 목록
radiusList = {
  "도보": 800,
  "자전거": 2000,
  "자동차": 10000,
  "대중교통": 15000,
  "휠체어": 600
}