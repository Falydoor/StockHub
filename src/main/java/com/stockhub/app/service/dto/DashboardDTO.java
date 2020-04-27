package com.stockhub.app.service.dto;

import java.util.List;

public class DashboardDTO {
    private String ticker;

    private double price;

    private List<DivDTO> dividends;

    private List<DashboardOptionsDTO> dashboardOptions;

    private double fiftyTwoWeekLow;

    private double fiftyTwoWeekHigh;

    private String name;

    public String getTicker() {
        return ticker;
    }

    public DashboardDTO ticker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public DashboardDTO price(double price) {
        this.price = price;
        return this;
    }

    public List<DivDTO> getDividends() {
        return dividends;
    }

    public DashboardDTO dividends(List<DivDTO> dividends) {
        this.dividends = dividends;
        return this;
    }

    public List<DashboardOptionsDTO> getDashboardOptions() {
        return dashboardOptions;
    }

    public DashboardDTO dashboardOptions(List<DashboardOptionsDTO> dashboardOptions) {
        this.dashboardOptions = dashboardOptions;
        return this;
    }

    public double getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    public DashboardDTO fiftyTwoWeekLow(double fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
        return this;
    }

    public double getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    public DashboardDTO fiftyTwoWeekHigh(double fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
        return this;
    }

    public String getName() {
        return name;
    }

    public DashboardDTO name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "DashboardDTO{" +
            "ticker='" + ticker + '\'' +
            ", price=" + price +
            ", dividends=" + dividends +
            ", dashboardOptions=" + dashboardOptions +
            ", fiftyTwoWeekLow=" + fiftyTwoWeekLow +
            ", fiftyTwoWeekHigh=" + fiftyTwoWeekHigh +
            ", name='" + name + '\'' +
            '}';
    }
}
