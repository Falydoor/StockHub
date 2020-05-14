package com.stockhub.app.service.dto;

public class OptionWithProfitDTO {
    private double strike;

    private double strikePercent;

    private double strikePrice;

    private double price;

    private double profit;

    private double profitDiv;

    private double profitTotal;

    private double profitPercent;

    private int volume;

    private int openInterest;

    public double getStrike() {
        return strike;
    }

    public OptionWithProfitDTO strike(double strike) {
        this.strike = strike;
        return this;
    }

    public double getStrikePercent() {
        return strikePercent;
    }

    public OptionWithProfitDTO strikePercent(double strikePercent) {
        this.strikePercent = strikePercent;
        return this;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public OptionWithProfitDTO strikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public OptionWithProfitDTO price(double price) {
        this.price = price;
        return this;
    }

    public double getProfit() {
        return profit;
    }

    public OptionWithProfitDTO profit(double profit) {
        this.profit = profit;
        return this;
    }

    public double getProfitDiv() {
        return profitDiv;
    }

    public OptionWithProfitDTO profitDiv(double profitDiv) {
        this.profitDiv = profitDiv;
        return this;
    }

    public double getProfitTotal() {
        return profitTotal;
    }

    public OptionWithProfitDTO profitTotal(double profitTotal) {
        this.profitTotal = profitTotal;
        return this;
    }

    public double getProfitPercent() {
        return profitPercent;
    }

    public OptionWithProfitDTO profitPercent(double profitPercent) {
        this.profitPercent = profitPercent;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public OptionWithProfitDTO volume(int volume) {
        this.volume = volume;
        return this;
    }

    public int getOpenInterest() {
        return openInterest;
    }

    public OptionWithProfitDTO openInterest(int openInterest) {
        this.openInterest = openInterest;
        return this;
    }

    @Override
    public String toString() {
        return "OptionWithProfitDTO{" +
            "strike=" + strike +
            ", strikePercent=" + strikePercent +
            ", strikePrice=" + strikePrice +
            ", price=" + price +
            ", profit=" + profit +
            ", profitDiv=" + profitDiv +
            ", profitTotal=" + profitTotal +
            ", profitPercent=" + profitPercent +
            ", volume=" + volume +
            ", openInterest=" + openInterest +
            '}';
    }
}
