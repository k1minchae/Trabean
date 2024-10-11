from typing import List, Dict
import httpx 
import os 
from dotenv import load_dotenv

load_dotenv()
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")

class GoogleAPI: 
    def __init__(self):
        pass

    # 위/경도 기준으로 radius안의 types에 해당하는 목록 num개 반환
    # Args : country(str) : 국가 , city(str) : 도시
    # 장소 목록 
    async def searchNearby(self, lat, lon, radius, num, field = None, types = [], excludeTypes = None) -> List[Dict[str, str]]:
        url = "https://places.googleapis.com/v1/places:searchNearby"
        
        if not field : "places.id,places.displayName.text"
            
        headers = {
            "Content-Type": "application/json",  # JSON 형식으로 데이터 전송
            "X-Goog-Api-Key": GOOGLE_API_KEY,  # API KEY
            "X-Goog-FieldMask": field,
        }

        # 요청 본문 (바디)
        data = {
            "includedTypes": types,
            "languageCode": "ko",
            "maxResultCount": num,
            "locationRestriction": {
                "circle": {
                    "center": {
                        "latitude": lat,
                        "longitude": lon,
                    },
                    "radius": radius,
                }
            },
        }
        
        if excludeTypes:
            data["excludedTypes"] = excludeTypes

        async with httpx.AsyncClient() as client:
            response = await client.post(url, headers=headers, json=data)

        if response.status_code == 200:
            responseData = response.json()
            if responseData:  # 제대로 된 관광지를 찾음
                return responseData
            else:
                print(f"No results found for radius {radius}. Increasing radius.")
                return None

        else:
            print("Error:", response.status_code, response.text)
            return
        
        
    # 관광지 ID에 대해 GET 요청 보내서 관광지 데이터 받아오기    
    async def searchDetail(self, placeId, fields = "displayName,googleMapsUri,location,rating,userRatingCount,primaryType,goodForChildren,paymentOptions,formattedAddress,editorialSummary"):
        url = f"https://places.googleapis.com/v1/places/{placeId}?fields={fields}&key={GOOGLE_API_KEY}&languageCode=ko"
        
        async with httpx.AsyncClient() as client:
            response = await client.get(url)

        if response.status_code == 200:
            return response.json()
        else:
            print(f"ID: {id} - Error: {response.status_code} - {response.text}")