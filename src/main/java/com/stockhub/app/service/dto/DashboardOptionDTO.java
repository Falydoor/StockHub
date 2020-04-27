package com.stockhub.app.service.dto;

public class DashboardOptionDTO {
    private double strike;

    private double strikePercent;

    private double strikePrice;

    private double price;

    private double profit;

    private double profitDiv;

    private double profitTotal;

    private double profitPercent;

    private int volume;

    public double getStrike() {
        return strike;
    }

    public DashboardOptionDTO strike(double strike) {
        this.strike = strike;
        return this;
    }

    public double getStrikePercent() {
        return strikePercent;
    }

    public DashboardOptionDTO strikePercent(double strikePercent) {
        this.strikePercent = strikePercent;
        return this;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public DashboardOptionDTO strikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public DashboardOptionDTO price(double price) {
        this.price = price;
        return this;
    }

    public double getProfit() {
        return profit;
    }

    public DashboardOptionDTO profit(double profit) {
        this.profit = profit;
        return this;
    }

    public double getProfitDiv() {
        return profitDiv;
    }

    public DashboardOptionDTO profitDiv(double profitDiv) {
        this.profitDiv = profitDiv;
        return this;
    }

    public double getProfitTotal() {
        return profitTotal;
    }

    public DashboardOptionDTO profitTotal(double profitTotal) {
        this.profitTotal = profitTotal;
        return this;
    }

    public double getProfitPercent() {
        return profitPercent;
    }

    public DashboardOptionDTO profitPercent(double profitPercent) {
        this.profitPercent = profitPercent;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public DashboardOptionDTO volume(int volume) {
        this.volume = volume;
        return this;
    }

    @Override
    public String toString() {
        return "DashboardOptionDTO{" +
            "strike='" + strike + '\'' +
            ", strikePercent='" + strikePercent + '\'' +
            ", strikePrice='" + strikePrice + '\'' +
            ", price='" + price + '\'' +
            ", profit='" + profit + '\'' +
            ", profitDiv='" + profitDiv + '\'' +
            ", profitTotal='" + profitTotal + '\'' +
            ", profitPercent='" + profitPercent + '\'' +
            ", volume=" + volume +
            '}';
    }
}
