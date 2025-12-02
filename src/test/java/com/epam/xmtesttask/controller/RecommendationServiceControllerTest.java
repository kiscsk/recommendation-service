package com.epam.xmtesttask.controller;

import com.epam.xmtesttask.domain.crypto.CryptoNormalizedRange;
import com.epam.xmtesttask.domain.crypto.CryptoStats;
import com.epam.xmtesttask.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceControllerTest {

    @Mock
    private PriceService priceService;

    @InjectMocks
    private RecommendationServiceController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDescNormalizedRanges_returnsList() {
        // Arrange
        List<CryptoNormalizedRange> expected = List.of(
                new CryptoNormalizedRange("BTC", new BigDecimal("0.5")),
                new CryptoNormalizedRange("ETH", new BigDecimal("0.3"))
        );
        when(priceService.getDescNormalizedRanges()).thenReturn(expected);

        // Act
        List<CryptoNormalizedRange> result = controller.getDescNormalizedRanges();

        // Assert
        assertEquals(expected, result);
        verify(priceService, times(1)).getDescNormalizedRanges();
    }

    @Test
    void getStats_returnsStats() {
        // Arrange
        String symbol = "BTC";
        CryptoStats expected = new CryptoStats(
                symbol,
                new BigDecimal("100"),
                new BigDecimal("200"),
                new BigDecimal("90"),
                new BigDecimal("210")
        );
        when(priceService.getStats(symbol)).thenReturn(expected);

        // Act
        CryptoStats result = controller.getStats(symbol);

        // Assert
        assertEquals(expected, result);
        verify(priceService, times(1)).getStats(symbol);
    }

    @Test
    void getHighestNormalizedRange_returnsRange() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 1, 15);
        CryptoNormalizedRange expected = new CryptoNormalizedRange("BTC", new BigDecimal("0.7"));
        when(priceService.getHighestNormalizedRange(date)).thenReturn(expected);

        // Act
        CryptoNormalizedRange result = controller.getHighestNormalizedRange(date);

        // Assert
        assertEquals(expected, result);
        verify(priceService, times(1)).getHighestNormalizedRange(date);
    }
}