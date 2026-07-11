package com.space.airesumeanalyzer.repository;

import com.space.airesumeanalyzer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 스프링 부트에게 이 인터페이스가 데이터베이스 창고 지기임을 알립니다.
public interface UserRepository extends JpaRepository<User, Long> {

    // [자바 문법 팁] JpaRepository<대상엔티티, 기본키ID타입>을 상속받으면
    // 기본적으로 save(), findById(), delete() 메서드를 공짜로 쓸 수 있습니다.

    // 비즈니스 규칙: 로그인 및 이메일 중복 검증을 위해 이메일로 회원을 찾는 특수 스티커 메서드 정의
    Optional<User> findByEmail(String email);
}