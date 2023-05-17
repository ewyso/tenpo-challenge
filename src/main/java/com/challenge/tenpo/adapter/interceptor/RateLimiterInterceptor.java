package com.challenge.tenpo.adapter.interceptor;

import com.challenge.tenpo.application.exception.RateLimitException;
import com.challenge.tenpo.config.ErrorCode;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

public class RateLimiterInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    public RateLimiterInterceptor(int requestsPerMinute) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(requestsPerMinute)
                .timeoutDuration(Duration.ofMillis(1))
                .build();
        this.rateLimiter = RateLimiter.of("customRateLimiter", config);
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (rateLimiter.acquirePermission(1)) {
            return true;
        } else {
            throw new RateLimitException(ErrorCode.RATE_LIMIT_EXCEED_ERROR);
        }
    }

}
