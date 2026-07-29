package com.space.airesumeanalyzer.config;

import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyDataInit implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // 서버가 켜질 때, DB에 1번 유저가 존재하는지 확인하고 없으면 강제 생성
        if (userRepository.findById(1L).isEmpty()) {
            User dummyUser = User.builder()
                    .email("tester@space.com")
                    .name("테스트유저")
                    .password("1234")
                    .build();

            userRepository.save(dummyUser);
            System.out.println("====== [임시 테스트 유저(ID: 1) 자동 생성 완료] ======");
        }
    }
}