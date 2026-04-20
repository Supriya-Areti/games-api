package com.example.gamesapi.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Long id;
    private Long brandId;
    private String name;
    private String engine;
    private List<GameVersion> versions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public List<GameVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<GameVersion> versions) {
        this.versions = versions;
    }
}
