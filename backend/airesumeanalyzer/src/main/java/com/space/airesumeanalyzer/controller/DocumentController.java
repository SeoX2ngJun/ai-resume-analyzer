package com.space.airesumeanalyzer.controller;

import com.space.airesumeanalyzer.dto.DocumentUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        // 1. 프론트엔드가 전송한 파일 객체가 백엔드 버퍼에 무사히 걸렸는지 메타데이터 정밀 감시 로그 출력
        log.info("====== [실전 파일 업로드 수신 통제 파이프라인 가동] ======");
        log.info("수신된 파일명: {}", file.getOriginalFilename());
        log.info("파일 물리 크기: {} bytes", file.getSize());
        log.info("콘텐트 타입 스펙: {}", file.getContentType());

        // 2. 외부 인프라(AWS S3 및 OpenAI) 연동 전, API 명세서 규격을 만족하는 가짜(Mock) 정합성 데이터 바인딩
        DocumentUploadResponse response = DocumentUploadResponse.builder()
                .documentId(1L) // 임시 가상 문서 시퀀스 발행
                .fileName(file.getOriginalFilename())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}