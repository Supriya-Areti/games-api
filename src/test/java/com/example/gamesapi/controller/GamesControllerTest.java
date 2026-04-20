package com.example.gamesapi.controller;

import com.example.gamesapi.dto.SaveGameRequest;
import com.example.gamesapi.model.Game;
import com.example.gamesapi.service.GamesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GamesControllerTest {

    private final GamesService gamesService = mock(GamesService.class);
    private final GamesController gamesController = new GamesController(gamesService);

    @Test
    @DisplayName("getGamesByBrand returns games from service")
    void getGamesByBrand_returnsGames() {
        Game game = new Game();
        List<Game> expectedGames = List.of(game);

        when(gamesService.getGamesByBrand(1L)).thenReturn(expectedGames);

        var result = gamesController.getGamesByBrand(1L);

        assertSame(expectedGames, result);
    }

    @Test
    @DisplayName("getGameById returns game from service")
    void getGameById_returnsGame() {
        Game expectedGame = new Game();

        when(gamesService.getGameById(1L)).thenReturn(expectedGame);

        var result = gamesController.getGameById(1L);

        assertSame(expectedGame, result);
    }

    @Test
    @DisplayName("saveGame returns saved game from service")
    void saveGame_returnsSavedGame() {
        SaveGameRequest request = new SaveGameRequest();
        Game expectedGame = new Game();

        when(gamesService.saveGame(request)).thenReturn(expectedGame);

        var result = gamesController.saveGame(request);

        assertSame(expectedGame, result);
    }

    @Test
    @DisplayName("updateGame returns updated game from service")
    void updateGame_returnsUpdatedGame() {
        SaveGameRequest request = new SaveGameRequest();
        Game expectedGame = new Game();

        when(gamesService.saveGame(request)).thenReturn(expectedGame);

        var result = gamesController.updateGame(request);

        assertSame(expectedGame, result);
    }
}