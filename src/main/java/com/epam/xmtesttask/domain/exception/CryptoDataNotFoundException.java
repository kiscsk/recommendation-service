package com.epam.xmtesttask.domain.exception;

import java.time.LocalDate;

public class CryptoDataNotFoundException extends RuntimeException {
    public CryptoDataNotFoundException(String symbol, LocalDate date) {
        super("Crypto (" + symbol + ") data not found for date: " + date);
    }
}
