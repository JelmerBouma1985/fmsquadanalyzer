package com.github.jelmerbouma85.fm24analyzer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Fm24analyzerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Fm24analyzerApplication.class)
                .run(args);
    }
}
