package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.AccountNumberApiRequestDto;
import com.trabean.travel.callApi.dto.request.AdminUserKeyApiRequestDto;
import com.trabean.travel.callApi.dto.request.MemberInfoApiRequestDto;
import com.trabean.travel.callApi.dto.request.MemberJoinApiRequestDto;
import com.trabean.travel.callApi.dto.request.MemberRoleUpdateApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountNumberApiResponseDto;
import com.trabean.travel.callApi.dto.response.AdminUserKeyApiResponseDto;
import com.trabean.travel.callApi.dto.response.MemberInfoApiResponseDto;
import com.trabean.travel.callApi.dto.response.MemberJoinApiResponseDto;
import com.trabean.travel.callApi.dto.response.MemberRoleUpdateApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "account", path = "/api/accounts", configuration = FeignClientsConfiguration.class)
public interface AccountClient {

    /**
     * Account API : accountId로 계좌번호 조회
     */
    @PostMapping("/internal/get-accountNo")
    AccountNumberApiResponseDto getAccount(@RequestBody AccountNumberApiRequestDto accountNumberApiRequestDto);

    /**
     * Account API : 통장 주인의 userKey 조회
     */
    @PostMapping("/internal/get-admin-userKey")
    AdminUserKeyApiResponseDto getAdminUserKey(@RequestBody AdminUserKeyApiRequestDto adminUserKeyApiRequestDto);

    /**
     * Account API : 여행통장 가입
     */
    @PostMapping("/internal/join-member")
    MemberJoinApiResponseDto joinMember(@RequestBody MemberJoinApiRequestDto memberJoinApiRequestDto);

    /**
     * Account API : 여행통장 결제 권한 변경
     */
    @PutMapping("/internal/update-userRole")
    MemberRoleUpdateApiResponseDto updateUserRole(
            @RequestBody MemberRoleUpdateApiRequestDto memberRoleUpdateApiRequestDto);

    /**
     * Account API : 멤버 목록 조회
     */
    @GetMapping("/internal/get-travel-account-members")
    MemberInfoApiResponseDto getMemberInfo(@RequestBody MemberInfoApiRequestDto memberInfoApiRequestDto);
}