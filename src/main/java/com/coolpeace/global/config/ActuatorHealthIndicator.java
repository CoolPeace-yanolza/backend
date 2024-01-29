package com.coolpeace.global.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class ActuatorHealthIndicator implements HealthIndicator {

    private static final String LOCAL_DATE_TIME_KOR_FORMAT = "yyyy년 MM월 dd일 HH시 mm분 ss초";

    private final LocalDateTime serverStartLocalDateTime = LocalDateTime.now();

    @Override
    public Health getHealth(boolean includeDetails) {
        long millis = serverStartLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long uptime = (System.currentTimeMillis() - millis) / 1000;
        return Health.up()
                .withDetail("startDate", serverStartLocalDateTime.format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_KOR_FORMAT)))
                .withDetail("uptime", uptime)
                .build();
    }

    @Override
    public Health health() {
        return getHealth(true);
    }
}
