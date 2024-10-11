package com.trabean.travel.controller;

import com.trabean.travel.dto.request.AccountChangeNameRequestDto;
import com.trabean.travel.dto.request.AccountChangeTargetAmountRequestDto;
import com.trabean.travel.dto.request.ExchangeEstimateRequestDto;
import com.trabean.travel.dto.request.ExchangeRequestDto;
import com.trabean.travel.dto.request.ForeignAccountHistoryRequestDto;
import com.trabean.travel.dto.request.InvitaionRequestDto;
import com.trabean.travel.dto.request.MemberJoinRequestDto;
import com.trabean.travel.dto.request.MemberRoleChangeRequestDto;
import com.trabean.travel.dto.request.SaveForeignAccountRequestDto;
import com.trabean.travel.dto.request.SaveKrwAccountRequestDto;
import com.trabean.travel.dto.request.SplitRequestDto;
import com.trabean.travel.dto.response.AccountInfoResponseDto;
import com.trabean.travel.dto.response.ExchangeEstimateResponseDto;
import com.trabean.travel.dto.response.ExchangeRateResponseDto;
import com.trabean.travel.dto.response.ExchangeResponseDto;
import com.trabean.travel.dto.response.ForeignAccountHistoryResponseDto;
import com.trabean.travel.dto.response.ParentsAccountIdResponseDto;
import com.trabean.travel.dto.response.TargetAmountListResponseDto;
import com.trabean.travel.dto.response.TravelAccountIdResponseDto;
import com.trabean.travel.dto.response.TravelListAccountResponseDto;
import com.trabean.travel.service.ExchangeService;
import com.trabean.travel.service.ForeignTravelAccountService;
import com.trabean.travel.service.KrwTravelAccountService;
import com.trabean.travel.service.MemberService;
import com.trabean.travel.service.TargetAmountService;
import com.trabean.travel.service.TravelAccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
public class TravelAccountController {

    private final TravelAccountService travelAccountService;
    private final TargetAmountService targetAmountService;
    private final KrwTravelAccountService krwTravelAccountService;
    private final ForeignTravelAccountService foreignTravelAccountService;
    private final MemberService memberService;
    private final ExchangeService exchangeService;

    @GetMapping("{parentAccountId}")
    public ResponseEntity<TravelListAccountResponseDto> getTravelListAccount(@PathVariable Long parentAccountId) {
        return ResponseEntity.ok(travelAccountService.findAllTravelAccount(parentAccountId));
    }

