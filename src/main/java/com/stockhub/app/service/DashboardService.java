package com.stockhub.app.service;

import com.stockhub.app.domain.Stock;
import com.stockhub.app.repository.StockRepository;
import com.stockhub.app.service.dto.*;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.time.temporal.ChronoUnit.YEARS;

@Service
public class DashboardService {
    private final String DEFAULT_TICKER = "T";

    private final double STRIKE_PERCENT = 0.05;

    private final YahooService yahooService;

    private final StockRepository stockRepository;

    public DashboardService(YahooService yahooService, StockRepository stockRepository) {
        this.yahooService = yahooService;
        this.stockRepository = stockRepository;
    }

    public List<ExpirationDTO> getExpirations() {
        return yahooService.getExpirations(DEFAULT_TICKER)
            .mapToObj(this::getExpirationDTO)
            .sorted(Comparator.comparingLong(ExpirationDTO::getExpirationDays).reversed())
            .collect(Collectors.toList());
    }

    public DashboardByTickerDTO buildByTicker(String ticker) {
        QuoteDTO quote = yahooService.getQuote(ticker);
        List<DivDTO> lastYearDividends = yahooService.getLastYearDividends(ticker);
        List<DashboardOptionsDTO> dashboardOptionsDTO = yahooService.getExpirations(ticker)
            .mapToObj(expiration -> getOption(ticker, expiration, quote.getRegularMarketPrice(), lastYearDividends))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(DashboardOptionsDTO::getExpiration).reversed())
            .collect(Collectors.toList());

        return new DashboardByTickerDTO()
            .fiftyTwoWeekHigh(quote.getFiftyTwoWeekHigh())
            .fiftyTwoWeekLow(quote.getFiftyTwoWeekLow())
            .name(quote.getLongName())
            .price(quote.getRegularMarketPrice())
            .ticker(ticker)
            .dashboardOptions(dashboardOptionsDTO)
            .dividends(lastYearDividends);
    }

    public List<DashboardByExpirationDTO> buildByExpiration(long expiration) {
        return stockRepository.findAll()
            .parallelStream()
            .map(Stock::getTicker)
            .map(ticker -> getOption(ticker, expiration, yahooService.getQuote(ticker).getRegularMarketPrice(), yahooService.getLastYearDividends(ticker)))
            .filter(Objects::nonNull)
            .map(this::getDashboardByExpiration)
            .sorted(Comparator.comparingDouble(DashboardByExpirationDTO::getCalculatedPercentProfit).reversed())
            .collect(Collectors.toList());
    }

    private DashboardByExpirationDTO getDashboardByExpiration(DashboardOptionsDTO option) {
        QuoteDTO quote = yahooService.getQuote(option.getTicker());

        return new DashboardByExpirationDTO()
            .fiftyTwoWeekHigh(quote.getFiftyTwoWeekHigh())
            .fiftyTwoWeekLow(quote.getFiftyTwoWeekLow())
            .name(quote.getLongName())
            .price(quote.getRegularMarketPrice())
            .ticker(option.getTicker())
            .dividends(yahooService.getLastYearDividends(option.getTicker()))
            .calculatedPercentProfit(option.getCalculatedPercentProfit())
            .expiration(option.getExpiration())
            .expirationDays(option.getExpirationDays())
            .options(option.getOptions());
    }

    private ExpirationDTO getExpirationDTO(long expiration) {
        return new ExpirationDTO().expiration(expiration).expirationDays(ChronoUnit.DAYS.between(LocalDate.now(), toDate(expiration)));
    }

    private DashboardOptionsDTO getOption(String ticker, long expiration, double price, List<DivDTO> lastYearDividends) {
        LocalDate expirationDate = toDate(expiration);
        List<OptionWithProfitDTO> options = yahooService.getCalls(ticker, expiration).stream()
            .map(call -> buildDashboardOption(call, price, expirationDate, lastYearDividends))
            .collect(Collectors.toList());

        if (options.isEmpty()) {
            return null;
        }

        return new DashboardOptionsDTO()
            .ticker(ticker)
            .expiration(expirationDate)
            .expirationDays(ChronoUnit.DAYS.between(LocalDate.now(), expirationDate))
            .options(options)
            .calculatedPercentProfit(calcProfit(options));
    }

    private OptionWithProfitDTO buildDashboardOption(CallDTO call, double price, LocalDate expirationDate, List<DivDTO> lastYearDividends) {
        return new OptionWithProfitDTO()
            .strike(call.getStrike())
            .strikePrice(call.getLastPrice())
            .profitDiv(getProfitDivs(lastYearDividends, expirationDate))
            .price(price)
            .volume(call.getVolume())
            .openInterest(call.getOpenInterest());
    }

    private double getProfitDivs(List<DivDTO> lastYearDividends, LocalDate expirationDate) {
        return LongStream.range(1, YEARS.between(LocalDate.now(), expirationDate) + 2)
            .mapToDouble(years -> lastYearDividends.stream()
                .filter(div -> isValidProfit(div, years, expirationDate))
                .mapToDouble(DivDTO::getDividends)
                .sum())
            .sum();
    }

    private boolean isValidProfit(DivDTO div, long years, LocalDate expirationDate) {
        return div.getDate().plusYears(years).isAfter(LocalDate.now()) && div.getDate().plusYears(years).isBefore(expirationDate);
    }

    private double calcProfit(List<OptionWithProfitDTO> options) {
        if (options.size() < 7) {
            return 0;
        }
        options.remove(0);
        options.remove(options.size() - 1);
        WeightedObservedPoints obs = new WeightedObservedPoints();
        options.forEach(option -> obs.add(option.getVolume(), option.getStrikePercent(), option.getProfitPercent()));
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);
        fitter.withStartPoint(new double[]{options.get(0).getStrikePercent(), options.get(0).getProfitPercent()});
        double[] coeffs = fitter.fit(obs.toList());
        return coeffs[0] + coeffs[1] * STRIKE_PERCENT + coeffs[2] * Math.pow(STRIKE_PERCENT, 2);
    }

    private LocalDate toDate(long expiration) {
        return Instant.ofEpochSecond(expiration).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }
}
