package com.epam.xmtesttask.service;

import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet filter for IP-based rate limiting using Bucket4j.
 * <p>
 * This filter restricts the number of requests a client (identified by IP address) can make
 * within a given time window. If the rate limit is exceeded, the filter responds with HTTP 429 (Too Many Requests).
 * <p>
 * The rate limit is configured as 60 requests per minute, refilling at a rate of 6 requests every minute.
 */
@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429);
            httpResp.getWriter().write("Too Many Requests");
        }
    }

    private Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> Bucket.builder()
                .addLimit(BandwidthBuilder.builder().capacity(60).refillGreedy(6, Duration.ofMinutes(1)).build())
                .build());
    }
}
