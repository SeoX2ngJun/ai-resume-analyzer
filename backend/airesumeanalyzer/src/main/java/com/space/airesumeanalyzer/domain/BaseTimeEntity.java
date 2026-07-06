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
@MappedSuperclass // 다른 엔티티들이 이 클래스를 상속받으면 아래 필드들을 자동으로 테이블 컬럼으로 씁니다.
@EntityListeners(AuditingEntityListener.class) // 데이터가 바뀌는 것을 실시간으로 감시하는 스위치입니다.
public abstract class BaseTimeEntity {

    @CreatedDate // 데이터가 처음 저장될 때 시간을 자동으로 박아줍니다.
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터가 수정될 때마다 실시간으로 갱신해줍니다.
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}