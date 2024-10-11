package com.trabean.util;

public class CurrencyUtils {

    public static String changeCurrency(String currency) {
        if ("USD".equals(currency)) {
            return "미국";
        } else if ("EUR".equals(currency)) {
            return "유럽";
        } else if ("JPY".equals(currency)) {
            return "일본";
        } else if ("GBP".equals(currency)) {
            return "영국";
        } else if ("CHF".equals(currency)) {
            return "스위스";
        } else if ("CAD".equals(currency)) {
            return "캐나다";
        } else if ("KRW".equals(currency)) {
            return "한국";
        } else if ("CNY".equals(currency)) {
            return "중국";
        }

        return null;
    }

}
