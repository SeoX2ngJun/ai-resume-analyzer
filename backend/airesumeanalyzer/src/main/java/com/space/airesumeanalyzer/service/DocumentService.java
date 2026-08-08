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

    @Transactional
    public DocumentUploadResponse saveDocumentMetadata(Long userId, String fileName, String s3Url, String extractedText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. ID: " + userId));

        Document document = Document.builder()
                .fileName(fileName)
                .s3Url(s3Url)
                .extractedText(extractedText)
                .status("SUCCESS") // 요구사항에 맞춘 상태값 또는 기존 로직 유지
                .user(user)
                .build();

        Document savedDocument = documentRepository.save(document);

        return DocumentUploadResponse.builder()
                .documentId(savedDocument.getId())
                .fileName(savedDocument.getFileName())
                .status(savedDocument.getStatus())
                .createdAt(savedDocument.getCreatedAt())
                .build();
    }
}