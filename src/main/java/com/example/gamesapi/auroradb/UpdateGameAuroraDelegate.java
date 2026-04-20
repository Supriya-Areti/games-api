package com.example.gamesapi.auroradb;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateGameAuroraDelegate extends AbstractAuroraDelegate {

    private static final String UPDATE_GAME = """
            UPDATE game
            SET brand_id = ?, name = ?, game_engine = ?
            WHERE id = ?
            """;

    public UpdateGameAuroraDelegate(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public int execute(Long id, Long brandId, String name, String engine) {
        return jdbcTemplate.update(
                UPDATE_GAME,
                brandId,
                name,
                engine,
                id
        );
    }
}