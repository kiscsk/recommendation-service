package com.epam.xmtesttask.repository;

import com.epam.xmtesttask.domain.crypto.CryptoPrice;
import com.epam.xmtesttask.domain.exception.CryptoNotFoundException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In-memory implementation of the {@link PriceRepository} interface.
 * <p>
 * Stores cryptocurrency price data in a thread-unsafe {@link HashMap} for fast access and testing purposes.
 * <p>
 * This implementation is suitable for development, testing, or small-scale deployments where persistence is not required.
 * For production use, consider a database-backed implementation.
 */
@Repository
public class InMemoryPriceRepository implements PriceRepository {

    /**
     * Internal map storing lists of {@link CryptoPrice} objects for each supported symbol.
     * The key is the cryptocurrency symbol (e.g., "BTC", "ETH").
     */
    private final Map<String, List<CryptoPrice>> prices = new HashMap<>();

    @Override
    public void savePrice(String symbol, LocalDateTime utcDateTime, BigDecimal price) {
        prices.putIfAbsent(symbol, new ArrayList<>());
        prices.get(symbol).add(
                new CryptoPrice(utcDateTime, price)
        );
    }

    @Override
    public boolean isSupported(String symbol) {
        return prices.containsKey(symbol) && !prices.get(symbol).isEmpty();
    }

    @Override
    public Set<String> getSupportedCryptos() {
        return prices.keySet();
    }

    @Override
    public List<CryptoPrice> getPrices(String symbol) {
        if (!isSupported(symbol)) {
            throw new CryptoNotFoundException(symbol);
        }
        return prices.get(symbol);
    }
}
