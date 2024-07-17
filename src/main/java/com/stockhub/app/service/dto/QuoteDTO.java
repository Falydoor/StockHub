package com.stockhub.app.service.dto;

public class QuoteDTO {
    private double regularMarketPrice;

    private double regularMarketChangePercent;

    private double fiftyTwoWeekHigh;

    private double fiftyTwoWeekLow;

    private String longName;

    public double getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public QuoteDTO regularMarketPrice(double regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
        return this;
    }

    public double getRegularMarketChangePercent() {
        return regularMarketChangePercent;
    }

    public QuoteDTO regularMarketChangePercent(double regularMarketChangePercent) {
        this.regularMarketChangePercent = regularMarketChangePercent;
        return this;
    }

    public double getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    public QuoteDTO fiftyTwoWeekHigh(double fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
        return this;
    }

    public double getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    public QuoteDTO fiftyTwoWeekLow(double fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
        return this;
    }

    public String getLongName() {
        return longName;
    }

    public QuoteDTO longName(String longName) {
        this.longName = longName;
        return this;
    }

    @Override
    public String toString() {
        return "QuoteDTO{" +
            "regularMarketPrice=" + regularMarketPrice +
            ", regularMarketChangePercent=" + regularMarketChangePercent +
            ", fiftyTwoWeekHigh=" + fiftyTwoWeekHigh +
            ", fiftyTwoWeekLow=" + fiftyTwoWeekLow +
            ", longName='" + longName + '\'' +
            '}';
    }
}
