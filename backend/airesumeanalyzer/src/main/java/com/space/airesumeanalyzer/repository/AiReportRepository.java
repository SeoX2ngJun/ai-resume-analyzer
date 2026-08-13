package com.space.airesumeanalyzer.repository;

import com.space.airesumeanalyzer.domain.AiReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiReportRepository extends JpaRepository<AiReport, Long> {
}