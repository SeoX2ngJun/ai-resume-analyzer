package com.space.airesumeanalyzer.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class DocumentUploadResponse {
    private final Long documentId;
    private final String fileName;
    private final String status;
    private final LocalDateTime createdAt;

    @Builder
    public DocumentUploadResponse(Long documentId, String fileName, String status, LocalDateTime createdAt) {
        this.documentId = documentId;
        this.fileName = fileName;
        this.status = status;
        this.createdAt = createdAt;
    }
}