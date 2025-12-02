package com.epam.xmtesttask.controller;

import com.epam.xmtesttask.domain.crypto.CryptoNormalizedRange;
import com.epam.xmtesttask.domain.crypto.CryptoStats;
import com.epam.xmtesttask.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for crypto recommendation endpoints.
 * <p>
 * Provides endpoints to retrieve normalized ranges, statistics, and recommendations for cryptocurrencies.
 * All endpoints are documented with OpenAPI annotations for interactive API documentation.
 */
@RestController
@RequestMapping("/cryptos")
@Tag(name = "Crypto Recommendation API", description = "Endpoints for crypto statistics and recommendations")
public class RecommendationServiceController {
    @Autowired
    private PriceService priceService;

    /**
     * Returns a descending sorted list of all cryptos, comparing the normalized range ((max-min)/min).
     *
     * @return List of {@link CryptoNormalizedRange} objects, sorted by normalized range descending.
     */
    @Operation(
            summary = "Get normalized ranges for all cryptos",
            description = "Returns a descending sorted list of all supported cryptocurrencies, comparing their normalized range ((max-min)/min) over the available period.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of normalized ranges",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CryptoNormalizedRange.class)
                            )
                    )
            }
    )
    @GetMapping("/normalized-range")
    public List<CryptoNormalizedRange> getDescNormalizedRanges() {
        return priceService.getDescNormalizedRanges();
    }

    /**
     * Returns the oldest, newest, min, and max values for a requested crypto.
     *
     * @param symbol The symbol of the cryptocurrency (e.g., BTC, ETH).
     * @return {@link CryptoStats} object containing statistics for the requested crypto.
     */
    @Operation(
            summary = "Get stats for a specific crypto",
            description = "Returns the oldest, newest, min, and max price values for the specified cryptocurrency symbol.",
            parameters = {
                    @Parameter(
                            name = "symbol",
                            description = "Cryptocurrency symbol (e.g., BTC, ETH)",
                            example = "BTC",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Crypto stats",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CryptoStats.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Crypto not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"code\": \"NOT_FOUND\", \"message\": \"Crypto not supported: BTC\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{symbol}/stats")
    public CryptoStats getStats(
            @PathVariable("symbol") String symbol) {
        return priceService.getStats(symbol);
    }

    /**
     * Returns the crypto with the highest normalized range for a specific day.
     *
     * @param date The date to check (format: yyyy-MM-dd).
     * @return {@link CryptoNormalizedRange} object for the crypto with the highest normalized range on the given day.
     */
    @Operation(
            summary = "Get crypto with highest normalized range for a day",
            description = "Returns the cryptocurrency with the highest normalized range for the specified date.",
            parameters = {
                    @Parameter(
                            name = "date",
                            description = "Date to check (format: yyyy-MM-dd)",
                            example = "2023-01-15",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Crypto with highest normalized range",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CryptoNormalizedRange.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No data for date",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"code\": \"NOT_FOUND\", \"message\": \"No data for date: 2023-01-15\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/highest-normalized-range")
    public CryptoNormalizedRange getHighestNormalizedRange(
            @RequestParam(value = "date", defaultValue = "yyyy-mm-dd") LocalDate date) {
        return priceService.getHighestNormalizedRange(date);
    }
}
