package com.space.airesumeanalyzer.controller;

import com.space.airesumeanalyzer.dto.DocumentUploadResponse;
import com.space.airesumeanalyzer.service.DocumentService;
import com.space.airesumeanalyzer.service.FileParserService;
import com.space.airesumeanalyzer.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final S3Service s3Service;
    private final DocumentService documentService;
    private final FileParserService fileParserService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(@RequestParam("file") MultipartFile file) {
        // 1. 파일 형식 검증 및 텍스트 추출
        String extractedText = fileParserService.extractText(file);

        // 2. AWS S3 업로드
        String s3Url = s3Service.uploadFile(file);

        // 3. 임시 유저 ID (인증 미구현 상태)
        Long tempUserId = 1L;

        // 4. 4개의 인자를 모두 전달하여 DB 메타데이터 및 텍스트 저장
        DocumentUploadResponse response = documentService.saveDocumentMetadata(
                tempUserId,
                file.getOriginalFilename(),
                s3Url,
                extractedText
        );

        return ResponseEntity.ok(response);
    }
}