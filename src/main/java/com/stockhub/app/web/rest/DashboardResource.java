package com.stockhub.app.web.rest;

import com.stockhub.app.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Transactional
public class DashboardResource {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/{ticker}")
    public ResponseEntity<?> dashboard(@PathVariable String ticker) throws IOException {
        return ResponseEntity.ok(dashboardService.getDashboard(ticker));
    }

}
