package com.epam.xmtesttask.domain.exception;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String ip) {
        super("Rate Limit Exceeded from ip: " + ip);
    }
}
