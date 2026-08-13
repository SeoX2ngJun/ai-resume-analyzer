package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.dto.DocumentUploadResponse;
import com.space.airesumeanalyzer.repository.DocumentRepository;
import com.space.airesumeanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final AiAnalysisService aiAnalysisService; // AI 분석 서비스 주입 추가

    @Transactional
    public DocumentUploadResponse saveDocumentMetadata(Long userId, String fileName, String s3Url, String extractedText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. ID: " + userId));

        // 1. Document 엔티티 저장
        Document document = Document.builder()
                .fileName(fileName)
                .s3Url(s3Url)
                .extractedText(extractedText)
                .status("SUCCESS")
                .user(user)
                .build();

        Document savedDocument = documentRepository.save(document);

        // 2. OpenAI API 연동 및 AiReport 영속화 호출
        aiAnalysisService.analyzeAndSaveReport(savedDocument, extractedText);

        // 3. 응답 DTO 반환
        return DocumentUploadResponse.builder()
                .documentId(savedDocument.getId())
                .fileName(savedDocument.getFileName())
                .status(savedDocument.getStatus())
                .createdAt(savedDocument.getCreatedAt())
                .build();
    }
}