package com.coolpeace.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.coolpeace.core")
@EnableJpaRepositories(basePackages = "com.coolpeace.core")
@SpringBootApplication(scanBasePackages = {"com.coolpeace.core", "com.coolpeace.api"})
public class CoolPeaceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoolPeaceApiApplication.class, args);
    }

}
