package com.kalah.core.services;


import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.exceptions.PitIndexNotExists;
import com.kalah.core.model.Game;
import com.kalah.core.model.Player;
import com.kalah.core.util.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return new PlayersDTO(game);
    }

    public PlayersDTO play(int index) {
        validateMovement(index);
        game.play(index);

        if (game.getGameStatus() != GameStatus.RUNNING) {
            game.finishGame();
        }
        return new PlayersDTO(game);
    }


    private void validateMovement(int index) throws IllegalArgumentException {
        if (index > Player.NUMBER_HOUSES - 1) {
            throw new PitIndexNotExists(index);
        }
    }

}
