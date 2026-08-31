package com.space.airesumeanalyzer.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(name = "s3_url", nullable = false, length = 1000)
    private String s3Url;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(
            mappedBy = "document",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AiReport aiReport;

    @Builder
    public Document(
            String fileName,
            String s3Url,
            String extractedText,
            DocumentStatus status,
            User user
    ) {
        this.fileName = fileName;
        this.s3Url = s3Url;
        this.extractedText = extractedText;
        this.status = status;
        this.user = user;
    }

    public void changeStatus(DocumentStatus status) {
        this.status = status;
    }
}