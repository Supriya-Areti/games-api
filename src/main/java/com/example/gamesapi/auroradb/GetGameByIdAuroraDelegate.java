package com.example.gamesapi.auroradb;

import com.example.gamesapi.model.Game;
import com.example.gamesapi.model.GameVersion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class GetGameByIdAuroraDelegate extends AbstractAuroraDelegate {

    private static final String GET_GAME_BY_ID = """
            SELECT
                g.id AS game_id,
                g.brand_id,
                g.name AS game_name,
                g.game_engine,
                gv.game_version,
                gv.version_name,
                gv.location
            FROM game g
            LEFT JOIN game_version gv ON g.id = gv.game_id
            WHERE g.id = ?
            ORDER BY gv.id
            """;

    public GetGameByIdAuroraDelegate(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Game getGameById(long gameId) {
        return jdbcTemplate.query(
                GET_GAME_BY_ID,
                (ResultSetExtractor<Game>) rs -> extractGame(rs),
                gameId
        );
    }

    private Game extractGame(ResultSet rs) throws SQLException {
        Game game = null;

        while (rs.next()) {
            if (game == null) {
                game = new Game();
                game.setId(rs.getLong("game_id"));
                game.setBrandId(rs.getLong("brand_id"));
                game.setName(rs.getString("game_name"));
                game.setEngine(rs.getString("game_engine"));
                game.setVersions(new ArrayList<>());
            }

            String version = rs.getString("game_version");

            if (version != null && !version.isBlank()) {
                GameVersion gameVersion = new GameVersion();
                gameVersion.setVersion(version);
                gameVersion.setVersionName(rs.getString("version_name"));
                gameVersion.setLocation(rs.getString("location"));
                game.getVersions().add(gameVersion);
            }
        }

        return game;
    }
}