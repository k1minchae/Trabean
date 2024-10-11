package com.trabean.user.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabean.user.user.dto.AddUserRequest;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ExternalApiService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);
    private final RestTemplate restTemplate;
    @Value("${API_KEY}")
    private String apiKey;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendToExternalApi(AddUserRequest addUserRequestDto) throws Exception {
        String externalApiUrl = "https://finopenapi.ssafy.io/ssafy/api/v1/member/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", addUserRequestDto.email());
        requestBody.put("name", addUserRequestDto.name());
        requestBody.put("password", addUserRequestDto.password());
        requestBody.put("apiKey", apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(externalApiUrl, HttpMethod.POST, entity, String.class);

            // 응답 데이터 로그 출력
            logger.info("External API response: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody(); // 응답 데이터 반환
            } else {
                throw new Exception("External API request failed: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new Exception("External API call failed: " + e.getMessage());
        }
    }
}
