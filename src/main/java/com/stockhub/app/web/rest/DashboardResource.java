package com.stockhub.app.web.rest;

import com.stockhub.app.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/dashboard")
@Transactional
public class DashboardResource {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/byTicker")
    public ResponseEntity<?> byTicker(@RequestParam String ticker) {
        return ResponseEntity.ok(dashboardService.buildByTicker(ticker));
    }

    @GetMapping("/byExpiration")
    public ResponseEntity<?> byExpiration(@RequestParam long expiration) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(dashboardService.buildByExpiration(expiration));
    }

    @GetMapping("/expirations")
    public ResponseEntity<?> expirations() {
        return ResponseEntity.ok(dashboardService.getExpirations());
    }

}
