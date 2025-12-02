package com.epam.xmtesttask.service;

import com.epam.xmtesttask.domain.crypto.CryptoNormalizedRange;
import com.epam.xmtesttask.domain.crypto.CryptoPrice;
import com.epam.xmtesttask.domain.crypto.CryptoStats;
import com.epam.xmtesttask.domain.exception.CryptoDataNotFoundException;
import com.epam.xmtesttask.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStats_shouldReturnCryptoStats() {
        // Given
        String symbol = "BTC";
        List<CryptoPrice> prices = List.of(
                new CryptoPrice(LocalDateTime.of(2023, 1, 1, 0, 0), new BigDecimal("100")),
                new CryptoPrice(LocalDateTime.of(2023, 1, 2, 0, 0), new BigDecimal("200")),
                new CryptoPrice(LocalDateTime.of(2023, 1, 3, 0, 0), new BigDecimal("150"))
        );
        when(priceRepository.getPrices(symbol)).thenReturn(prices);

        // When
        CryptoStats stats = priceService.getStats(symbol);

        // Then
        assertEquals(symbol, stats.symbol());
        assertEquals(new BigDecimal("100"), stats.oldest());
        assertEquals(new BigDecimal("150"), stats.newest());
        assertEquals(new BigDecimal("100"), stats.min());
        assertEquals(new BigDecimal("200"), stats.max());
    }

    @Test
    void getDescNormalizedRanges_shouldReturnSortedList() {
        // Given
        Set<String> symbols = Set.of("BTC", "ETH");
        when(priceRepository.getSupportedCryptos()).thenReturn(symbols);

        List<CryptoPrice> btcPrices = List.of(
                new CryptoPrice(LocalDateTime.now(), new BigDecimal("100")),
                new CryptoPrice(LocalDateTime.now(), new BigDecimal("150"))
        );
        List<CryptoPrice> ethPrices = List.of(
                new CryptoPrice(LocalDateTime.now(), new BigDecimal("50")),
                new CryptoPrice(LocalDateTime.now(), new BigDecimal("80"))
        );
        when(priceRepository.getPrices("BTC")).thenReturn(btcPrices);
        when(priceRepository.getPrices("ETH")).thenReturn(ethPrices);

        // When
        List<CryptoNormalizedRange> result = priceService.getDescNormalizedRanges();

        // Then
        assertEquals(2, result.size());
        assertEquals("ETH", result.get(0).symbol()); // BTC: (150-100)/100 = 0.5, ETH: (80-50)/50 = 0.6
        assertEquals("BTC", result.get(1).symbol());
        assertEquals(new BigDecimal("0.6"), result.get(0).normalizedRange());
        assertEquals(new BigDecimal("0.5"), result.get(1).normalizedRange());
    }

    @Test
    void getHighestNormalizedRange_shouldReturnMax() {
        // Given
        Set<String> symbols = Set.of("BTC", "ETH");
        LocalDate date = LocalDate.of(2023, 1, 1);
        when(priceRepository.getSupportedCryptos()).thenReturn(symbols);

        List<CryptoPrice> btcPrices = List.of(
                new CryptoPrice(date.atStartOfDay(), new BigDecimal("100")),
                new CryptoPrice(date.atTime(12, 0), new BigDecimal("150"))
        );
        List<CryptoPrice> ethPrices = List.of(
                new CryptoPrice(date.atStartOfDay(), new BigDecimal("50")),
                new CryptoPrice(date.atTime(12, 0), new BigDecimal("60"))
        );
        when(priceRepository.getPrices("BTC")).thenReturn(btcPrices);
        when(priceRepository.getPrices("ETH")).thenReturn(ethPrices);

        // When
        CryptoNormalizedRange result = priceService.getHighestNormalizedRange(date);

        // Then
        assertEquals("BTC", result.symbol());
        assertEquals(new BigDecimal("0.5"), result.normalizedRange());
    }

    @Test
    void getHighestNormalizedRange_shouldThrowIfNoData() {
        // Given
        Set<String> symbols = Set.of("BTC");
        LocalDate date = LocalDate.of(2023, 1, 1);
        when(priceRepository.getSupportedCryptos()).thenReturn(symbols);
        when(priceRepository.getPrices("BTC")).thenReturn(List.of());

        // When & Then
        assertThrows(CryptoDataNotFoundException.class, () -> priceService.getHighestNormalizedRange(date));
    }

    @Test
    void getNormalizedRange_shouldCalculateCorrectly() {
        // Given
        CryptoStats stats = new CryptoStats("BTC",
                new BigDecimal("100"),
                new BigDecimal("180"),
                new BigDecimal("100"),
                new BigDecimal("150"));

        // When
        CryptoNormalizedRange range = priceService.getNormalizedRange(stats);

        // Then
        assertEquals("BTC", range.symbol());
        assertEquals(new BigDecimal("0.5"), range.normalizedRange());
    }
}