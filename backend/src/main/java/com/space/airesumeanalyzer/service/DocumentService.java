package com.space.airesumeanalyzer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.airesumeanalyzer.domain.AiReport;
import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.domain.DocumentStatus;
import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.dto.AiAnalysisResult;
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
    private final ObjectMapper objectMapper;

    public DocumentUploadResponse saveDocumentMetadata(
            Long userId,
            String fileName,
            String s3Url,
            String extractedText
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 문서 생성 직후에는 아직 AI 분석이 끝나지 않았으므로 PROCESSING
        Document document = Document.builder()
                .fileName(fileName)
                .s3Url(s3Url)
                .extractedText(extractedText)
                .status(DocumentStatus.PROCESSING)
                .user(user)
                .build();

        // PROCESSING 상태를 먼저 DB에 저장
        document = documentRepository.saveAndFlush(document);

        try {
            // AI 분석 및 AiReport 저장
            aiAnalysisService.analyzeAndSaveReport(document, extractedText);

            // AI 분석 성공
            document.changeStatus(DocumentStatus.SUCCESS);
            documentRepository.save(document);

        } catch (Exception e) {

            // AI 분석 실패
            document.changeStatus(DocumentStatus.FAILED);
            documentRepository.save(document);

            throw e;
        }

        return DocumentUploadResponse.builder()
                .documentId(document.getId())
                .fileName(document.getFileName())
                .status(document.getStatus().name())
                .createdAt(document.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public AiReportResponse getDocumentReport(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 문서입니다."));

        AiReport report = document.getAiReport();

        if (report == null) {
            throw new IllegalStateException(
                    "아직 AI 분석 결과가 존재하지 않습니다."
            );
        }

        try {
            AiAnalysisResult analysisResult =
                    objectMapper.readValue(
                            report.getReportContent(),
                            AiAnalysisResult.class
                    );

            return AiReportResponse.builder()
                    .documentId(document.getId())
                    .fileName(document.getFileName())
                    .aiReport(
                            AiReportResponse.ReportDetail.builder()
                                    .summary(analysisResult.getSummary())
                                    .strengths(analysisResult.getStrengths())
                                    .weaknesses(analysisResult.getWeaknesses())
                                    .passRate(analysisResult.getPassRate())
                                    .build()
                    )
                    .build();

        } catch (JsonProcessingException e) {

            throw new IllegalStateException(
                    "저장된 AI 분석 결과를 읽는 중 오류가 발생했습니다.",
                    e
            );
        }
    }
}