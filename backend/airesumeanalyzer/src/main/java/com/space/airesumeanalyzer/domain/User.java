package com.space.airesumeanalyzer.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // 데이터베이스 테이블명을 'users'로 지정합니다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 의미 없는 빈 객체 생성을 막는 안전장치입니다.
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PostgreSQL 16의 ID 순차적 발행 기법을 적용합니다.
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true) // 로그인 및 이메일 식별을 위한 유니크 조건을 부여합니다.
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    // N+1 병목 방지를 위해 지연 로딩(LAZY)을 기본으로 하며, User 소멸 시 하위 자소서도 세트로 정리되게 합니다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @Builder // 안전한 빌더 패턴을 적용하여 데이터 생성 정합성을 유지합니다.
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}