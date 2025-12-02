package com.epam.xmtesttask.repository;

import com.epam.xmtesttask.domain.crypto.CryptoPrice;
import com.epam.xmtesttask.domain.exception.CryptoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryPriceRepositoryTest {

    private InMemoryPriceRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPriceRepository();
    }

    @Test
    void savePrice_and_getPrices_shouldStoreAndRetrievePrice() {
        // Given: a symbol, date, and price
        String symbol = "BTC";
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        BigDecimal price = new BigDecimal("10000");

        // When: saving the price
        repository.savePrice(symbol, dateTime, price);

        // Then: the price can be retrieved and matches the input
        List<CryptoPrice> prices = repository.getPrices(symbol);
        assertEquals(1, prices.size());
        assertEquals(dateTime, prices.get(0).utcDateTime());
        assertEquals(price, prices.get(0).price());
    }

    @Test
    void isSupported_shouldReturnTrueIfSymbolHasPrices() {
        // Given: a symbol with a saved price
        String symbol = "ETH";
        repository.savePrice(symbol, LocalDateTime.now(), new BigDecimal("2000"));

        // When: checking if the symbol is supported
        boolean supported = repository.isSupported(symbol);

        // Then: it should return true
        assertTrue(supported);
    }

    @Test
    void isSupported_shouldReturnFalseIfSymbolNotPresent() {
        // Given: a symbol that has not been added

        // When: checking if the symbol is supported
        boolean supported = repository.isSupported("DOGE");

        // Then: it should return false
        assertFalse(supported);
    }

    @Test
    void isSupported_shouldReturnFalseIfSymbolHasNoPrices() {
        // Given: a symbol that has not been added

        // When: checking if the symbol is supported
        boolean supported = repository.isSupported("LTC");

        // Then: it should return false
        assertFalse(supported);
    }

    @Test
    void getSupportedCryptos_shouldReturnAllSymbols() {
        // Given: two symbols with saved prices
        repository.savePrice("BTC", LocalDateTime.now(), new BigDecimal("10000"));
        repository.savePrice("ETH", LocalDateTime.now(), new BigDecimal("2000"));

        // When: retrieving supported cryptos
        Set<String> supported = repository.getSupportedCryptos();

        // Then: both symbols should be present
        assertTrue(supported.contains("BTC"));
        assertTrue(supported.contains("ETH"));
        assertEquals(2, supported.size());
    }

    @Test
    void getPrices_shouldThrowExceptionIfSymbolNotSupported() {
        // Given: a symbol that has not been added

        // When & Then: requesting prices should throw CryptoNotFoundException
        assertThrows(CryptoNotFoundException.class, () -> repository.getPrices("UNKNOWN"));
    }

    @Test
    void getPrices_shouldThrowExceptionIfSymbolHasNoPrices() {
        // Given: a symbol that has not been added

        // When & Then: requesting prices should throw CryptoNotFoundException
        assertThrows(CryptoNotFoundException.class, () -> repository.getPrices("LTC"));
    }
}