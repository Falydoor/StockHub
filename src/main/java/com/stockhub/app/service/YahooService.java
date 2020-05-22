package com.stockhub.app.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockhub.app.service.dto.CallDTO;
import com.stockhub.app.service.dto.DivDTO;
import com.stockhub.app.service.dto.QuoteDTO;
import com.stockhub.app.service.dto.YahooOptionsDTO;
import com.stockhub.app.web.rest.errors.DashboardException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.LongStream;

@Service
public class YahooService {
    private final Logger log = LoggerFactory.getLogger(YahooService.class);

    private final String URL = "https://query1.finance.yahoo.com/v7/finance";

    private final CsvSchema DIV_SCHEMA = CsvSchema.builder().addColumn("Date").addNumberColumn("Dividends").build().withColumnSeparator(',').withHeader();

    private final ObjectMapper objectMapper;

    private final CsvMapper csvMapper;

    public YahooService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.csvMapper = new CsvMapper();
        this.csvMapper.registerModule(new JavaTimeModule());
    }

    public LongStream getExpirations(String ticker) {
        try {
            return LongStream.of(getResult(ticker).getExpirationDates());
        } catch (IOException e) {
            log.error("Error while getting expirations", e);
            throw new DashboardException("Unable to get expirations");
        }
    }

    public List<CallDTO> getCalls(String ticker, long expiration) {
        try {
            return getResult(ticker, expiration).getOptions().get(0).getCalls();
        } catch (IOException e) {
            log.error("Error while getting calls", e);
            throw new DashboardException("Unable to get calls");
        }
    }

    public QuoteDTO getQuote(String ticker) {
        try {
            return getResult(ticker).getQuote();
        } catch (IOException e) {
            log.error("Error while getting quote", e);
            throw new DashboardException("Unable to get quote");
        }
    }

    public List<DivDTO> getLastYearDividends(String ticker) {
        try {
            long startDate = LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.UTC);
            long endDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            return getDividends(ticker, startDate, endDate).readAll();
        } catch (IOException e) {
            log.error("Error while getting last year dividends", e);
            throw new DashboardException("Unable to get last year dividends");
        }
    }

    private MappingIterator<DivDTO> getDividends(String ticker, long startDate, long endDate) {
        try {
            URL url = new URL(URL + "/download/" + ticker + "?period1=" + startDate + "&period2=" + endDate + "&interval=1d&events=div");
            log.debug("DIVIDENDS URL => {}", url);
            return csvMapper.readerFor(DivDTO.class).with(DIV_SCHEMA).readValues(url);
        } catch (IOException e) {
            log.error("Error while getting dividends", e);
            return MappingIterator.emptyIterator();
        }
    }

    private YahooOptionsDTO.OptionChainDTO.ResultDTO getResult(String ticker) throws IOException {
        return getResult(ticker, 0);
    }

    private YahooOptionsDTO.OptionChainDTO.ResultDTO getResult(String ticker, long expiration) throws IOException {
        String url = URL + "/options/" + ticker;
        if (expiration > 0) {
            url += "?date=" + expiration;
        }
        log.debug("OPTIONS URL : {}", url);
        return objectMapper.readValue(new URL(url), YahooOptionsDTO.class).getOptionChain().getResult().get(0);
    }
}
