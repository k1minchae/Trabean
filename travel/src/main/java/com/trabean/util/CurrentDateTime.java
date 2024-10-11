package com.trabean.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

/**
 * {@code CurrentDateTime} 클래스는 현재 날짜와 시간을 세 가지 형식으로 제공합니다.
 *
 * <pre>
 * 1. YYYYMMDDHHMMSS
 * 2. YYYYMMDD
 * 3. HHMMSS
 * </pre>
 * <p>
 * 이 클래스는 {@code DateTimeFormatter}를 사용하여 날짜와 시간을 포맷합니다. 이 클래스는 불변 클래스이며, 스레드 안전성을 보장합니다.
 *
 * <p>사용 예시:</p>
 * <pre>
 * // 1. CurrentDateTime 객체 생성
 *   CurrentDateTime currentDateTime = CurrentDateTime.getCurrentDateTime();
 *
 * // 2. 필요한 필드값 조회
 *   String full = CurrentDateTime.getYYYYMMDDHHMMSS();
 *   String date = CurrentDateTime.getYYYYMMDD();
 *   String time = CurrentDateTime.getHHMMSS();
 * </pre>
 *
 * @author FickleBoBo
 * @since 2024-09-21
 */
@Getter
public class CurrentDateTime {

    /**
     * 날짜와 시간을 나타내는 포맷터 (YYYYMMDDHHMMSS).
     */
    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 날짜를 나타내는 포맷터 (YYYYMMDD).
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 시간을 나타내는 포맷터 (HHMMSS).
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    /**
     * YYYYMMDDHHMMSS 형식의 현재 날짜와 시간.
     */
    private final String YYYYMMDDHHMMSS;

    /**
     * YYYYMMDD 형식의 현재 날짜.
     */
    private final String YYYYMMDD;

    /**
     * HHMMSS 형식의 현재 시간.
     */
    private final String HHMMSS;

    /**
     * {@code CurrentDateTime} 객체의 생성자. 현재 시간을 가져와 세 가지 형식으로 포맷합니다.
     */
    private CurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        this.YYYYMMDDHHMMSS = now.format(FULL_FORMATTER);
        this.YYYYMMDD = now.format(DATE_FORMATTER);
        this.HHMMSS = now.format(TIME_FORMATTER);
    }

    /**
     * 현재 시간을 가져와 새로운 {@code CurrentDateTime} 객체를 생성하고 반환하는 정적 팩토리 메서드.
     *
     * @return 새로 생성된 {@code CurrentDateTime} 객체
     */
    public static CurrentDateTime getCurrentDateTime() {
        return new CurrentDateTime();
    }
}
