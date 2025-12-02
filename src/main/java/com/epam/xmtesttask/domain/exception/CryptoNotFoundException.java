package com.epam.xmtesttask.domain.exception;

public class CryptoNotFoundException extends RuntimeException {
    public CryptoNotFoundException(String symbol) {
        super("Crypto not supported or no data: " + symbol);
    }
}
