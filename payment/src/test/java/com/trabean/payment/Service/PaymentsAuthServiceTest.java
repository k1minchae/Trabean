//package com.trabean.payment.Service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.eq;
//import static org.mockito.Mockito.when;
//
//import com.trabean.payment.dto.response.UserRoleResponse;
//import com.trabean.payment.enums.UserRole;
//import com.trabean.payment.exception.PaymentsException;
//import com.trabean.payment.service.PaymentsAuthService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//@RestClientTest(PaymentsAuthService.class)
//class PaymentsAuthServiceTest {
//
//    @Autowired
//    private PaymentsAuthService paymentsAuthService;
//
//    @MockBean
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private MockRestServiceServer mockServer;
//
//    @Value("${external.api.userRoleUrl}")
//    private String USER_ROLE_API_URL = "http://localhost/api/userRole";
//
//    @Test
//    @DisplayName("결제 권한 있는 유저 테스트")
//    void checkAuthPayment_ShouldReturnUserId_WhenUserHasRole() {
//        // given
//        UserRoleResponse mockResponse = UserRoleResponse.builder()
//                .userId(1L)
//                .userRole(UserRole.PAYER)
//                .status("SUCCESS")
//                .message("결제 권한 승인")
//                .build();  // Lombok의 Builder 패턴을 사용해 값 설정
//
//        // Mock RestTemplate에서 API 호출에 대한 응답 설정
//        when(restTemplate.postForObject(
//                eq(USER_ROLE_API_URL),
//                ArgumentMatchers.any(HttpEntity.class),
//                eq(UserRoleResponse.class)))
//                .thenReturn(mockResponse);
//
//        // when
//        Long result = paymentsAuthService.checkAuthPayment("userKeySample", 100L);
//
//        // then
//        assertEquals(1L, result);  // userId가 1L로 반환되는지 확인
//    }
////
////    @Test
////    void checkAuthPayment_ShouldThrowForbiddenException_WhenUserHasNoRole() {
////        // given
////        UserRoleResponse mockResponse = UserRoleResponse.builder()
////                .userId(1L)
////                .userRole(UserRole.NONE_PAYER)
////                .status("FAILED")
////                .message("인증 실패")
////                .build();  // Lombok의 Builder 패턴을 사용해 값 설정
////
////        // RestTemplate이 NONE_PAYER인 유저 권한 응답 설정
////        when(restTemplate.postForObject(
////                eq(USER_ROLE_API_URL),
////                ArgumentMatchers.any(HttpEntity.class),
////                eq(UserRoleResponse.class)))
////                .thenReturn(mockResponse);
////
////        // when & then
////        PaymentsException exception = assertThrows(PaymentsException.class, () -> {
////            paymentsAuthService.checkAuthPayment("userKeySample", 100L);
////        });
////
////        // 예외 메시지와 상태 코드 확인
////        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
////        assertEquals("권한이 없는 사용자입니다.", exception.getMessage());
////    }
//
////    @Test
////    @DisplayName("없는 유저 조회시 null값 반환")
////    void checkAuthPayment_ShouldThrowBadRequestException_WhenResponseIsNull() {
////        // given
////        // RestTemplate이 null 응답을 반환하도록 설정
////        when(restTemplate.postForObject(
////                eq(USER_ROLE_API_URL),
////                ArgumentMatchers.any(HttpEntity.class),
////                eq(UserRoleResponse.class)))
////                .thenReturn(null);
////
////        // when & then
////        PaymentsException exception = assertThrows(PaymentsException.class, () -> {
////            paymentsAuthService.checkAuthPayment("userKeySample", 100L);
////        });
////
////        // 예외 메시지와 상태 코드 확인
////        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
////        assertEquals("유저 결제 권한을 받아오지 못 했습니다.", exception.getMessage());
////    }
//
//    @Test
//    @DisplayName("외부 API 호출 실패 테스트")
//    void checkAuthPayment_ShouldThrowInternalServerError_WhenApiCallFails() {
//        // given
//        // RestTemplate이 예외를 던지도록 설정
//        when(restTemplate.postForObject(
//                eq(USER_ROLE_API_URL),
//                ArgumentMatchers.any(HttpEntity.class),
//                eq(UserRoleResponse.class)))
//                .thenThrow(new RestClientException("API 호출 실패"));
//
//        // when & then
//        PaymentsException exception = assertThrows(PaymentsException.class, () -> {
//            paymentsAuthService.checkAuthPayment("userKeySample", 100L);
//        });
//
//        // 예외 메시지와 상태 코드 확인
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
//        assertEquals("권한 조회 외부 API 호출 실패: API 호출 실패", exception.getMessage());
//    }
//}
