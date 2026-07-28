package com.space.airesumeanalyzer.repository;

import com.space.airesumeanalyzer.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 스프링 부트 컨텍스트를 전체 구동하여 실제 인프라와 연동하는 통합 테스트 선언
@Transactional  // 테스트 완료 후 데이터베이스를 자동으로 롤백(Rollback)하여 데이터 정합성을 유지하는 안전장치
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 정보 저장 및 이메일 조회 무결성 검증")
    public void saveAndFindUserTest() {
        // 1. Given (테스트를 위한 가짜 유저 데이터 준비)
        User testUser = User.builder()
                .name("테스터")
                .email("test@space.com")
                .password("securePassword123!")
                .build();

        // 2. When (실제 PostgreSQL 창고에 저장 및 이메일 기반 조회 실행)
        User savedUser = userRepository.save(testUser);
        Optional<User> foundUserOpt = userRepository.findByEmail("test@space.com");

        // 3. Then (데이터베이스 I/O 결과의 정합성 검증)
        assertThat(foundUserOpt).isPresent(); // 데이터가 반드시 존재해야 함
        User foundUser = foundUserOpt.get();

        assertThat(foundUser.getName()).isEqualTo("테스터");
        assertThat(foundUser.getEmail()).isEqualTo("test@space.com");

        // BaseTimeEntity에 의해 생성 시간이 자동으로 기록되었는지 데이터 무결성 검증
        assertThat(foundUser.getCreatedAt()).isNotNull();
        assertThat(foundUser.getUpdatedAt()).isNotNull();

        System.out.println("====== DB 저장 완료 및 검증 성공 ======");
        System.out.println("저장된 유저 ID: " + savedUser.getId());
        System.out.println("생성 시간: " + foundUser.getCreatedAt());
    }
}