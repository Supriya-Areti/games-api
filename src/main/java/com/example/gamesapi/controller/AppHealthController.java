package com.example.gamesapi.controller;

import com.example.gamesapi.config.AWSConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AppHealthController {

    private final AWSConfigurationProperties awsConfigurationProperties;

    public AppHealthController(AWSConfigurationProperties awsConfigurationProperties) {
        this.awsConfigurationProperties = awsConfigurationProperties;
    }

    @GetMapping("/health/simple")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("application", "games-api");
        response.put("region", awsConfigurationProperties.getRegion());
        response.put("port", awsConfigurationProperties.getPort());
        return response;
    }
}