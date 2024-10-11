package com.trabean.notification.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Decryption {

    // AES 알고리즘을 사용
    private static final String ALGORITHM = "AES";
    // CBC 모드와 PKCS5 패딩을 사용하는 변환 방식
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    // 16바이트 비밀 키 (AES-128 사용 시)
    private static final String SECRET_KEY = "your-16-byte-key"; // 실제 비밀 키로 대체해야 함


    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] originalData = cipher.doFinal(decodedData);
            return new String(originalData);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }
}