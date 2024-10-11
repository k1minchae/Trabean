package com.trabean.notification.api.feign;

import com.trabean.notification.api.feign.dto.request.UserKeyReq;
import com.trabean.notification.api.feign.dto.response.UserKeyRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", path = "/api/user") // 서비스 이름 설정
public interface UserFeign {

    @PostMapping("/getuserkey")
    UserKeyRes getUserKey(@RequestBody UserKeyReq userKeyReq);
}