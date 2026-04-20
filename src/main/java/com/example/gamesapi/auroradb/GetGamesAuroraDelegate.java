package com.example.gamesapi.auroradb;

import com.example.gamesapi.model.Game;
import com.example.gamesapi.model.GameVersion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GetGamesAuroraDelegate extends AbstractAuroraDelegate {

    private static final String GET_GAMES_BY_BRAND = """
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
            WHERE g.brand_id = ?
            ORDER BY g.id, gv.id
            """;

    public GetGamesAuroraDelegate(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Collection<Game> getGamesByBrand(long brandId) {
        return jdbcTemplate.query(
                GET_GAMES_BY_BRAND,
                (ResultSetExtractor<Collection<Game>>) this::extractGames,
                brandId
        );
    }

    private Collection<Game> extractGames(ResultSet rs) throws SQLException {
        Map<Long, Game> gamesMap = new LinkedHashMap<>();

        while (rs.next()) {
            long gameId = rs.getLong("game_id");

            Game game = gamesMap.get(gameId);

            if (game == null) {
                game = new Game();
                game.setId(gameId);
                game.setBrandId(rs.getLong("brand_id"));
                game.setName(rs.getString("game_name"));
                game.setEngine(rs.getString("game_engine"));
                game.setVersions(new ArrayList<>());
                gamesMap.put(gameId, game);
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

        return gamesMap.values();
    }
}