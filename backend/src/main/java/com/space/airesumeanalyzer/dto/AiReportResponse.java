package com.space.airesumeanalyzer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiReportResponse {
    private final Long documentId;
    private final String fileName;
    private final ReportDetail aiReport;

    @Getter
    @Builder
    public static class ReportDetail {
        private final String summary;
        private final double passRate;
    }
}