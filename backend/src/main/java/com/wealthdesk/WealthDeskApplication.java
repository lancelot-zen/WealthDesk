package com.wealthdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WealthDeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(WealthDeskApplication.class, args);
    }
}