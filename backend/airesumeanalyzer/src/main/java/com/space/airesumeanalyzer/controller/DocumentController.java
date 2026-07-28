package com.space.airesumeanalyzer.controller;

import com.space.airesumeanalyzer.dto.DocumentUploadResponse;
import com.space.airesumeanalyzer.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor // DocumentService 의존성 주입을 위한 어노테이션
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        log.info("====== [실전 파일 업로드 수신 통제 파이프라인 가동] ======");
        log.info("수신된 파일명: {}", file.getOriginalFilename());
        log.info("파일 물리 크기: {} bytes", file.getSize());

        // TODO 1: 인증(JWT/Session) 기능 구현 후 실제 로그인한 유저의 PK로 대체
        Long tempUserId = 1L;

        // TODO 2: AWS S3 연동 후 실제 업로드된 객체 URL로 대체
        String tempS3Url = "https://s3.aws.mock.url/temp-file.pdf";

        // 1. 서비스 계층으로 데이터 전달 및 DB 영속화 (실제 Document 엔티티의 PK 획득)
        Long savedDocumentId = documentService.saveDocumentMetadata(tempUserId, file.getOriginalFilename(), tempS3Url);

        // 2. 프론트엔드 API 명세서 규격에 맞춘 응답 DTO 생성
        DocumentUploadResponse response = DocumentUploadResponse.builder()
                .documentId(savedDocumentId) // DB에서 발급된 실제 ID 반환
                .fileName(file.getOriginalFilename())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}