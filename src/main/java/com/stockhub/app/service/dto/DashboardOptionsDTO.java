package com.stockhub.app.service.dto;

import java.time.LocalDate;
import java.util.List;

public class DashboardOptionsDTO {
    private LocalDate expiration;

    private long expirationDays;

    private List<OptionWithProfitDTO> options;

    private boolean show;

    private double calculatedPercentProfit;

    private String ticker;

    public LocalDate getExpiration() {
        return expiration;
    }

    public DashboardOptionsDTO expiration(LocalDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public long getExpirationDays() {
        return expirationDays;
    }

    public DashboardOptionsDTO expirationDays(long expirationDays) {
        this.expirationDays = expirationDays;
        return this;
    }

    public List<OptionWithProfitDTO> getOptions() {
        return options;
    }

    public DashboardOptionsDTO options(List<OptionWithProfitDTO> options) {
        this.options = options;
        return this;
    }

    public boolean isShow() {
        return show;
    }

    public DashboardOptionsDTO show(boolean show) {
        this.show = show;
        return this;
    }

    public double getCalculatedPercentProfit() {
        return calculatedPercentProfit;
    }

    public DashboardOptionsDTO calculatedPercentProfit(double calculatedPercentProfit) {
        this.calculatedPercentProfit = calculatedPercentProfit;
        return this;
    }

    public String getTicker() {
        return ticker;
    }

    public DashboardOptionsDTO ticker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    @Override
    public String toString() {
        return "DashboardOptionsDTO{" +
            "expiration=" + expiration +
            ", expirationDays=" + expirationDays +
            ", options=" + options +
            ", show=" + show +
            ", calculatedPercentProfit=" + calculatedPercentProfit +
            ", ticker='" + ticker + '\'' +
            '}';
    }
}
