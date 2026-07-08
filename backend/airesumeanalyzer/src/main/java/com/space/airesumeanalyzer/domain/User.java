package com.space.airesumeanalyzer.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // PostgreSQL에 'users'라는 이름의 테이블로 생성됩니다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 코딩 실수로 의미 없는 빈 객체가 낭비되는 것을 막는 안전장치입니다.
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호표 발행을 PostgreSQL 데이터베이스 엔진에 전적으로 위임합니다.
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100) // 중복 이메일 가입 절대 차단 규칙
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // 지연 로딩(LAZY): 회원의 이름만 부를 땐 자소서를 가져오지 않고, 진짜 자소서 목록을 열 때만 DB에서 꺼내옵니다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    @Builder // 필요한 데이터만 순서에 상관없이 깔끔하게 채워서 객체를 만드는 디자인 패턴입니다.
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}