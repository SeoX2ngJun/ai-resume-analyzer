package com.space.airesumeanalyzer.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_report_id")
    private Long id;

    @Column(name = "report_content", nullable = false, columnDefinition = "TEXT") // AI가 내뱉은 복잡한 JSON 결과를 통째로 보관합니다.
    private String reportContent;

    @Column(name = "pass_rate", nullable = false)
    private Integer passRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false, unique = true) // 1:1 관계의 명확한 주인을 선언하며 중복 생성을 완전 차단합니다.
    private Document document;

    @Builder
    public AiReport(String reportContent, Integer passRate, Document document) {
        this.reportContent = reportContent;
        this.passRate = passRate;
        this.document = document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}