package com.space.airesumeanalyzer.repository;

import com.space.airesumeanalyzer.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    // 필요 시 findById 페치 조인 활용 가능
}