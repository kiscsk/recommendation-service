package com.epam.xmtesttask.domain.crypto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable value object representing statistical data for a cryptocurrency over a given period.
 * <p>
 * This record encapsulates the symbol and key price statistics:
 * <ul>
 *     <li>Oldest: The price at the earliest timestamp in the period</li>
 *     <li>Newest: The price at the latest timestamp in the period</li>
 *     <li>Min: The minimum price observed in the period</li>
 *     <li>Max: The maximum price observed in the period</li>
 * </ul>
 * All fields are required and must not be null.
 *
 * @param symbol the cryptocurrency symbol (e.g., "BTC", "ETH"); must not be null
 * @param oldest the oldest price value in the period; must not be null
 * @param newest the newest price value in the period; must not be null
 * @param min    the minimum price value in the period; must not be null
 * @param max    the maximum price value in the period; must not be null
 */
public record CryptoStats(
        String symbol,
        BigDecimal oldest,
        BigDecimal newest,
        BigDecimal min,
        BigDecimal max
) {
    public CryptoStats {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(oldest);
        Objects.requireNonNull(newest);
        Objects.requireNonNull(min);
        Objects.requireNonNull(max);
    }
}
