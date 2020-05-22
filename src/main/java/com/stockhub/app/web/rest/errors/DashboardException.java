package com.stockhub.app.web.rest.errors;

public class DashboardException extends BadRequestAlertException {
    public DashboardException(String defaultMessage) {
        super(defaultMessage, null, null);
    }

    public DashboardException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }
}
