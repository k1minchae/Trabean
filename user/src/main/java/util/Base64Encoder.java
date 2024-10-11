package util; // 패키지 선언 (필요에 따라 생략 가능)

import java.util.Base64;

public class Base64Encoder {
    public static void main(String[] args) {
        String secretKey = "SomeSecretKey";  // 원하는 비밀 키로 수정
        String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.out.println("Base64 Encoded Key: " + base64EncodedKey);
    }
}
