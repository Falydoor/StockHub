package com.stockhub.app.service.dto;

import java.time.LocalDate;
import java.util.List;

public class DashboardByExpirationDTO {
    private String ticker;

    private double price;

    private List<DivDTO> dividends;

    private double fiftyTwoWeekLow;

    private double fiftyTwoWeekHigh;

    private String name;

    private LocalDate expiration;

    private long expirationDays;

    private List<OptionWithProfitDTO> options;

    private boolean show;

    private double calculatedPercentProfit;

    public String getTicker() {
        return ticker;
    }

    public DashboardByExpirationDTO ticker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public DashboardByExpirationDTO price(double price) {
        this.price = price;
        return this;
    }

    public List<DivDTO> getDividends() {
        return dividends;
    }

    public DashboardByExpirationDTO dividends(List<DivDTO> dividends) {
        this.dividends = dividends;
        return this;
    }

    public double getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    public DashboardByExpirationDTO fiftyTwoWeekLow(double fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
        return this;
    }

    public double getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    public DashboardByExpirationDTO fiftyTwoWeekHigh(double fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
        return this;
    }

    public String getName() {
        return name;
    }

    public DashboardByExpirationDTO name(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public DashboardByExpirationDTO expiration(LocalDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public long getExpirationDays() {
        return expirationDays;
    }

    public DashboardByExpirationDTO expirationDays(long expirationDays) {
        this.expirationDays = expirationDays;
        return this;
    }

    public List<OptionWithProfitDTO> getOptions() {
        return options;
    }

    public DashboardByExpirationDTO options(List<OptionWithProfitDTO> options) {
        this.options = options;
        return this;
    }

    public boolean isShow() {
        return show;
    }

    public DashboardByExpirationDTO show(boolean show) {
        this.show = show;
        return this;
    }

    public double getCalculatedPercentProfit() {
        return calculatedPercentProfit;
    }

    public DashboardByExpirationDTO calculatedPercentProfit(double calculatedPercentProfit) {
        this.calculatedPercentProfit = calculatedPercentProfit;
        return this;
    }

    @Override
    public String toString() {
        return "DashboardByExpirationDTO{" +
            "ticker='" + ticker + '\'' +
            ", price=" + price +
            ", dividends=" + dividends +
            ", fiftyTwoWeekLow=" + fiftyTwoWeekLow +
            ", fiftyTwoWeekHigh=" + fiftyTwoWeekHigh +
            ", name='" + name + '\'' +
            ", expiration=" + expiration +
            ", expirationDays=" + expirationDays +
            ", options=" + options +
            ", show=" + show +
            ", calculatedPercentProfit=" + calculatedPercentProfit +
            '}';
    }
}
