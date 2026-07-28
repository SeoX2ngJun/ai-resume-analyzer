package com.space.airesumeanalyzer.controller;

import com.space.airesumeanalyzer.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 파일 업로드 용량(10MB) 초과 예외 낚아채기
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.error("파일 용량 초과 예외 발생: {}", e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ERR_FILE_SIZE_EXCEEDED")
                .errorMessage("업로드 가능한 파일의 최대 크기는 10MB입니다.")
                .build();

        // 400 Bad Request 상태 코드와 함께 응답
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 2. 회원가입 이메일 중복 등 비즈니스 로직 예외 낚아채기 (IllegalStateException)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error("비즈니스 로직 예외 발생: {}", e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ERR_BUSINESS_LOGIC")
                .errorMessage(e.getMessage()) // UserService에서 던진 메시지("이미 존재하는 회원입니다.")를 그대로 사용
                .build();

        // 400 Bad Request 상태 코드와 함께 응답
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}