package com.space.airesumeanalyzer.controller;

import com.space.airesumeanalyzer.dto.ErrorResponse;
import com.space.airesumeanalyzer.exception.InvalidFileTypeException; // 패키지 경로에 맞게 임포트
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 2. 지원하지 않는 파일 형식 예외 낚아채기 (추가된 부분)
     */
    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileTypeException(InvalidFileTypeException e) {
        log.error("지원하지 않는 파일 형식 예외 발생: {}", e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ERR_INVALID_FILE_TYPE")
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 3. 회원가입 이메일 중복 등 비즈니스 로직 예외 낚아채기 (IllegalStateException)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error("비즈니스 로직 예외 발생: {}", e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ERR_BUSINESS_LOGIC")
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}