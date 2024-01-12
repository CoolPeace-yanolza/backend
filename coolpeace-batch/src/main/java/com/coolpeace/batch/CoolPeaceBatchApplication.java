package com.coolpeace.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EntityScan(basePackages = "com.coolpeace.core")
@EnableJpaRepositories(basePackages = "com.coolpeace.core")
@SpringBootApplication(scanBasePackages = {"com.coolpeace.core", "com.coolpeace.batch"})
public class CoolPeaceBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoolPeaceBatchApplication.class, args);
    }
}
