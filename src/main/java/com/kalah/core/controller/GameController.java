package com.kalah.core.controller;

import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class GameController {

    final private GameService service;

    @Autowired
    public GameController(GameService service) {
        this.service = service;
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello from kalah!";
    }

    @RequestMapping("/init")
    PlayersDTO initGame() {
        return  service.initGame();
    }
}
