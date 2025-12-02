package com.epam.xmtesttask.domain.crypto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable value object representing a single cryptocurrency price entry.
 * <p>
 * Each instance contains the UTC date and time of the price observation and the price value itself.
 * This record is used to model individual price points for cryptocurrencies in the system.
 *
 * @param utcDateTime   the UTC date and time of the price entry; must not be null
 * @param price         the price value for the cryptocurrency at the given time; must not be null
 */
public record CryptoPrice(
        LocalDateTime utcDateTime,
        BigDecimal price
) {
    public CryptoPrice {
        Objects.requireNonNull(utcDateTime);
        Objects.requireNonNull(price);
    }
}
