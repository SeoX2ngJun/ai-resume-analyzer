package com.space.airesumeanalyzer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.airesumeanalyzer.domain.AiReport;
import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.dto.AiAnalysisResult;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final AiReportRepository aiReportRepository;

    // JSON 문자열 <-> Java 객체 변환
    private final ObjectMapper objectMapper;

    private final RestClient restClient = RestClient.create();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Transactional
    public AiReport analyzeAndSaveReport(Document document, String extractedText) {

        String prompt =
                "다음 자기소개서를 분석해 주세요.\n\n"
                        + "[자기소개서 본문]\n"
                        + extractedText;

        /*
         * OpenAI에게 요구할 JSON 구조 정의
         *
         * {
         *   "summary": "...",
         *   "strengths": ["...", "..."],
         *   "weaknesses": ["...", "..."],
         *   "passRate": 80
         * }
         */
        Map<String, Object> schema = Map.of(
                "type", "object",

                "properties", Map.of(
                        "summary", Map.of(
                                "type", "string"
                        ),

                        "strengths", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "string"
                                )
                        ),

                        "weaknesses", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "string"
                                )
                        ),

                        "passRate", Map.of(
                                "type", "integer",
                                "minimum", 0,
                                "maximum", 100
                        )
                ),

                "required", List.of(
                        "summary",
                        "strengths",
                        "weaknesses",
                        "passRate"
                ),

                "additionalProperties", false
        );

        OpenAIRequest.ResponseFormat responseFormat =
                OpenAIRequest.ResponseFormat.builder()
                        .type("json_schema")
                        .json_schema(
                                OpenAIRequest.JsonSchema.builder()
                                        .name("resume_analysis")
                                        .strict(true)
                                        .schema(schema)
                                        .build()
                        )
                        .build();

        OpenAIRequest requestBody = OpenAIRequest.builder()
                .model(model)
                .temperature(0.3)
                .messages(List.of(

                        OpenAIRequest.Message.builder()
                                .role("system")
                                .content(
                                        "당신은 전문 채용 담당자이자 자기소개서 컨설턴트입니다. "
                                                + "지원자의 자기소개서를 객관적으로 분석하세요. "
                                                + "passRate는 자기소개서 완성도를 기준으로 0부터 100 사이 정수로 평가하세요."
                                )
                                .build(),

                        OpenAIRequest.Message.builder()
                                .role("user")
                                .content(prompt)
                                .build()
                ))
                .response_format(responseFormat)
                .build();

        OpenAIResponse response = restClient.post()
                .uri(apiUrl)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(OpenAIResponse.class);

        // OpenAI 응답 유효성 검사
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()
                || response.getChoices().get(0).getMessage() == null) {

            throw new IllegalStateException(
                    "OpenAI 분석 결과를 받아오지 못했습니다."
            );
        }

        String aiAnalysisContent =
                response.getChoices()
                        .get(0)
                        .getMessage()
                        .getContent();

        try {
            // OpenAI가 반환한 JSON 문자열을 Java 객체로 변환
            AiAnalysisResult analysisResult =
                    objectMapper.readValue(
                            aiAnalysisContent,
                            AiAnalysisResult.class
                    );

            AiReport aiReport = AiReport.builder()
                    .document(document)

                    // JSON 전체 문자열을 DB에 저장
                    .reportContent(aiAnalysisContent)

                    // 기존 85 하드코딩 제거
                    .passRate(analysisResult.getPassRate())

                    .build();

            return aiReportRepository.save(aiReport);

        } catch (JsonProcessingException e) {

            throw new IllegalStateException(
                    "AI 분석 결과 JSON 파싱에 실패했습니다.",
                    e
            );
        }
    }
}