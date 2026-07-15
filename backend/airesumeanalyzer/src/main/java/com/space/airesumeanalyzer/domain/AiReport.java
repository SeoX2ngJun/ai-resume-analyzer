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

    @Column(nullable = false)
    private int passRate;

    @Column(nullable = false, columnDefinition = "TEXT") // 거대한 결과 JSON 문장을 적재하기 위해 TEXT 명세 지정
    private String reportContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false, unique = true) // unique=true로 데이터 단에서 1:1 무결성을 유지합니다.
    private Document document;

    @Builder
    public AiReport(int passRate, String reportContent, Document document) {
        this.passRate = passRate;
        this.reportContent = reportContent;
        this.document = document;
    }
}