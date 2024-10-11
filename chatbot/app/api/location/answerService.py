from typing import List
from .answerSchemas import TravelRequest, TravelResponse
from .answerRepository import PlaceFetcher
import itertools
import networkx as nx
from geopy.distance import geodesic


class ChatBotAnswer:
    def __init__(self):
        self.placeFetcher = PlaceFetcher()
        self.tourism = []
        self.restaurant = []
        self.tourismSet = []
        self.travelResponse = {}
        self.travelResponse["hotel"] = {}
        self.travelResponse["routes"] = {}
        
    # 장소 목록을 기반으로 위/경도 평균 구하기
    def getMidLocation(self, places):
        lat = [place["location"]["latitude"] for place in places]
        lon = [place["location"]["longitude"] for place in places]
        averageLatitude = sum(lat) / len(lat)
        averageLongitude = sum(lon) / len(lon)
        average = {
            "midLat": averageLatitude,
            "midLon": averageLongitude
        }
        return average

    # 관광지 id 목록 받아서 관광지 목록에 대한 세부 정보들 반환
    async def getPlacesInfo(self, places):
        placesInfo = []

        for id in places:
            info = await self.placeFetcher.getPlaceInfo(id)
            if info:
                placesInfo.append(info)

        return placesInfo

    # 이동수단, 선호도에 맞는 알맞은 radius 반환
    def getOptimalRadius(self, trans, priority):
        radius = self.placeFetcher.getRadius(trans)
        optimalRadius = radius * (priority[2]/2)  # 검색 반경
        if optimalRadius > 50000:
            optimalRadius = 50000
        return optimalRadius


    # 위/경도를 기준으로 반경 radius 안에서 priority에 맞는 숙박시설 찾기
    async def getLoging(self, lat, lon, radius, priority, preferLoging):
        if radius > 50000:
            radius = 50000
        types = self.placeFetcher.getLogingTypes(preferLoging)

        # 비동기 결과를 변수에 저장한 후 접근
        logingResponse = await self.placeFetcher.getPlaces(lat, lon, 20, radius, types)
        logingLists = logingResponse["places"]

        # 기본 숙박시설 검색
        if not logingLists:
            logingResponse = await self.placeFetcher.getPlaces(lat, lon, 20, radius, ["lodging"])
            logingLists = logingResponse["places"]

        # 우선순위에 따른 정렬
        hotel = self.getPrioritySort(logingLists, priority)[0]
        return hotel


    def getPrioritySort(self, sortList, priority):
        # 우선순위에 따른 정렬   
        if priority[0] < priority[1]:
            result = sorted(
                [item for item in sortList if 'rating' in item and 'userRatingCount' in item],
                key=lambda x: (-x['rating'], -x['userRatingCount'])
            )
        else:
            result = sorted(
                [item for item in sortList if 'rating' in item and 'userRatingCount' in item],
                key=lambda x: (-x['userRatingCount'], -x['rating'])
            )
        return result


    async def getPlaces(self, lat, lon, radius, num, priority, interests):
        seenIds = set()
        result = []
        types = []

        for inter in interests:
            types += self.placeFetcher.getCategories(inter)
        types = list(set(types))
        
        # 비동기 결과 대기 후 키에 접근
        response = await self.placeFetcher.getPlaces(lat, lon, 20, radius, types)
        places = response["places"]

         # 중복된 place를 추가하지 않도록 필터링
        for item in self.tourism:
            seenIds.add(item["id"])
        for place in places: 
            if place["id"] not in seenIds:
                result.append(place)
                seenIds.add(place["id"])

        # 아이 항목 필터링
        if priority[3] <= 2 or "아이와 함께" in interests:
            result = [item for item in result if item.get("goodForChildren")]

        if len(result) < num:
            presponse = await self.placeFetcher.getPlaces(lat, lon, 20, radius, [])
            places = response["places"]

            for place in places:  # 중복된 place를 추가하지 않도록 필터링
                if place["id"] not in seenIds:
                    result.append(place)
                    seenIds.add(place["id"])

        if priority[3] <= 2 or "아이와 함께" in interests:
            result = [item for item in result if item.get("goodForChildren")]

        result = self.getPrioritySort(result, priority)
        return result[:num]
    
    
    def planTourism(self):
        locations = [(place['location']['latitude'], place['location']['longitude']) for place in self.tourism]
        self.tourismSet = self.matching(locations)
        day = 1
        for ind1, ind2 in self.tourismSet:
            self.travelResponse["routes"][day] = {"tourism": []}
            self.travelResponse["routes"][day]["tourism"].append(self.tourism[ind1])
            self.travelResponse["routes"][day]["tourism"].append(self.tourism[ind2])
            day += 1
        return 1    
        
    
    def matching(self, locations):
        n = len(locations)  # 점의 수

        # 모든 쌍의 점에 대한 거리 계산
        edges = []
        for (i, j) in itertools.combinations(range(n), 2):
            dist = geodesic(locations[i], locations[j]).kilometers
            edges.append((i, j, dist))  # (점1, 점2, 거리) 형식으로 추가

        # 그래프 생성 및 가중치 추가
        G = nx.Graph()
        G.add_weighted_edges_from(edges)

        # 최소 매칭 계산
        minMatching = nx.algorithms.matching.min_weight_matching(G)
        indexPairs = [(i, j) for i, j in minMatching]
        return indexPairs
    

    async def getRestaurant(self, priority):
        day = 1
        for ind1, ind2 in self.tourismSet:
            midLat = (self.tourism[ind1]["location"]["latitude"] + self.tourism[ind2]["location"]["latitude"])/2
            midLon = (self.tourism[ind1]["location"]["longitude"] + self.tourism[ind2]["location"]["longitude"])/2
            response = None
            radius = 1000
            while(not response):
                response = await self.placeFetcher.getPlaces(midLat, midLon, 10, radius, ["restaurant"])
                radius += 1000  
            response = response["places"]
            restaurants = self.getPrioritySort(response, priority)
            self.travelResponse["routes"][day].update({"restaurant": restaurants[:2]})
            day += 1
        return 1


    # 주어진 일수(days)에 맞는 최적의 여행 경로를 생성하여 반환
    async def getTravelRoutes(self, request: TravelRequest) -> TravelResponse:
        # 관광지 ID 목록이 있는 경우 관광지 정보를 받아오기
        if request.attractions:
            # getPlacesInfo가 비동기 함수이므로, await로 결과를 받아야 함
            self.tourism = await self.getPlacesInfo(request.attractions)
            midLocation = self.getMidLocation(self.tourism)  # 리스트로 getMidLocation에 전달
        else:
            midLocation = self.placeFetcher.getCityLocation(request.location.country, request.location.city)  # 중심 위치 선정

        priority = request.preferences.priority
        transportation = request.transportation
        radius = self.getOptimalRadius(transportation, priority)
        preferLoging = request.preferences.preferLoging

        # 숙박시설 정보 가져오기
        hotel = await self.getLoging(midLocation["midLat"], midLocation["midLon"], radius, priority, preferLoging)
        self.travelResponse["hotel"] = hotel
        
        # 장소 목록 나머지 받아오기
        days = request.days
        num = 2 * days - len(self.tourism)
        interests = request.preferences.interest
        self.tourism += await self.getPlaces(hotel["location"]["latitude"], hotel["location"]["longitude"], radius, num, priority, interests)

        if self.planTourism():
            if await self.getRestaurant(priority):
                return self.travelResponse
        
        return "error"