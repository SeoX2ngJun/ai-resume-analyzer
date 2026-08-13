package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.Document;
import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.dto.DocumentUploadResponse;
import com.space.airesumeanalyzer.repository.DocumentRepository;
import com.space.airesumeanalyzer.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DocumentServiceTest {

    @Autowired DocumentService documentService;
    @Autowired DocumentRepository documentRepository;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("자소서 메타데이터 및 추출 텍스트 DB 영속화 검증")
    void saveDocumentMetadataTest() {
        // given (외래키 무결성을 위해 가상 유저를 먼저 DB에 등록)
        User testUser = User.builder()
                .email("doc-tester@space.com")
                .password("pwd123")
                .name("자소서테스터")
                .build();
        userRepository.save(testUser);

        String testFileName = "백엔드_이력서_최종.pdf";
        String testS3Url = "https://s3.aws.test/bucket/doc.pdf";
        String testExtractedText = "이것은 PDF에서 파싱된 가상의 자기소개서 본문 텍스트입니다.";

        // when (4개의 인자를 전달하여 서비스 로직 호출 및 DTO 응답 수신)
        DocumentUploadResponse response = documentService.saveDocumentMetadata(
                testUser.getId(),
                testFileName,
                testS3Url,
                testExtractedText
        );

        // then (DB에서 다시 조회하여 데이터 정합성 확인)
        Document savedDoc = documentRepository.findById(response.getDocumentId()).orElseThrow();

        assertThat(savedDoc.getFileName()).isEqualTo(testFileName);
        assertThat(savedDoc.getS3Url()).isEqualTo(testS3Url);
        assertThat(savedDoc.getExtractedText()).isEqualTo(testExtractedText);
        assertThat(savedDoc.getStatus()).isEqualTo("SUCCESS");

        // Document에 연결된 User의 ID가 최초 등록한 testUser의 ID와 일치하는지 확인
        assertThat(savedDoc.getUser().getId()).isEqualTo(testUser.getId());

        System.out.println("====== Document 및 추출 텍스트 저장 검증 완료 ======");
        System.out.println("생성된 Document ID: " + savedDoc.getId());
    }
}