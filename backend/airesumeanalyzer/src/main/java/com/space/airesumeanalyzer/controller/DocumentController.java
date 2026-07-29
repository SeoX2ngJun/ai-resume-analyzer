package com.space.airesumeanalyzer.controller;

import com.space.airesumeanalyzer.dto.DocumentUploadResponse;
import com.space.airesumeanalyzer.service.DocumentService;
import com.space.airesumeanalyzer.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final S3Service s3Service; // S3Service 주입

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        log.info("====== [실전 파일 업로드 수신 통제 파이프라인 가동] ======");
        log.info("수신된 파일명: {}", file.getOriginalFilename());
        log.info("파일 물리 크기: {} bytes", file.getSize());

        // TODO 1: 인증(JWT/Session) 기능 구현 후 실제 로그인한 유저의 PK로 대체
        Long tempUserId = 1L;

        // 1. AWS S3에 파일 업로드 및 실제 URL 획득 (가짜 URL 제거)
        String actualS3Url = s3Service.uploadFile(file);
        log.info("AWS S3 업로드 완료. URL: {}", actualS3Url);

        // 2. 서비스 계층으로 데이터 전달 및 DB 영속화 (실제 S3 URL 전달)
        Long savedDocumentId = documentService.saveDocumentMetadata(tempUserId, file.getOriginalFilename(), actualS3Url);

        // 3. 프론트엔드 API 명세서 규격에 맞춘 응답 DTO 생성
        DocumentUploadResponse response = DocumentUploadResponse.builder()
                .documentId(savedDocumentId)
                .fileName(file.getOriginalFilename())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}