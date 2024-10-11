package com.trabean.gateway.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Encryption {
    // AES 알고리즘을 사용
    private static final String ALGORITHM = "AES";
    // CBC 모드와 PKCS5 패딩을 사용하는 변환 방식
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    // 16바이트 비밀 키 (AES-128 사용 시)
    private static final String SECRET_KEY = "your-16-byte-key"; // 실제 비밀 키로 대체해야 함


    public static String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }
//    /**
//     * 주어진 문자열을 AES로 암호화합니다.
//     *
//     * @param value 암호화할 문자열
//     * @return 암호화된 문자열 (Base64 인코딩)
//     * @throws Exception 암호화 도중 발생할 수 있는 예외
//     */
//    public static String encrypt(String value) throws Exception {
//        // 비밀 키를 사용하여 SecretKeySpec 객체 생성
//        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
//        // 지정된 알고리즘과 변환 방식으로 Cipher 객체 생성
//        //Cipher는 암호화 및 복호화 작업을 수행하는 클래스나 객체
//        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//        // 암호화 모드로 초기화 (IV가 필요함)
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey, generateIv());
//        // 주어진 문자열을 암호화
//        byte[] encrypted = cipher.doFinal(value.getBytes());
//        // 암호화된 바이트 배열을 Base64로 인코딩하여 문자열로 반환
//        return Base64.getEncoder().encodeToString(encrypted);
//    }

    /**
     * 랜덤 IV(Initialization Vector)를 생성합니다.
     * IV는 암호화의 첫 블록을 암호화할 때 사용하는 랜덤한 데이터로, 보안을 강화하고 동일한 평문에 대해 매번 다른 암호문을 생성하는 데 도움을 줍니다.
     *
     * @return 생성된 IV
     */
    private static IvParameterSpec generateIv() {
        // 16바이트 길이의 바이트 배열 생성
        byte[] iv = new byte[16];
        // SecureRandom을 사용하여 랜덤한 바이트로 채움
        new SecureRandom().nextBytes(iv);
        // 생성된 바이트 배열을 IV 파라미터로 변환하여 반환
        return new IvParameterSpec(iv);
    }
}
