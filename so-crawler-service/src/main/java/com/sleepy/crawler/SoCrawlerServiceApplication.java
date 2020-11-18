package com.sleepy.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SoCrawlerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoCrawlerServiceApplication.class, args);
    }

}
