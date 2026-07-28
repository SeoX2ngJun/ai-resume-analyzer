package com.space.airesumeanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // BaseTimeEntity의 시간 감지 센서 및 기록 기계를 최종 기동시키는 필수 활성화 스위치입니다.
@SpringBootApplication
public class AiresumeanalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiresumeanalyzerApplication.class, args);
    }
}