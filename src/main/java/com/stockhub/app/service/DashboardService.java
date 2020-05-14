package com.stockhub.app.service;

import com.stockhub.app.domain.Stock;
import com.stockhub.app.repository.StockRepository;
import com.stockhub.app.service.dto.*;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final YahooService yahooService;

    private final StockRepository stockRepository;

    public DashboardService(YahooService yahooService, StockRepository stockRepository) {
        this.yahooService = yahooService;
        this.stockRepository = stockRepository;
    }

    public List<ExpirationDTO> getExpirations() {
        return yahooService.getExpirations("T")
            .mapToObj(expiration -> new ExpirationDTO().expiration(expiration).expirationDays(ChronoUnit.DAYS.between(LocalDate.now(), toDate(expiration))))
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
            .map(option -> {
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
            })
            .sorted(Comparator.comparingDouble(DashboardByExpirationDTO::getCalculatedPercentProfit).reversed())
            .collect(Collectors.toList());
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
            .calculatedPercentProfit(calcProfit(options, 0.05));
    }

    private OptionWithProfitDTO buildDashboardOption(CallDTO call, double price, LocalDate expirationDate, List<DivDTO> lastYearDividends) {
        double profitDiv = getDiv(lastYearDividends, expirationDate);

        return new OptionWithProfitDTO()
            .strike(call.getStrike())
            .strikePercent(call.getStrike() / price - 1)
            .strikePrice(call.getLastPrice())
            .profit(call.getStrike() - price)
            .profitDiv(profitDiv)
            .price(price)
            .profitTotal(call.getLastPrice() + (call.getStrike() - price) + profitDiv)
            .profitPercent((call.getLastPrice() + call.getStrike() + profitDiv) / price - 1)
            .volume(call.getVolume())
            .openInterest(call.getOpenInterest());
    }

    private double getDiv(List<DivDTO> lastYearDividends, LocalDate expirationDate) {
        return LongStream.range(1, YEARS.between(LocalDate.now(), expirationDate) + 2)
            .mapToDouble(i -> lastYearDividends.stream()
                .filter(div -> div.getDate().plusYears(i).isAfter(LocalDate.now()) && div.getDate().plusYears(i).isBefore(expirationDate))
                .mapToDouble(DivDTO::getDividends)
                .sum())
            .sum();
    }

    private double calcProfit(List<OptionWithProfitDTO> options, double strikePercent) {
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
        return coeffs[0] + coeffs[1] * strikePercent + coeffs[2] * Math.pow(strikePercent, 2);
    }

    private double solveTwentyPercent(List<OptionWithProfitDTO> options, long expiration) {
        WeightedObservedPoints obs = new WeightedObservedPoints();
        options.forEach(option -> obs.add(option.getVolume(), option.getStrikePercent(), option.getProfitPercent()));
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);
        fitter.withStartPoint(new double[]{options.get(0).getStrikePercent(), options.get(0).getProfitPercent()});
        double[] coeffs = fitter.fit(obs.toList());
        coeffs[0] -= 0.20;
        PolynomialFunction polynomial = new PolynomialFunction(coeffs);
        LaguerreSolver laguerreSolver = new LaguerreSolver();
        try {
            return laguerreSolver.solve(100, polynomial, options.get(0).getStrikePercent(), options.get(options.size() - 1).getStrikePercent());
        } catch (Exception e) {
            log.error("Unable to solve for {}", expiration);
            return 0;
        }
    }

    private LocalDate toDate(long expiration) {
        return Instant.ofEpochSecond(expiration).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }
}
