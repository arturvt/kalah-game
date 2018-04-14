package com.backbase.core.controller;

import com.backbase.core.services.GameService;
import com.backbase.core.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class GameController {

    final private IGameService service;

    @Autowired
    public GameController(IGameService service) {
        this.service = service;
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        service.verifyGameConfig();
        return "Hello from backbase!";
    }
}
