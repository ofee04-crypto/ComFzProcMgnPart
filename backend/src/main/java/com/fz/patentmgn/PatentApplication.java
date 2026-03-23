package com.fz.patentmgn;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PatentApplication {

    public static void main(String[] args) {
        // Run as a headless=false context to allow Desktop API usage for browser launch
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PatentApplication.class);
        builder.headless(false);
        builder.run(args);
    }

}
