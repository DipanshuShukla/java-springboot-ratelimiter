package com.dipanshushukla.rate_limiter.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate<String, String> template;

    private static final String STRING_KEY_PREFIX = "rate-limit:";

    private Integer ALLOWED_REQUEST_LIMIT;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        String key = STRING_KEY_PREFIX + ip;

        String value = template.opsForValue().get(key);

        if (value != null && Integer.parseInt(value) >= ALLOWED_REQUEST_LIMIT) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Oops, You exhausted your request limits... 😵");
            response.getWriter().flush();
            log.info("Blocked incomming request from ip: " + ip);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
