package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.AiReport;
import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.dto.OpenAIRequest;
import com.space.airesumeanalyzer.dto.OpenAIResponse;
import com.space.airesumeanalyzer.repository.AiReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final AiReportRepository aiReportRepository;
    private final RestClient restClient = RestClient.create();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Transactional
    public AiReport analyzeAndSaveReport(Document document, String extractedText) {
        String prompt = "다음 자기소개서를 분석하여 주요 강점, 보완할 점, 그리고 총평을 요약해 주세요.\n\n[자기소개서 본문]\n" + extractedText;

        OpenAIRequest requestBody = OpenAIRequest.builder()
                .model(model)
                .temperature(0.3)
                .messages(List.of(
                        OpenAIRequest.Message.builder().role("system").content("당신은 전문 채용 담당자이자 자기소개서 컨설턴트입니다.").build(),
                        OpenAIRequest.Message.builder().role("user").content(prompt).build()
                ))
                .build();

        OpenAIResponse response = restClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(OpenAIResponse.class);

        String aiAnalysisContent = "분석 결과가 없습니다.";
        if (response != null && !response.getChoices().isEmpty()) {
            aiAnalysisContent = response.getChoices().get(0).getMessage().getContent();
        }

        // AiReport 엔티티 생성 및 연관관계 매핑
        AiReport aiReport = AiReport.builder()
                .document(document)
                .reportContent(aiAnalysisContent)
                .passRate(85) // 임시 점수 또는 파싱 결과 반영
                .build();

        return aiReportRepository.save(aiReport);
    }
}