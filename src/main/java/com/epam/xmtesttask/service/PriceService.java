package com.epam.xmtesttask.service;

import com.epam.xmtesttask.domain.crypto.CryptoNormalizedRange;
import com.epam.xmtesttask.domain.crypto.CryptoPrice;
import com.epam.xmtesttask.domain.crypto.CryptoStats;
import com.epam.xmtesttask.domain.exception.CryptoDataNotFoundException;
import com.epam.xmtesttask.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class providing business logic for cryptocurrency price statistics and recommendations.
 * <p>
 * This service exposes methods to:
 * <ul>
 *     <li>Retrieve statistics (oldest, newest, min, max) for a given crypto symbol.</li>
 *     <li>Calculate normalized ranges for all supported cryptos and sort them in descending order.</li>
 *     <li>Find the crypto with the highest normalized range for a specific date.</li>
 *     <li>Calculate the normalized range for a given set of statistics.</li>
 *     <li>Retrieve statistics for a crypto symbol on a specific date.</li>
 * </ul>
 * <p>
 * The service relies on {@link PriceRepository} for data access and throws
 * {@link CryptoDataNotFoundException} when data is missing for a requested date.
 */
@Service
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;

    /**
     * Retrieves statistics (oldest, newest, min, max) for the specified cryptocurrency symbol.
     *
     * @param symbol the cryptocurrency symbol (e.g., "BTC", "ETH")
     * @return a {@link CryptoStats} object containing the statistics
     */
    public CryptoStats getStats(String symbol) {
        List<CryptoPrice> cryptoPrices = priceRepository.getPrices(symbol);
        return getCryptoStats(symbol, cryptoPrices);
    }

    /**
     * Returns a descending sorted list of all supported cryptos, comparing their normalized range ((max-min)/min).
     *
     * @return a list of {@link CryptoNormalizedRange} objects, sorted by normalized range descending
     */
    public List<CryptoNormalizedRange> getDescNormalizedRanges() {
        return priceRepository.getSupportedCryptos().stream()
                .map(this::getStats)
                .map(this::getNormalizedRange)
                .sorted()
                .toList()
                .reversed();
    }

    /**
     * Returns the crypto with the highest normalized range for a specific date.
     *
     * @param date the date to check
     * @return a {@link CryptoNormalizedRange} object for the crypto with the highest normalized range on the given date
     * @throws CryptoDataNotFoundException if no price data is found for any crypto on the given date
     */
    public CryptoNormalizedRange getHighestNormalizedRange(LocalDate date) {
        return priceRepository.getSupportedCryptos().stream()
                .map(cryptoSymbol -> getStatsForDate(cryptoSymbol, date))
                .map(this::getNormalizedRange)
                .max(CryptoNormalizedRange::compareTo).orElseThrow();
    }

    /**
     * Calculates the normalized range ((max-min)/min) for the given crypto statistics.
     *
     * @param stats the {@link CryptoStats} object containing min and max values
     * @return a {@link CryptoNormalizedRange} object representing the normalized range
     */
    public CryptoNormalizedRange getNormalizedRange(CryptoStats stats) {
        BigDecimal normalizedRange = stats.max().subtract(stats.min()).divide(stats.min(), new MathContext(4, RoundingMode.HALF_UP));
        return new CryptoNormalizedRange(stats.symbol(), normalizedRange);
    }

    private CryptoStats getStatsForDate(String symbol, LocalDate date) {
        List<CryptoPrice> cryptoPrices = priceRepository.getPrices(symbol).stream()
                .filter(price -> price.utcDateTime().toLocalDate().isEqual(date)).toList();
        if (cryptoPrices.isEmpty()) {
            throw new CryptoDataNotFoundException(symbol, date);
        }
        return getCryptoStats(symbol, cryptoPrices);
    }

    private static CryptoStats getCryptoStats(String symbol, List<CryptoPrice> cryptoPrices) {
        BigDecimal oldest = cryptoPrices.getFirst().price();
        BigDecimal newest = cryptoPrices.getLast().price();
        BigDecimal min = cryptoPrices.stream().map(CryptoPrice::price).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = cryptoPrices.stream().map(CryptoPrice::price).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        return new CryptoStats(symbol, oldest, newest, min, max);
    }
}
