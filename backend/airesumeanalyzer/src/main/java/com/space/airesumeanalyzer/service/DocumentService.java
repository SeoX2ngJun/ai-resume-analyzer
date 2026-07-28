package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.repository.DocumentRepository;
import com.space.airesumeanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    /**
     * 자소서 메타데이터 DB 저장 로직 (S3 업로드 직후 호출됨)
     * @param userId 업로드한 유저의 PK
     * @param fileName 원본 파일명
     * @param s3Url S3에 저장된 객체 URL
     * @return 저장된 Document의 PK
     */
    @Transactional
    public Long saveDocumentMetadata(Long userId, String fileName, String s3Url) {
        // 1. 엔티티 조회: 업로드한 유저가 DB에 실제로 존재하는지 검증 및 영속성 컨텍스트로 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + userId));

        // 2. 엔티티 생성: Document 객체 조립 (상태는 최초 업로드 상태인 'UPLOADED'로 명시)
        Document document = Document.builder()
                .fileName(fileName)
                .s3Url(s3Url)
                .extractedText("") // 파싱 및 AI 분석 전이므로 비워둠.
                .status("UPLOADED")
                .user(user)
                .build();

        // 3. 엔티티 저장: DB에 DML(Insert) 쿼리 투영
        documentRepository.save(document);

        return document.getId();
    }
}