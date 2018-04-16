package com.kalah.core.controller;

import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@EnableAutoConfiguration
public class GameController {

    final private GameService service;
    
    @Autowired
    public GameController(GameService service) {
        this.service = service;
    }

    @RequestMapping("/init")
    PlayersDTO initGame() {
        return service.initGame();
    }

    @RequestMapping("/play/{pitIndex}")
    PlayersDTO play(@PathVariable int pitIndex) {
        return service.play(pitIndex);
    }
}
