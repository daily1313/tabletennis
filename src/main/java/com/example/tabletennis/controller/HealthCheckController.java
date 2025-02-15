package com.example.tabletennis.controller;


import com.example.tabletennis.common.dto.ApiResponse;
import com.example.tabletennis.monitoring.health.ApplicationHealthCheckIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

@RequestMapping("/health")
@RestController
public class HealthCheckController {

    private final ApplicationHealthCheckIndicator applicationHealthCheckIndicator;
    private final AtomicBoolean isExecuted = new AtomicBoolean(false);

    public HealthCheckController(ApplicationHealthCheckIndicator applicationHealthCheckIndicator) {
        this.applicationHealthCheckIndicator = applicationHealthCheckIndicator;
    }

    @GetMapping
    public ApiResponse<Void> getServerStatus() {
        if (isExecuted.compareAndSet(false, true)) {
            boolean isApplicationHealthy = isStatusUp(applicationHealthCheckIndicator.health());
            if (isApplicationHealthy) {
                return ApiResponse.success();
            }
        }

        return ApiResponse.error();
    }

    private boolean isStatusUp(Health health) {
        return health.getStatus().getCode().equals("UP");
    }
}
