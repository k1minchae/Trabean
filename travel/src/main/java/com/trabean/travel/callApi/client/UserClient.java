package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.response.MainAccountIdApiResponseDto;
import com.trabean.travel.callApi.dto.response.UserEmailApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", path = "/api/user", configuration = FeignClientsConfiguration.class)
public interface UserClient {

    /**
     * User API : userID로 user email 반환
     */
    @GetMapping("/useremail/{userId}")
    UserEmailApiResponseDto getUserEmail(@PathVariable Long userId);

    /**
     * User API : email 가지고 우리 DB에 있는 회원인지 검사
     */
    @GetMapping("/emailDB/{email}")
    Long isMember(@PathVariable String email);

    /**
     * User API : userId로 mainAccountId 반환
     */
    @GetMapping("/mainAccountId/{userId}")
    MainAccountIdApiResponseDto getMainAccountId(@PathVariable Long userId);

}
