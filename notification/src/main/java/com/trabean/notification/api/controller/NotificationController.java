package com.trabean.notification.api.controller;


import com.trabean.notification.api.dto.request.NotificationCreateReq;
import com.trabean.notification.api.dto.response.NotificationReadRes;
import com.trabean.notification.api.feign.UserFeign;
import com.trabean.notification.api.service.NotificationService;
import com.trabean.notification.interceptor.UserHeaderInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final UserFeign userFeign;

    @PostMapping("")
    public ResponseEntity<?> saveNotification(@RequestBody NotificationCreateReq notificationCreateReq) {
        log.info("@@@@@@@@@@@@@컨트롤러진입 (알림생성)");
        notificationService.saveNotification(notificationCreateReq);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getNotification() {
        List<NotificationReadRes> notificationReadResList = notificationService.findByUserId(UserHeaderInterceptor.userId.get());
        return ResponseEntity.ok().body(notificationReadResList);
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<?> updateIsRead(@PathVariable Long notificationId) {
        notificationService.updateIsReadById(notificationId);
        return ResponseEntity.ok().build();
    }

    /**
     * SSE + 단일 스레드 -> 실시간 알림
     * Sse 객체를 통해 로직에서 응답을 제공
     *
     * @return Sse 객체를 반환
     */
    @GetMapping(value = "/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter handleSse() {
        Long userId = UserHeaderInterceptor.userId.get();
        // 무제한 타임아웃 설정을 가진 SseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(60000L);

        // 단일 스레드로 작업을 스케줄링 할 수 있는 ScheduledExecutorService 생성
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // 5초마다 주기적으로 작업을 수행하도록 설정
        executor.scheduleAtFixedRate(() -> {
            try {
                // 사용자 상태를 가져와서 "1" 또는 "0"으로 변환
                String result = notificationService.getStatus(userId) ? "1" : "0";
                // 클라이언트에 결과 전송
                sseEmitter.send(result);
                // 로그에 전송된 정보 기록
                log.info("정보를 한번 보내볼게요 : " + result);
            } catch (IOException e) {
                // IOException 발생 시, SSE 연결 종료 처리
                log.info("정보 보내는 거 종료1");
                sseEmitter.completeWithError(e); // 오류와 함께 SSE 완료
                executor.shutdown(); // 스케줄러 종료
            }
        }, 0, 5, TimeUnit.SECONDS); // 0초 후 시작하고, 5초 간격으로 반복
        // SSE가 완료되면 스케줄러 종료
        sseEmitter.onCompletion(executor::shutdown);
        // SSE가 타임아웃되면 스케줄러 종료
        sseEmitter.onTimeout(executor::shutdown);
        // 로그에 SSE 시작 메시지 기록
        log.info("정보 보내는 거 종료2");

        // SseEmitter 반환하여 클라이언트 연결을 유지
        return sseEmitter;
    }


//    /**
//     * 다중 스레드 버전
//     */
//    @GetMapping(value = "/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter handleSse() {
//        // 무제한 타임아웃 설정을 가진 SseEmitter 객체 생성
//        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
//
//        // 스레드 풀 생성 (예: 최대 5개의 스레드)
//        ScheduledExecutorService executor = Executors.newFixedThreadPool(5);
//
//        // 5초마다 주기적으로 작업을 수행하도록 설정
//        executor.scheduleAtFixedRate(() -> {
//            try {
//                // 사용자 상태를 가져와서 "1" 또는 "0"으로 변환
//                String result = notificationService.getStatus(UserHeaderInterceptor.userId.get()) ? "1" : "0";
//                // 클라이언트에 결과 전송
//                sseEmitter.send(result);
//                // 로그에 전송된 정보 기록
//                log.info("정보를 한번 보내볼게요 : " + result);
//            } catch (IOException e) {
//                // IOException 발생 시, SSE 연결 종료 처리
//                log.info("정보 보내는 거 종료1");
//                sseEmitter.completeWithError(e); // 오류와 함께 SSE 완료
//                executor.shutdown(); // 스케줄러 종료
//            }
//        }, 0, 5, TimeUnit.SECONDS); // 0초 후 시작하고, 5초 간격으로 반복
//
//        // SSE가 완료되면 스케줄러 종료
//        sseEmitter.onCompletion(() -> {
//            executor.shutdown(); // SSE 완료 시 스케줄러 종료
//            log.info("SSE 연결이 종료되었습니다.");
//        });
//        // SSE가 타임아웃되면 스케줄러 종료
//        sseEmitter.onTimeout(() -> {
//            executor.shutdown(); // 타임아웃 시 스케줄러 종료
//            log.info("SSE 연결이 타임아웃되었습니다.");
//        });
//
//        // 로그에 SSE 시작 메시지 기록
//        log.info("정보 보내는 거 시작");
//
//        // SseEmitter 반환하여 클라이언트 연결을 유지
//        return sseEmitter;
//    }


}
