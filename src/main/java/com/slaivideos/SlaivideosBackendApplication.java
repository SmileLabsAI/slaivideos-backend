package com.slaivideos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan("com.slaivideos")
public class SlaivideosBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SlaivideosBackendApplication.class, args);
    }
}