package com.space.airesumeanalyzer.service;

import com.space.airesumeanalyzer.domain.User;
import com.space.airesumeanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 성능 상의 최적화를 위해 읽기 전용 상태를 기본으로 세팅
@RequiredArgsConstructor // 레포지토리를 안전하게 자동으로 주입해주는 롬복 생성자 스티커
public class UserService {

    private final UserRepository userRepository;

    @Transactional // 데이터 쓰기 및 저장이 일어나는 비즈니스 메서드는 쓰기용 트랜잭션을 수동 선언
    public Long join(User user) {
        validateDuplicateUser(user); // 가입시키기 전 이메일 중복 체크 규칙을 우선 수행
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}