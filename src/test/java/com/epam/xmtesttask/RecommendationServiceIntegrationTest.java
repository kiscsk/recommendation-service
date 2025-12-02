package com.epam.xmtesttask;

import com.epam.xmtesttask.repository.CSVPriceLoader;
import com.epam.xmtesttask.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = RecommendationServiceApplication.class)
@AutoConfigureMockMvc
public class RecommendationServiceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PriceRepository priceRepository;

    @MockitoBean
    private CSVPriceLoader csvPriceLoader;

    @BeforeEach
    void setup() {
        priceRepository.savePrice("TEST", LocalDateTime.of(2025, 12, 2, 22, 9), BigDecimal.valueOf(12.3));
    }

    @Test
    public void normalizedRange_shouldReturnStatus200()
            throws Exception {

        mvc.perform(get("/cryptos/normalized-range")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void stats_shouldReturnStatus200_whenExistingCrypto()
            throws Exception {

        mvc.perform(get("/cryptos/TEST/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void stats_shouldReturnStatus404_whenNotExistingCrypto()
            throws Exception {

        mvc.perform(get("/cryptos/NO/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void highestNormalizedRange_shouldReturnStatus200_whenHasDataForDate()
            throws Exception {

        mvc.perform(get("/cryptos/highest-normalized-range")
                        .contentType(MediaType.APPLICATION_JSON).param("date", "2025-12-02"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void highestNormalizedRange_shouldReturnStatus404_whenNoDataForDate()
            throws Exception {

        mvc.perform(get("/cryptos/highest-normalized-range")
                        .contentType(MediaType.APPLICATION_JSON).param("date", "2025-12-01"))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
