package com.stockhub.app.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DivDTO {
    @JsonProperty("Date")
    private LocalDate date;

    @JsonProperty("Dividends")
    private double dividends;

    public LocalDate getDate() {
        return date;
    }

    public DivDTO date(LocalDate date) {
        this.date = date;
        return this;
    }

    public double getDividends() {
        return dividends;
    }

    public DivDTO dividends(double dividends) {
        this.dividends = dividends;
        return this;
    }

    @Override
    public String toString() {
        return "DivDTO{" +
            "date='" + date + '\'' +
            ", dividends=" + dividends +
            '}';
    }
}
