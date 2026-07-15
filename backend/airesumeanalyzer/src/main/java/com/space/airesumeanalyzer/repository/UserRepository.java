package com.space.airesumeanalyzer.repository;

import com.space.airesumeanalyzer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 이 인터페이스가 데이터베이스 입출력을 전담하는 파일임을 스프링 컨텍스트에 선포합니다.
public interface UserRepository extends JpaRepository<User, Long> {

    // JpaRepository<대상엔티티, PK타입>을 상속받으면 save(), findById() 등은 공짜로 제공됩니다.
    // 이메일을 기준으로 중복 검사나 로그인을 처리하기 위해 JPA 쿼리 메서드 스펙을 명시합니다.
    Optional<User> findByEmail(String email);
}