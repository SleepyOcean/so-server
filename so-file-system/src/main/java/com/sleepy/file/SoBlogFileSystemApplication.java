package com.sleepy.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sleepy.**"})
public class SoBlogFileSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoBlogFileSystemApplication.class, args);
    }

}
