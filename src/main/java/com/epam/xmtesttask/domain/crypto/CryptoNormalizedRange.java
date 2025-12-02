package com.epam.xmtesttask.domain.crypto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable value object representing the normalized range of a cryptocurrency.
 * <p>
 * The normalized range is typically calculated as (max - min) / min for a given period,
 * and is used to compare the relative volatility or price movement of different cryptocurrencies.
 * <p>
 * This record implements {@link Comparable} to allow sorting by normalized range.
 *
 * @param symbol           the cryptocurrency symbol (e.g., "BTC", "ETH"); must not be null
 * @param normalizedRange  the normalized range value; must not be null
 */
public record CryptoNormalizedRange(
        String symbol,
        BigDecimal normalizedRange
) implements Comparable<CryptoNormalizedRange> {

    public CryptoNormalizedRange {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(normalizedRange);
    }

    @Override
    public int compareTo(CryptoNormalizedRange that) {
        return this.normalizedRange.compareTo(that.normalizedRange);
    }
}
