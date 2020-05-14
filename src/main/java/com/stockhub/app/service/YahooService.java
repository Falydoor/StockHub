package com.stockhub.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockhub.app.service.dto.CallDTO;
import com.stockhub.app.service.dto.DivDTO;
import com.stockhub.app.service.dto.QuoteDTO;
import com.stockhub.app.web.rest.errors.DashboardException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@Service
public class YahooService {
    private final Logger log = LoggerFactory.getLogger(YahooService.class);

    private final String URL = "https://query1.finance.yahoo.com/v7/finance";

    private final ObjectMapper objectMapper;

    private final CsvMapper csvMapper;

    public YahooService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.csvMapper = new CsvMapper();
        this.csvMapper.registerModule(new JavaTimeModule());
    }

    public LongStream getExpirations(String ticker) {
        try {
            return StreamSupport.stream(getResult(ticker).get("expirationDates").spliterator(), false)
                .mapToLong(JsonNode::asLong);
        } catch (IOException e) {
            log.error("Error while getting expirations", e);
            return LongStream.empty();
        }
    }

    public List<CallDTO> getCalls(String ticker, long expiration) {
        try {
            JsonNode options = getResult(ticker, expiration).get("options");
            if (!options.isEmpty()) {
                return objectMapper.readerFor(new TypeReference<List<CallDTO>>() {
                }).readValue(options.get(0).get("calls"));
            }
        } catch (IOException e) {
            log.error("Error while getting calls", e);
        }
        return Collections.emptyList();
    }

    public QuoteDTO getQuote(String ticker) {
        try {
            return objectMapper.treeToValue(getResult(ticker).get("quote"), QuoteDTO.class);
        } catch (IOException e) {
            log.error("Error while getting quote", e);
            throw new DashboardException("Unable to get quote", "", "");
        }
    }

    public List<DivDTO> getLastYearDividends(String ticker) {
        try {
            long startDate = LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.UTC);
            long endDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            URL url = new URL(URL + "/download/" + ticker + "?period1=" + startDate + "&period2=" + endDate + "&interval=1d&events=div");
            log.debug("DIVIDENDS URL => {}", url);
            CsvSchema bootstrapSchema = CsvSchema.builder().addColumn("Date").addNumberColumn("Dividends").build().withColumnSeparator(',').withHeader();
            MappingIterator<DivDTO> dividends = csvMapper.readerFor(DivDTO.class).with(bootstrapSchema).readValues(url);
            return dividends.readAll();
        } catch (IOException e) {
            log.error("Error while getting dividends", e);
            return Collections.emptyList();
        }
    }

    private JsonNode getResult(String ticker) throws IOException {
        return getResult(ticker, 0);
    }

    private JsonNode getResult(String ticker, long expiration) throws IOException {
        String url = URL + "/options/" + ticker;
        if (expiration > 0) {
            url += "?date=" + expiration;
        }
        log.debug("OPTIONS URL : {}", url);
        return objectMapper.readTree(new URL(url)).get("optionChain").get("result").get(0);
    }
}
