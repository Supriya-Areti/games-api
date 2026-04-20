package com.example.gamesapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public class AWSConfigurationProperties {

    private String region;
    private String s3Bucket;
    private String gamesCloudfront;
    private  String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getGamesCloudfront() {
        return gamesCloudfront;
    }

    public void setGamesCloudfront(String gamesCloudfront) {
        this.gamesCloudfront = gamesCloudfront;
    }
}