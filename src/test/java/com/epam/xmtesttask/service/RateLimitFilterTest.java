package com.epam.xmtesttask.service;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RateLimitFilter();
    }

    @Test
    void doFilter_shouldAllowRequest_whenBucketHasTokens() throws IOException, ServletException {
        // Given: a request from IP "127.0.0.1" and a bucket with available tokens
        String ip = "127.0.0.1";
        ServletRequest request = mock(ServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRemoteAddr()).thenReturn(ip);

        // Inject a mock bucket into the filter's internal map
        Bucket mockBucket = mock(Bucket.class);
        when(mockBucket.tryConsume(1)).thenReturn(true);
        // Use reflection to access the private buckets field
        Map<String, Bucket> buckets = (Map<String, Bucket>)
                getPrivateField(filter, "buckets");
        buckets.put(ip, mockBucket);

        // When: doFilter is called
        filter.doFilter(request, response, chain);

        // Then: the filter chain should be called, and no error response sent
        verify(chain, times(1)).doFilter(request, response);
        // No status should be set on response
        verifyNoInteractions(response);
    }

    @Test
    void doFilter_shouldRejectRequest_whenBucketHasNoTokens() throws IOException, ServletException {
        // Given: a request from IP "127.0.0.1" and a bucket with no available tokens
        String ip = "127.0.0.1";
        ServletRequest request = mock(ServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRemoteAddr()).thenReturn(ip);

        // Inject a mock bucket into the filter's internal map
        Bucket mockBucket = mock(Bucket.class);
        when(mockBucket.tryConsume(1)).thenReturn(false);
        Map<String, Bucket> buckets = (Map<String, Bucket>)
                getPrivateField(filter, "buckets");
        buckets.put(ip, mockBucket);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // When: doFilter is called
        filter.doFilter(request, response, chain);

        // Then: the response should be set to 429 and "Too Many Requests" written, filter chain not called
        verify(response, times(1)).setStatus(429);
        verify(writer, times(1)).write("Too Many Requests");
        verify(chain, never()).doFilter(request, response);
    }

    /**
     * Utility method to access private fields via reflection.
     */
    private Object getPrivateField(Object target, String fieldName) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}