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

    @Column(nullable = false, length = 1000)
    private String s3Url;

    @Column(columnDefinition = "TEXT") // 대용량 자소서 본문을 온전히 적재하기 위해 PostgreSQL의 TEXT 타입을 지정합니다.
    private String extractedText;

    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY) // N+1 문제 방지를 위해 무조건 지연 로딩을 선언합니다.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 일대일 양방향 관계로, 자소서 삭제 시 AI 분석 리포트 데이터가 고아로 남아 누수되는 현상을 원천 차단합니다.
    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private AiReport aiReport;

    @Builder
    public Document(String fileName, String s3Url, String extractedText, String status, User user) {
        this.fileName = fileName;
        this.s3Url = s3Url;
        this.extractedText = extractedText;
        this.status = status;
        this.user = user;
    }
}