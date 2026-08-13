package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("새로운 회원은 정상적으로 가입(DB 적재)에 성공해야 한다")
    void userJoinSuccessTest() {
        User user = User.builder()
                .email("new-user-test@space.com")
                .password("password123")
                .name("신규가입")
                .build();

        Long savedId = userService.join(user);

        User findUser = userRepository.findById(savedId).orElseThrow();
        assertThat(findUser.getEmail()).isEqualTo("new-user-test@space.com");
    }

    @Test
    @DisplayName("동일한 이메일로 가입을 시도할 경우 IllegalStateException 예외가 터져야 한다")
    void userJoinDuplicateExceptionTest() {
        User user1 = User.builder()
                .email("duplicate-check@space.com")
                .password("pwd1")
                .name("유저1")
                .build();
        userService.join(user1);

        User user2 = User.builder()
                .email("duplicate-check@space.com")
                .password("pwd2")
                .name("유저2")
                .build();

        assertThatThrownBy(() -> userService.join(user2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 회원입니다.");
    }
}