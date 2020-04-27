package com.stockhub.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.util.concurrent.AtomicDouble;
import com.stockhub.app.service.dto.DashboardDTO;
import com.stockhub.app.service.dto.DashboardOptionDTO;
import com.stockhub.app.service.dto.DashboardOptionsDTO;
import com.stockhub.app.service.dto.DivDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DashboardService {

    private final ObjectMapper objectMapper;

    public DashboardService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DashboardDTO getDashboard(String ticker) throws IOException {
        return new DashboardBuilder(ticker, objectMapper).buildDashboard();
    }

    static class DashboardBuilder {
        private final Logger log = LoggerFactory.getLogger(DashboardBuilder.class);

        private final String ticker;

        private final ObjectMapper objectMapper;

        private List<DivDTO> dividends;

        private double price;

        private LocalDate expirationDate;

        public DashboardBuilder(String ticker, ObjectMapper objectMapper) {
            this.ticker = ticker;
            this.objectMapper = objectMapper;
        }

        public DashboardDTO buildDashboard() throws IOException {
            dividends = getFullYearDiv();
            JsonNode result = objectMapper.readTree(new URL("https://query1.finance.yahoo.com/v7/finance/options/" + ticker))
                .get("optionChain").get("result").get(0);
            JsonNode quote = result.get("quote");
            price = quote.get("regularMarketPrice").asDouble();

            return new DashboardDTO()
                .fiftyTwoWeekHigh(quote.get("fiftyTwoWeekHigh").asDouble())
                .fiftyTwoWeekLow(quote.get("fiftyTwoWeekLow").asDouble())
                .dividends(dividends)
                .ticker(ticker)
                .dashboardOptions(getOptions(result))
                .price(price)
                .name(quote.get("longName").asText());
        }

        private List<DashboardOptionsDTO> getOptions(JsonNode result) {
            return StreamSupport.stream(result.get("expirationDates").spliterator(), false)
                .map(JsonNode::asInt)
                .map(this::getOption)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(DashboardOptionsDTO::getExpiration).reversed())
                .collect(Collectors.toList());
        }

        public String getTicker() {
            return ticker;
        }

        private List<DivDTO> getFullYearDiv() throws IOException {
            int previousYear = LocalDate.now().getYear() - 1;
            long startDate = LocalDateTime.of(previousYear, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
            long endDate = LocalDateTime.of(previousYear, 12, 31, 11, 59).toEpochSecond(ZoneOffset.UTC);
            URL url = new URL("https://query1.finance.yahoo.com/v7/finance/download/" + ticker + "?period1=" + startDate + "&period2=" + endDate + "&interval=1d&events=div");
            log.debug("DIV URL => {}", url);
            CsvSchema bootstrapSchema = CsvSchema.builder().addColumn("Date").addNumberColumn("Dividends").build().withColumnSeparator(',').withHeader();
            CsvMapper mapper = new CsvMapper();
            mapper.registerModule(new JavaTimeModule());
            MappingIterator<DivDTO> values = mapper.readerFor(DivDTO.class).with(bootstrapSchema).readValues(url);
            return values.readAll();
        }

        private DashboardOptionsDTO getOption(int expiration) {
            try {
                expirationDate = Instant.ofEpochSecond(expiration).atZone(ZoneId.systemDefault()).toLocalDate();
                Spliterator<JsonNode> spliterator = objectMapper.readTree(new URL("https://query1.finance.yahoo.com/v7/finance/options/" + ticker + "?date=" + expiration))
                    .get("optionChain").get("result").get(0).get("options").get(0).get("calls").spliterator();
                List<DashboardOptionDTO> options = StreamSupport.stream(spliterator, false)
                    .filter(this::isOTMAndRealistic)
                    .map(this::buildDashboardOption)
                    .collect(Collectors.toList());

                return new DashboardOptionsDTO()
                    .expiration(expirationDate)
                    .expirationDays(ChronoUnit.DAYS.between(LocalDate.now(), expirationDate))
                    .options(options);
            } catch (IOException e) {
                log.error("Error getting option");
                return null;
            }
        }

        private DashboardOptionDTO buildDashboardOption(JsonNode option) {
            AtomicDouble profitDiv = new AtomicDouble();
            AtomicInteger yearDelta = new AtomicInteger();
            while (yearDelta.getAndIncrement() <= (expirationDate.getYear() - LocalDate.now().getYear())) {
                dividends.stream()
                    .filter(div -> LocalDate.now().isBefore(div.getDate().plusYears(yearDelta.get())) && div.getDate().plusYears(yearDelta.get()).isBefore(expirationDate))
                    .forEach(div -> profitDiv.addAndGet(div.getDividends()));
            }

            return new DashboardOptionDTO()
                .strike(option.get("strike").asDouble())
                .strikePercent(option.get("strike").asDouble() / price - 1)
                .strikePrice(option.get("lastPrice").asDouble())
                .profit(option.get("strike").asDouble() - price)
                .profitDiv(profitDiv.get())
                .price(price)
                .profitTotal(option.get("lastPrice").asDouble() + (option.get("strike").asDouble() - price) + profitDiv.get())
                .profitPercent((option.get("lastPrice").asDouble() + option.get("strike").asDouble() + profitDiv.get()) / price - 1)
                .volume(option.path("volume").asInt());
        }

        private boolean isOTMAndRealistic(JsonNode option) {
            return option.get("strike").asDouble() > price && option.get("strike").asDouble() / price < 1.5;
        }
    }

}
