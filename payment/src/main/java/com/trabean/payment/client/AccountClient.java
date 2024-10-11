package com.trabean.payment.client;

import com.trabean.payment.dto.response.AccountNoResponse;
import com.trabean.payment.dto.response.UserRoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "accountClient", url = "http://j11a604.p.ssafy.io:8081/api/accounts")
public interface AccountClient {

    @PostMapping(value = "/get-user-role", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserRoleResponse getUserRole(@RequestBody String requestBody);

    @PostMapping(value = "/get-account-number", consumes = MediaType.APPLICATION_JSON_VALUE)
    AccountNoResponse getAccountNumber(@RequestBody String requestBody);
}
