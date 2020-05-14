package com.stockhub.app.service.dto;

public class ExpirationDTO {
    private long expiration;

    private long expirationDays;

    public long getExpiration() {
        return expiration;
    }

    public ExpirationDTO expiration(long expiration) {
        this.expiration = expiration;
        return this;
    }

    public long getExpirationDays() {
        return expirationDays;
    }

    public ExpirationDTO expirationDays(long expirationDays) {
        this.expirationDays = expirationDays;
        return this;
    }

    @Override
    public String toString() {
        return "ExpirationDTO{" +
            "expiration=" + expiration +
            ", expirationDays=" + expirationDays +
            '}';
    }
}
