package com.epam.xmtesttask.repository;

import com.epam.xmtesttask.domain.crypto.CryptoPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Repository interface for managing cryptocurrency price data.
 * <p>
 * Provides methods for saving price entries, checking symbol support,
 * retrieving supported cryptocurrencies, and fetching price history.
 * <p>
 * Implementations of this interface are responsible for the actual data storage and retrieval,
 * which may be in-memory, database-backed, or file-based.
 */
public interface PriceRepository {

    /**
     * Saves a price entry for a given cryptocurrency symbol at a specific UTC date and time.
     *
     * @param symbol      the cryptocurrency symbol (e.g., "BTC", "ETH")
     * @param utcDateTime the UTC date and time of the price entry
     * @param price       the price value to save
     */
    void savePrice(String symbol, LocalDateTime utcDateTime, BigDecimal price);

    /**
     * Checks if the given cryptocurrency symbol is supported.
     *
     * @param symbol the cryptocurrency symbol to check
     * @return {@code true} if the symbol is supported, {@code false} otherwise
     */
    boolean isSupported(String symbol);

    /**
     * Returns a set of all supported cryptocurrency symbols.
     *
     * @return a set of supported symbols (e.g., {"BTC", "ETH"})
     */
    Set<String> getSupportedCryptos();

    /**
     * Retrieves the list of price entries for the specified cryptocurrency symbol.
     * <p>
     * The returned list is typically sorted by date and time in ascending order.
     *
     * @param symbol the cryptocurrency symbol for which to retrieve prices
     * @return a list of {@link CryptoPrice} objects for the given symbol;
     *         may be empty if no prices are available
     */
    List<CryptoPrice> getPrices(String symbol);
}
