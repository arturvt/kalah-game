package com.kalah.core.services;


import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.Game;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class GameService {

    final private AppConfig gameConfig;

    private Game game = null;

    @Autowired
    public GameService(AppConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public PlayersDTO initGame() {
        game = new Game(this.gameConfig);
        game.initGame();



        return PlayersDTO.builder()
                .setLastUpdate(new Date())
                .setPlayers(this.game.getPlayers())
                .createPlayersDTO();

    }
}
