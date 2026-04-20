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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GamesServiceTest {

    private final GetGamesAuroraDelegate getGamesAuroraDelegate = mock(GetGamesAuroraDelegate.class);
    private final GetGameByIdAuroraDelegate getGameByIdAuroraDelegate = mock(GetGameByIdAuroraDelegate.class);
    private final AddGameAuroraDelegate addGameAuroraDelegate = mock(AddGameAuroraDelegate.class);
    private final UpdateGameAuroraDelegate updateGameAuroraDelegate = mock(UpdateGameAuroraDelegate.class);
    private final AWSConfigurationProperties awsConfigurationProperties = mock(AWSConfigurationProperties.class);

    private final GamesService gamesService = new GamesService(
            getGamesAuroraDelegate,
            getGameByIdAuroraDelegate,
            addGameAuroraDelegate,
            updateGameAuroraDelegate,
            awsConfigurationProperties
    );

    @Test
    void getGamesByBrand_decoratesVersionLocations() {
        GameVersion version = new GameVersion();
        version.setLocation("games/findables/v1/index.html");

        Game game = new Game();
        game.setVersions(List.of(version));

        when(getGamesAuroraDelegate.getGamesByBrand(1L)).thenReturn(List.of(game));
        when(awsConfigurationProperties.getGamesCloudfront())
                .thenReturn("https://dummy-cloudfront-domain.net");

        var result = gamesService.getGamesByBrand(1L);

        Game resultGame = result.iterator().next();
        GameVersion resultVersion = resultGame.getVersions().get(0);

        assertEquals(
                "https://dummy-cloudfront-domain.net/games/findables/v1/index.html",
                resultVersion.getLocation()
        );
    }

    @Test
    void getGamesByBrand_leavesBlankLocationUnchanged() {
        GameVersion version = new GameVersion();
        version.setLocation("");

        Game game = new Game();
        game.setVersions(List.of(version));

        when(getGamesAuroraDelegate.getGamesByBrand(1L)).thenReturn(List.of(game));
        when(awsConfigurationProperties.getGamesCloudfront())
                .thenReturn("https://dummy-cloudfront-domain.net");

        var result = gamesService.getGamesByBrand(1L);

        Game resultGame = result.iterator().next();
        GameVersion resultVersion = resultGame.getVersions().get(0);

        assertEquals("", resultVersion.getLocation());
    }

    @Test
    void getGameById_whenGameExists_decoratesLocation() {
        GameVersion version = new GameVersion();
        version.setLocation("games/super-game/v1/index.html");

        Game game = new Game();
        game.setVersions(List.of(version));

        when(getGameByIdAuroraDelegate.getGameById(1L)).thenReturn(game);
        when(awsConfigurationProperties.getGamesCloudfront())
                .thenReturn("https://dummy-cloudfront-domain.net");

        var result = gamesService.getGameById(1L);

        assertSame(game, result);
        assertEquals(
                "https://dummy-cloudfront-domain.net/games/super-game/v1/index.html",
                result.getVersions().get(0).getLocation()
        );
    }

    @Test
    void getGameById_whenGameDoesNotExist_throwsException() {
        when(getGameByIdAuroraDelegate.getGameById(999L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> gamesService.getGameById(999L));
    }

    @Test
    void saveGame_whenCreatingNewGame_returnsDecoratedGame() {
        SaveGameRequest request = new SaveGameRequest();
        request.setBrandId(1L);
        request.setName("findables");
        request.setEngine("findObject");

        GameVersion version = new GameVersion();
        version.setLocation("games/findables/v1/index.html");

        Game savedGame = new Game();
        savedGame.setVersions(List.of(version));

        when(addGameAuroraDelegate.execute(1L, "findables", "findObject")).thenReturn(10L);
        when(getGameByIdAuroraDelegate.getGameById(10L)).thenReturn(savedGame);
        when(awsConfigurationProperties.getGamesCloudfront())
                .thenReturn("https://dummy-cloudfront-domain.net");

        var result = gamesService.saveGame(request);

        assertSame(savedGame, result);
        assertEquals(
                "https://dummy-cloudfront-domain.net/games/findables/v1/index.html",
                result.getVersions().get(0).getLocation()
        );
    }

    @Test
    void saveGame_whenUpdatingExistingGame_returnsDecoratedGame() {
        SaveGameRequest request = new SaveGameRequest();
        request.setId(5L);
        request.setBrandId(1L);
        request.setName("updated game");
        request.setEngine("superEngine");

        GameVersion version = new GameVersion();
        version.setLocation("games/updated-game/v1/index.html");

        Game updatedGame = new Game();
        updatedGame.setVersions(List.of(version));

        when(updateGameAuroraDelegate.execute(5L, 1L, "updated game", "superEngine")).thenReturn(1);
        when(getGameByIdAuroraDelegate.getGameById(5L)).thenReturn(updatedGame);
        when(awsConfigurationProperties.getGamesCloudfront())
                .thenReturn("https://dummy-cloudfront-domain.net");

        var result = gamesService.saveGame(request);

        assertSame(updatedGame, result);
        assertEquals(
                "https://dummy-cloudfront-domain.net/games/updated-game/v1/index.html",
                result.getVersions().get(0).getLocation()
        );
    }

    @Test
    void saveGame_whenUpdateAffectsNoRows_returnsNull() {
        SaveGameRequest request = new SaveGameRequest();
        request.setId(5L);
        request.setBrandId(1L);
        request.setName("updated game");
        request.setEngine("superEngine");

        when(updateGameAuroraDelegate.execute(5L, 1L, "updated game", "superEngine")).thenReturn(0);

        var result = gamesService.saveGame(request);

        assertNull(result);
    }
}