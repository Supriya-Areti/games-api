package com.example.gamesapi.service;

import com.example.gamesapi.auroradb.AddGameAuroraDelegate;
import com.example.gamesapi.auroradb.GetGameByIdAuroraDelegate;
import com.example.gamesapi.auroradb.GetGamesAuroraDelegate;
import com.example.gamesapi.auroradb.UpdateGameAuroraDelegate;
import com.example.gamesapi.config.AWSConfigurationProperties;
import com.example.gamesapi.dto.SaveGameRequest;
import com.example.gamesapi.exception.ResourceNotFoundException;
import com.example.gamesapi.model.Game;
import com.example.gamesapi.model.GameVersion;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class GamesService {

    private final GetGamesAuroraDelegate getGamesAuroraDelegate;
    private final GetGameByIdAuroraDelegate getGameByIdAuroraDelegate;
    private final AddGameAuroraDelegate addGameAuroraDelegate;
    private final UpdateGameAuroraDelegate updateGameAuroraDelegate;
    private final AWSConfigurationProperties awsConfigurationProperties;

    public GamesService(GetGamesAuroraDelegate getGamesAuroraDelegate,
                        GetGameByIdAuroraDelegate getGameByIdAuroraDelegate,
                        AddGameAuroraDelegate addGameAuroraDelegate,
                        UpdateGameAuroraDelegate updateGameAuroraDelegate,
                        AWSConfigurationProperties awsConfigurationProperties) {
        this.getGamesAuroraDelegate = getGamesAuroraDelegate;
        this.getGameByIdAuroraDelegate = getGameByIdAuroraDelegate;
        this.addGameAuroraDelegate = addGameAuroraDelegate;
        this.updateGameAuroraDelegate = updateGameAuroraDelegate;
        this.awsConfigurationProperties = awsConfigurationProperties;
    }

    public Collection<Game> getGamesByBrand(long brandId) {
        Collection<Game> games = getGamesAuroraDelegate.getGamesByBrand(brandId);
        decorateLocations(games);
        return games;
    }

    public Game saveGame(SaveGameRequest request) {
        Long gameId;

        if (request.getId() == null) {
            gameId = addGameAuroraDelegate.execute(
                    request.getBrandId(),
                    request.getName(),
                    request.getEngine()
            );
        } else {
            int updatedRows = updateGameAuroraDelegate.execute(
                    request.getId(),
                    request.getBrandId(),
                    request.getName(),
                    request.getEngine()
            );

            if (updatedRows == 0) {
                return null;
            }

            gameId = request.getId();
        }

        Game game = getGameByIdAuroraDelegate.getGameById(gameId);

        if (game != null) {
            decorateLocations(List.of(game));
        }

        return game;
    }

    private void decorateLocations(Collection<Game> games) {
        for (Game game : games) {
            if (game.getVersions() != null) {
                for (GameVersion version : game.getVersions()) {
                    if (version.getLocation() != null && !version.getLocation().isBlank()) {
                        version.setLocation(
                                awsConfigurationProperties.getGamesCloudfront() + "/" + version.getLocation()
                        );
                    }
                }
            }
        }
    }

    public Game getGameById(long gameId) {
        Game game = getGameByIdAuroraDelegate.getGameById(gameId);

        if (game == null) {
            throw new ResourceNotFoundException("Game not found with id: " + gameId);
        }

        decorateLocations(List.of(game));
        return game;
    }
}