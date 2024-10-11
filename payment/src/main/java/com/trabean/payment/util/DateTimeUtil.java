package com.trabean.payment.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DateTimeUtil {

    // 현재 날짜를 yyyyMMdd 형태로 반환
    public static String getTransmissionDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(dateFormatter);
    }

    // 현재 시간을 HHmmss 형태로 반환
    public static String getTransmissionTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        return now.format(timeFormatter);
    }

    // YYYYMMDD + HHMMSS + 6자리 일련번호 생성
    public static String generateUniqueNumber() {
        LocalDateTime now = LocalDateTime.now();

        // YYYYMMDD 형식 날짜
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = now.format(dateFormatter);

        // HHMMSS 형식 시간
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String timePart = now.format(timeFormatter);

        // 6자리 랜덤 숫자 생성
        Random random = new Random();
        int serialNumber = random.nextInt(900000) + 100000;  // 100000 ~ 999999 범위의 랜덤 숫자

        // 최종 채번된 고유 번호
        return datePart + timePart + serialNumber;
    }
}
