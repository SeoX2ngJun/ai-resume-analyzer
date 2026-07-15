package com.space.airesumeanalyzer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 자식 엔티티 클래스들에게 매핑 정보(시간 필드)를 물려주는 어노테이션입니다.
@EntityListeners(AuditingEntityListener.class) // 등록/수정 시간을 자동으로 관리해주는 리스너를 장착합니다.
public abstract class BaseTimeEntity {

    @CreatedDate // 데이터가 최초 저장될 때 자동으로 생성 시간이 채워집니다.
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터가 수정될 때 자동으로 수정 시간이 채워집니다.
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}