package com.example.gamesapi.controller;

import com.example.gamesapi.dto.SaveGameRequest;
import com.example.gamesapi.model.Game;
import com.example.gamesapi.service.GamesService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class GamesController {

    private final GamesService gamesService;

    public GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping("/games/by-brand")
    public Collection<Game> getGamesByBrand(@RequestParam Long brandId) {
        return gamesService.getGamesByBrand(brandId);
    }

    @GetMapping("/games/by-id")
    public Game getGameById(@RequestParam Long id) {
        return gamesService.getGameById(id);
    }

    @PostMapping("/games/save-game")
    public Game saveGame(@Valid @RequestBody SaveGameRequest request) {
        return gamesService.saveGame(request);
    }

    @PutMapping("/games/update-game")
    public Game updateGame(@Valid @RequestBody SaveGameRequest request) {
        return gamesService.saveGame(request);
    }
}