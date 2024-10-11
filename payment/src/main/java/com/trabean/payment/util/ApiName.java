package com.trabean.payment.util;

public class ApiName {
    // 미리 정의한 여러 API 이름들
    public static final String FOREIGN_BALANCE = "inquireForeignCurrencyDemandDepositAccountBalance";
    public static final String KRW_BALANCE = "inquireDemandDepositAccountBalance";
    public static final String FOREIGN_WITHDRAW = "updateForeignCurrencyDemandDepositAccountWithdrawal";
    public static final String KRW_WITHDRAW = "updateDemandDepositAccountWithdrawal";
    public static final String FOREIGN_DEPOSIT = "updateForeignCurrencyDemandDepositAccountDeposit";
    public static final String KRW_DEPOSIT = "updateDemandDepositAccountDeposit";
    public static final String EXCHANGE_RATE = "exchangeRateSearch";

    // 생성자를 private 으로 정의하여 객체 생성을 방지
    private ApiName() {
    }
}
