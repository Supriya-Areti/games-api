package com.example.gamesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
public class GamesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamesApiApplication.class, args);
    }
}