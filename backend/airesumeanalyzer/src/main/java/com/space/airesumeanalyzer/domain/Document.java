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

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "s3_url", nullable = false, length = 1000) // 파일이 저장될 AWS S3 주소 칸입니다.
    private String s3Url;

    @Column(name = "extracted_text", columnDefinition = "TEXT") // 자소서 전체 본문이 들어갈 수 있도록 대용량 TEXT 타입으로 지정했습니다.
    private String extractedText;

    @Column(name = "status", nullable = false, length = 20) // PENDING(분석중), SUCCESS(성공), FAILED(실패) 상태 관리용입니다.
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 어떤 유저가 올린 자소서인지 알려주는 연결 다리(외래키)입니다.
    private User user;

    // cascade: 자소서 저장/삭제 시 리포트도 함께 세트로 처리
    // orphanRemoval: 자소서와 리포트 간의 연결 고리가 끊어지면 고아가 된 리포트 데이터를 DB에서 자동 파쇄
    @OneToOne(mappedBy = "document", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private AiReport aiReport;

    @Builder
    public Document(String fileName, String s3Url, String extractedText, String status, User user) {
        this.fileName = fileName;
        this.s3Url = s3Url;
        this.extractedText = extractedText;
        this.status = status;
        this.user = user;
    }

    // 연관관계 편의 메서드: 자바 객체 세상에서 자소서와 리포트가 서로 양방향으로 꼬임 없이 완벽하게 한 세트로 묶이도록 보장합니다.
    public void setAiReport(AiReport aiReport) {
        this.aiReport = aiReport;
        if (aiReport != null && aiReport.getDocument() != this) {
            aiReport.setDocument(this);
        }
    }
}