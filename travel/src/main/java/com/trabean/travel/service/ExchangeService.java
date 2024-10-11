package com.trabean.travel.service;

import com.trabean.interceptor.UserHeaderInterceptor;
import com.trabean.travel.callApi.client.ExchangeClient;
import com.trabean.travel.callApi.client.ExchangeRateClient;
import com.trabean.travel.callApi.client.ForeignCurrencyClient;
import com.trabean.travel.callApi.client.KoreaeximClient;
import com.trabean.travel.callApi.dto.request.DepositForeignAccountApiRequestDto;
import com.trabean.travel.callApi.dto.request.ExchangeApiRequestDto;
import com.trabean.travel.callApi.dto.request.ExchangeEstimateApiRequestDto;
import com.trabean.travel.callApi.dto.request.ExchangeRateApiRequestDto;
import com.trabean.travel.callApi.dto.response.ExchangeApiResponseDto;
import com.trabean.travel.callApi.dto.response.ExchangeEstimateApiResponseDto;
import com.trabean.travel.callApi.dto.response.ExchangeEstimateApiResponseDto.ApiCurrencyDetail;
import com.trabean.travel.callApi.dto.response.ExchangeRateApiResponseDto.RecDetail;
import com.trabean.travel.callApi.dto.response.ExchangeRateOpenApiResponseDto;
import com.trabean.travel.dto.request.ExchangeEstimateRequestDto;
import com.trabean.travel.dto.request.ExchangeRequestDto;
import com.trabean.travel.dto.response.ExchangeEstimateResponseDto;
import com.trabean.travel.dto.response.ExchangeEstimateResponseDto.CurrencyDetail;
import com.trabean.travel.dto.response.ExchangeRateResponseDto;
import com.trabean.travel.dto.response.ExchangeResponseDto;
import com.trabean.travel.dto.response.ExchangeResponseDto.AccountInfo;
import com.trabean.travel.dto.response.ExchangeResponseDto.ExchangeCurrency;
import com.trabean.util.CurrencyUtils;
import com.trabean.util.RequestHeader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeClient exchangeClient;
    private final ForeignCurrencyClient foreignCurrencyClient;
    private final ExchangeRateClient exchangeRateClient;
    private final KoreaeximClient koreaeximClient;
    private final CommonAccountService commonAccountService;

    @Value("${KOREAEXIM_AUTHKEY}")
    private String authKey;

    private String userKey = UserHeaderInterceptor.userKey.get();

    public ExchangeEstimateResponseDto exchangeEstimate(ExchangeEstimateRequestDto requestDto) {
        ExchangeEstimateApiRequestDto exchangeEstimateApiRequestDto
                = new ExchangeEstimateApiRequestDto(
                RequestHeader.builder()
                        .apiName("estimate")
                        .userKey(userKey)
                        .build(),
                requestDto.getCurrency(),
                requestDto.getExchangeCurrency(),
                requestDto.getAmount()
        );

        ExchangeEstimateApiResponseDto exchangeEstimateApiResponseDto = exchangeClient.getExchangeEstimate(
                exchangeEstimateApiRequestDto);

        ApiCurrencyDetail returnCurrency = exchangeEstimateApiResponseDto.getRec().getCurrency();
        ApiCurrencyDetail returnExchangeCurrency = exchangeEstimateApiResponseDto.getRec().getExchangeCurrency();

        CurrencyDetail currency = new CurrencyDetail(returnCurrency.getAmount(),
                CurrencyUtils.changeCurrency(returnCurrency.getCurrency()),
                returnCurrency.getCurrency());

        CurrencyDetail exchangeCurrency = new CurrencyDetail(returnExchangeCurrency.getAmount(),
                CurrencyUtils.changeCurrency(returnExchangeCurrency.getCurrency()),
                returnExchangeCurrency.getCurrency());

        return new ExchangeEstimateResponseDto(currency, exchangeCurrency);
    }

    public ExchangeResponseDto exchange(ExchangeRequestDto requestDto) {
        String adminUserKey = commonAccountService.getUserKey(requestDto.getKrwAccountId());

        ExchangeApiRequestDto exchangeApiRequestDto = new ExchangeApiRequestDto(
                RequestHeader.builder()
                        .apiName("exchange")
                        .userKey(adminUserKey)
                        .build(),
                requestDto.getWithdrawalAccountNo(),
                requestDto.getExchangeCurrency(),
                requestDto.getExchangeAmount()
        );

        ExchangeApiResponseDto exchangeApiResponseDto = exchangeClient.exchange(exchangeApiRequestDto);

        ExchangeCurrency exchangeCurrency = exchangeApiResponseDto.getRec().getExchangeCurrency();
        AccountInfo accountInfo = exchangeApiResponseDto.getRec().getAccountInfo();

        // 외화통장에 입금
        String amount = exchangeCurrency.getAmount();
        String depositAccountNo = requestDto.getDepositAccountNo();

        DepositForeignAccountApiRequestDto depositForeignAccountApiRequestDto = new DepositForeignAccountApiRequestDto(
                RequestHeader.builder()
                        .apiName("updateForeignCurrencyDemandDepositAccountDeposit")
                        .userKey(adminUserKey)
                        .build(),
                depositAccountNo,
                amount,
                "환전"
        );

        foreignCurrencyClient.updateForeignAccountDeposit(depositForeignAccountApiRequestDto);

        return new ExchangeResponseDto(exchangeCurrency, accountInfo);
    }

    public List<ExchangeRateResponseDto> getExchangeRate() {
        // SSAFY API에서 현재 환율 조회
        ExchangeRateApiRequestDto exchangeRateApiRequestDto = new ExchangeRateApiRequestDto(
                RequestHeader.builder()
                        .apiName("exchangeRate")
                        .userKey(userKey)
                        .build()
        );

        List<RecDetail> nowExchangeRateList = exchangeRateClient.getExchangeRate(exchangeRateApiRequestDto).getRec();

//        // Open API로 영업일 기준 하루 전 환율 조회
//        String[] date = nowExchangeRateList.get(0).getCreated().split(" ");
//        int agoDate = Integer.parseInt(date[0].replace("-", "")) - 1;
//
//        List<ExchangeRateOpenApiResponseDto> exchangeRateOpenApiResponseDto = null;
//        int tryTime = 0;
//
//        while ((exchangeRateOpenApiResponseDto == null || exchangeRateOpenApiResponseDto.isEmpty()) && tryTime <= 10) {
//            exchangeRateOpenApiResponseDto = koreaeximClient.getExchangeRateFromOpenApi(authKey, agoDate + "",
//                    "AP01");
//            agoDate--;
//            tryTime++;
//        }
//
//        // 검색을 쉽게 하기 위해 currency(화폐단위)를 key 값으로 HashMap에 저장
//        HashMap<String, String> agoExchangeRateMap = new HashMap<>();
//
//        for (ExchangeRateOpenApiResponseDto data : exchangeRateOpenApiResponseDto) {
//            String currency = data.getCur_unit();
//
//            if (currency.equals("CNH")) {
//                currency = "CNY";
//            } else if (currency.equals("JPY(100)")) {
//                currency = "JPY";
//            }
//
//            agoExchangeRateMap.put(currency, data.getDeal_bas_r());
//        }

        HashMap<String, String> agoExchangeRateMap = new HashMap<>();
        agoExchangeRateMap.put("CAD", "988.22");
        agoExchangeRateMap.put("CHF", "1,576.7");
        agoExchangeRateMap.put("CNY", "190.05");
        agoExchangeRateMap.put("EUR", "1,478.12");
        agoExchangeRateMap.put("GBP", "1,762.23");
        agoExchangeRateMap.put("JPY", "910.26");
        agoExchangeRateMap.put("USD", "1,346.5");

        // return 할 데이터 만들기
        List<ExchangeRateResponseDto> result = new ArrayList<>();

        for (RecDetail data : nowExchangeRateList) {
            String currency = data.getCurrency();
            double exchangeRate = Double.parseDouble(data.getExchangeRate().replace(",", ""));
            double pastExchangeRate = Double.parseDouble(agoExchangeRateMap.get(currency).replace(",", ""));
            double changeRate = Math.round(((exchangeRate - pastExchangeRate) / pastExchangeRate) * 100) / 100.0;

            ExchangeRateResponseDto exchangeRateResponseDto = ExchangeRateResponseDto.builder()
                    .id(data.getId())
                    .country(CurrencyUtils.changeCurrency(currency))
                    .currency(currency)
                    .exchangeRate(exchangeRate)
                    .pastExchangeRate(pastExchangeRate)
                    .changeRate(changeRate)
                    .build();

            result.add(exchangeRateResponseDto);
        }

        return result;
    }

}
