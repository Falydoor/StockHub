package com.stockhub.app.service.dto;

public class CallDTO {
    private double strike;

    private double lastPrice;

    private int volume;

    private boolean inTheMoney;

    private int openInterest;

    public double getStrike() {
        return strike;
    }

    public CallDTO strike(double strike) {
        this.strike = strike;
        return this;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public CallDTO lastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public CallDTO volume(int volume) {
        this.volume = volume;
        return this;
    }

    public boolean isInTheMoney() {
        return inTheMoney;
    }

    public CallDTO inTheMoney(boolean inTheMoney) {
        this.inTheMoney = inTheMoney;
        return this;
    }

    public int getOpenInterest() {
        return openInterest;
    }

    public CallDTO openInterest(int openInterest) {
        this.openInterest = openInterest;
        return this;
    }

    @Override
    public String toString() {
        return "CallDTO{" +
            "strike=" + strike +
            ", lastPrice=" + lastPrice +
            ", volume=" + volume +
            ", inTheMoney=" + inTheMoney +
            ", openInterest=" + openInterest +
            '}';
    }
}
