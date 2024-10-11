package com.trabean.payment.client.ssafy;

import com.trabean.payment.dto.request.BalanceRequest;
import com.trabean.payment.dto.request.WithdrawalRequest;
import com.trabean.payment.dto.response.BalanceResponse;
import com.trabean.payment.dto.response.WithdrawalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "demandDepositClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit")
public interface DemandDepositClient {

    @PostMapping(value = "/foreignCurrency/inquireForeignCurrencyDemandDepositAccountBalance", consumes = MediaType.APPLICATION_JSON_VALUE)
    BalanceResponse getFORBalance(@RequestBody BalanceRequest request);

    @PostMapping(value = "/inquireDemandDepositAccountBalance", consumes = MediaType.APPLICATION_JSON_VALUE)
    BalanceResponse getKRWBalance(@RequestBody BalanceRequest request);

    @PostMapping(value = "/updateDemandDepositAccountWithdrawal", consumes = MediaType.APPLICATION_JSON_VALUE)
    WithdrawalResponse withdrawKRW(@RequestBody WithdrawalRequest request);

    @PostMapping(value = "/foreignCurrency/updateForeignCurrencyDemandDepositAccountWithdrawal", consumes = MediaType.APPLICATION_JSON_VALUE)
    WithdrawalResponse withdrawFOR(@RequestBody WithdrawalRequest request);
}
