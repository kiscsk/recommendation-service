package com.epam.xmtesttask.repository;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Service responsible for loading cryptocurrency price data from CSV files at application startup.
 * <p>
 * This loader scans the {@code resources/prices} directory for all files ending with {@code _values.csv},
 * parses each file, and saves the price data into the provided {@link PriceRepository}.
 * <p>
 * The expected CSV format is: {@code timestamp,symbol,price}
 * <p>
 * The {@link #loadCsvFiles()} is automatically invoked after bean construction
 * due to the {@link PostConstruct} annotation.
 */
@Service
public class CSVPriceLoader {
    @Autowired
    private PriceRepository priceRepository;

    @PostConstruct
    public void loadCsvFiles() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL priceResource = classLoader.getResource("prices");
        List<String> fileList;
        try {
            Path path = Paths.get(priceResource.toURI());
            fileList = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(filePath -> filePath.endsWith("_values.csv"))
                    .map(filePath -> filePath.substring(filePath.indexOf("prices") - 1))
                    .toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (fileList.isEmpty()) return;

        fileList.forEach(file -> {
                InputStream inputStream = classLoader.getResourceAsStream(file);
                try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                    String[] line;
                    reader.readNext();
                    while ((line = reader.readNext()) != null) {
                        LocalDateTime utcDateTime = createLocalDateTime(line[0]);
                        String symbol = line[1];
                        BigDecimal price = new BigDecimal(line[2], new MathContext(4));
                        priceRepository.savePrice(symbol, utcDateTime, price);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

    private LocalDateTime createLocalDateTime(String timestamp) {
        long longTimeStamp = Long.parseLong(timestamp);
        Instant instant = Instant.ofEpochMilli(longTimeStamp);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
