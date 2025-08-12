package com.dipanshushukla.rate_limiter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/ping")
public class RateLimiterTestController {

    private RedisTemplate<String, String> template;

    private static final String STRING_KEY_PREFIX = "rate-limit:";

    @Value("${request-limit-per-minute}")
    private Integer ALLOWED_REQUEST_LIMIT;

    RateLimiterTestController(RedisTemplate<String, String> template) {
        this.template = template;
    }

    @GetMapping()
    public String pingMethod(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        String key = STRING_KEY_PREFIX + ip;

        String value = template.opsForValue().get(key);

        if (value == null) {
            template.opsForValue().set(key, "1", Duration.ofMinutes(1));
            return successMessage(ip, 1);
        }

        Integer count = Integer.parseInt(value) + 1;

        template.opsForValue().increment(key);

        return successMessage(ip, count);
    }

    private String successMessage(String ip, int count) {
        return "Awesome!! You are well within your Limit. 😎\n"
                + "Your IP Address: " + ip + "\n"
                + "Requests used: " + count + "/" + ALLOWED_REQUEST_LIMIT;
    }

}
