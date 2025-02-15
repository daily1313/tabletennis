package com.example.tabletennis.monitoring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ApplicationHealthCheckIndicator implements HealthIndicator {

    private final AtomicReference<Health> healthRefer = new AtomicReference<>(Health.up().build());

    @Override
    public Health health() {
        return healthRefer.get();
    }

    public void setHealth(Health health) {
        this.healthRefer.set(health);
    }
}

