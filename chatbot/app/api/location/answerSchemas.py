from pydantic import BaseModel
from typing import List, Dict, Optional

# 요청 바디에 대한 데이터 모델 정의
class Location(BaseModel):
    country: str  # 여행할 국가명
    city: str  # 여행할 도시명


class Preferences(BaseModel):
    interest: List[str]  # 여행 스타일
    priority: List[int]  # 우선순위
    preferLoging : str


class TravelRequest(BaseModel):
    location: Location  # 여행지 정보
    days: int  # 여행 기간
    transportation: str  # 이동수단
    preferences: Preferences  # 여행 선호 정보
    attractions : List[str] # 선호하는 관광지 id 목록


# 응답 바디에 대한 데이터 모델 정의
class Location(BaseModel):
    latitude: float
    longitude: float


class DisplayName(BaseModel):
    text: str = None
    languageCode: Optional[str] = None


class PaymentOptions(BaseModel):
    acceptsCreditCards: Optional[bool] = None
    acceptsCashOnly: Optional[bool] = None
    acceptsDebitCards: Optional[bool] = None


class Place(BaseModel):
    id: str
    formattedAddress: str = None
    location: Location
    rating: float = None
    googleMapsUri: str = None
    userRatingCount: int = None
    displayName: DisplayName = None
    primaryType: Optional[str] = None
    editorialSummary: Optional[DisplayName] = None
    goodForChildren: Optional[bool] = None
    paymentOptions: Optional[PaymentOptions] = None


class Route(BaseModel):
    tourism: List[Place]
    restaurant: List[Place]


class Hotel(BaseModel):
    id: str = None
    formattedAddress: str = None
    location: Location
    rating: float = None
    googleMapsUri: str = None
    userRatingCount: int = None
    displayName: DisplayName = None
    primaryType: str = None
    editorialSummary: Optional[DisplayName] = None
    goodForChildren: Optional[bool] = None
    paymentOptions: Optional[PaymentOptions] = None


class TravelResponse(BaseModel):
    hotel: Hotel
    routes: Dict[int, Route]