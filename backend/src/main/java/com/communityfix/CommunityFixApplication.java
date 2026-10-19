package com.communityfix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommunityFixApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityFixApplication.class, args);
    }
}
