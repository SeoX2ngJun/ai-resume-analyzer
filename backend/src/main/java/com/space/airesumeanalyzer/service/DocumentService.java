package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.AiReport;
import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.dto.AiReportResponse;
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
    private final AiAnalysisService aiAnalysisService;

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

    /**
     * AI 분석 결과 리포트 상세 조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public AiReportResponse getDocumentReport(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문서 ID입니다: " + documentId));

        if (document.getAiReport() == null) {
            throw new IllegalStateException("해당 문서에 대한 AI 분석 리포트가 아직 생성되지 않았습니다.");
        }

        AiReport report = document.getAiReport();

        AiReportResponse.ReportDetail reportDetail = AiReportResponse.ReportDetail.builder()
                .summary(report.getReportContent())
                .passRate(report.getPassRate())
                .build();

        return AiReportResponse.builder()
                .documentId(document.getId())
                .fileName(document.getFileName())
                .aiReport(reportDetail)
                .build();
    }
}