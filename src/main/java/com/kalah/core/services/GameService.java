package com.kalah.core.services;


import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GameService {

    final private AppConfig gameConfig;

    // by now only one game is allowed per server. It can be improved by handling several instances of this object;
    private Game game = null;

    @Autowired
    public GameService(AppConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public PlayersDTO initGame() {
        game = new Game(this.gameConfig);
        game.initGame();

        return PlayersDTO.builder()
                .setPlayerTurn(this.game.getCurrentPlayer().getPlayerName())
                .setLastUpdate(new Date())
                .setGameStatus(this.game.getGameStatus())
                .setPlayers(this.game.getPlayers())
                .createPlayersDTO();

    }
}
