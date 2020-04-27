package com.stockhub.app.service.dto;

import java.time.LocalDate;
import java.util.List;

public class DashboardOptionsDTO {
    private LocalDate expiration;

    private long expirationDays;

    private List<DashboardOptionDTO> options;

    private boolean show;

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

    public List<DashboardOptionDTO> getOptions() {
        return options;
    }

    public DashboardOptionsDTO options(List<DashboardOptionDTO> options) {
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

    @Override
    public String toString() {
        return "DashboardOptionsDTO{" +
            "expiration=" + expiration +
            ", expirationDays=" + expirationDays +
            ", options=" + options +
            ", show=" + show +
            '}';
    }
}