    @PutMapping("/change/accountName")
    public ResponseEntity<Void> updateTravelAccountName(
            @RequestBody AccountChangeNameRequestDto accountChangeNameRequestDto) {
        Long accountId = accountChangeNameRequestDto.getAccountId();
        String accountName = accountChangeNameRequestDto.getAccountName();
        travelAccountService.updateTravelAccountName(accountId, accountName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/change/targetAmount")
    public ResponseEntity<Void> updateTargetAmount(
            @RequestBody AccountChangeTargetAmountRequestDto accountChangeTargetAmountRequestDto) {
        Long accountId = accountChangeTargetAmountRequestDto.getAccountId();
        Long targetAmount = accountChangeTargetAmountRequestDto.getTargetAmount();
        targetAmountService.updateTargetAmount(accountId, targetAmount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/currency")
    public ResponseEntity<TravelAccountIdResponseDto> getTravelAccountIdByCurrency(@RequestParam Long accountId,
                                                                                   @RequestParam String currency) {
        TravelAccountIdResponseDto travelAccountIdResponseDto = travelAccountService.findAccountIdByCurrency(accountId,
                currency);

        if (travelAccountIdResponseDto != null) {
            return ResponseEntity.ok(travelAccountIdResponseDto);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/krw-account/save")
    public ResponseEntity<Void> saveKrwAccountSave(@RequestBody SaveKrwAccountRequestDto saveKrwAccountRequestDto) {
        krwTravelAccountService.save(saveKrwAccountRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/foreign-account/save")
    public ResponseEntity<Void> saveForeignAccountSave(
            @RequestBody SaveForeignAccountRequestDto saveForeignAccountRequestDto) {
        foreignTravelAccountService.save(saveForeignAccountRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/invitation")
    public ResponseEntity<Void> invite(@RequestBody InvitaionRequestDto invitaionRequestDto) {
        memberService.invite(invitaionRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/foreign/{accountId}")
    public ResponseEntity<ForeignAccountHistoryResponseDto> getForeignAccountHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "20240101") String startDate,
            @RequestParam(defaultValue = "20241231") String endDate,
            @RequestParam(defaultValue = "A") String transactionType) {
        ForeignAccountHistoryRequestDto foreignAccountHistoryRequestDto
                = new ForeignAccountHistoryRequestDto(accountId, startDate, endDate, transactionType);

        ForeignAccountHistoryResponseDto foreignAccountHistoryResponseDto
                = foreignTravelAccountService.findForeignAccountHistory(foreignAccountHistoryRequestDto);
        return ResponseEntity.ok(foreignAccountHistoryResponseDto);
    }

    @PostMapping("/exchange/estimate")
    public ResponseEntity<ExchangeEstimateResponseDto> getExchangeEstimate(
            @RequestBody ExchangeEstimateRequestDto requestDto) {
        return ResponseEntity.ok(exchangeService.exchangeEstimate(requestDto));
    }

    @PostMapping("/exchange")
    public ResponseEntity<ExchangeResponseDto> exchange(@RequestBody ExchangeRequestDto exchangeRequestDto) {
        return ResponseEntity.ok(exchangeService.exchange(exchangeRequestDto));
    }

    @GetMapping("/info/{accountId}")
    public ResponseEntity<AccountInfoResponseDto> getAccountInfo(@PathVariable Long accountId) {
        return ResponseEntity.ok(travelAccountService.getInfo(accountId));
    }

    @GetMapping("/childList/{accountId}")
    public ResponseEntity<List<Long>> getChildList(@PathVariable Long accountId) {
        return ResponseEntity.ok(travelAccountService.getChildList(accountId));
    }

    @GetMapping("/exchangeRate")
    public ResponseEntity<List<ExchangeRateResponseDto>> getExchangeRate() {
        return ResponseEntity.ok(exchangeService.getExchangeRate());
    }

    @PostMapping("/split")
    public ResponseEntity<Void> splitAmount(@RequestBody SplitRequestDto splitRequestDto) {
        krwTravelAccountService.splitAmount(splitRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinTravelAccount(@RequestBody MemberJoinRequestDto memberJoinRequestDto) {
        String message = memberService.join(memberJoinRequestDto);

        if (message.equals("여행통장 가입 성공")) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/role")
    public ResponseEntity<Void> changeRole(@RequestBody MemberRoleChangeRequestDto memberRoleChangeRequestDto) {
        String message = memberService.changeRole(memberRoleChangeRequestDto);

        if (message.equals("여행통장 결제 권한 변경 성공")) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/targetAmount/{accountId}")
    public ResponseEntity<TargetAmountListResponseDto> getTargetAmountList(@PathVariable Long accountId) {
        return ResponseEntity.ok(targetAmountService.getTargetAmountList(accountId));
    }

    @GetMapping("/parents/{accoutId}")
    public ResponseEntity<ParentsAccountIdResponseDto> getParentAccountId(@PathVariable Long accoutId) {
        return ResponseEntity.ok(travelAccountService.getParentsAccountId(accoutId));
    }

    @GetMapping("/account-name/{accountId}")
    public ResponseEntity<String> getAccountName(@PathVariable Long accountId) {
        return ResponseEntity.ok(travelAccountService.getAccountName(accountId));
    }

    @GetMapping("/invite-member/{accountId}")
    public ResponseEntity<Boolean> isInviteMember(@PathVariable Long accountId) {
        return ResponseEntity.ok(memberService.isInviteMember(accountId));
    }
}
