package com.example.gamesapi.auroradb;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class AddGameAuroraDelegate extends AbstractAuroraDelegate {

    private static final String INSERT_GAME = """
            INSERT INTO game (brand_id, name, game_engine)
            VALUES (?, ?, ?)
            """;

    public AddGameAuroraDelegate(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Long execute(Long brandId, String name, String engine) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_GAME, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, brandId);
            ps.setString(2, name);
            ps.setString(3, engine);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }
}