package com.space.airesumeanalyzer.repository;

import com.space.airesumeanalyzer.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // 특정 유저(user_id)가 업로드한 문서 목록을 최신순으로 조회하기 위한 쿼리 메서드
    List<Document> findByUserIdOrderByCreatedAtDesc(Long userId);
}