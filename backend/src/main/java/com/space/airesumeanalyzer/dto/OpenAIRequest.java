package com.space.airesumeanalyzer.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class OpenAIRequest {

    private String model;
    private List<Message> messages;
    private double temperature;

    // OpenAI 응답 형식을 JSON Schema로 강제
    private ResponseFormat response_format;

    @Getter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }

    @Getter
    @Builder
    public static class ResponseFormat {
        private String type;
        private JsonSchema json_schema;
    }

    @Getter
    @Builder
    public static class JsonSchema {
        private String name;
        private boolean strict;
        private Map<String, Object> schema;
    }
}