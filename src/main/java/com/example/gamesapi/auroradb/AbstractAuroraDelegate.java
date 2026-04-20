package com.example.gamesapi.auroradb;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractAuroraDelegate {

    protected final JdbcTemplate jdbcTemplate;

    protected AbstractAuroraDelegate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}