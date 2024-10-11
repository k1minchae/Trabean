package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.NotificationApiRequestDto;
import com.trabean.travel.callApi.dto.response.NotificationApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification", path="/api/notifications")
public interface NotificationClient {

    @PostMapping
    NotificationApiResponseDto postNotification(@RequestBody NotificationApiRequestDto notificationApiRequestDto);

}
