package com.dipanshushukla.rate_limiter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private RedisTemplate<String, String> template;

    @Value("${request-limit-per-minute}")
    private Integer ALLOWED_REQUEST_LIMIT;

    SecurityConfig(RedisTemplate<String, String> template) {
        this.template = template;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .addFilterBefore(new RateLimiterFilter(template, ALLOWED_REQUEST_LIMIT),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
