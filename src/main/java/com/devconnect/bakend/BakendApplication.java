package com.devconnect.bakend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BakendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BakendApplication.class, args);
    }

}
