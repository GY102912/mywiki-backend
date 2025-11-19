package com.jian.community.global.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    Bucket bucket() {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(100) // 버킷 최대 용량
                .refillIntervally(10, Duration.ofMinutes(1)) // 1분마다 10개의 토큰 리필
                .build();

        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }
}
