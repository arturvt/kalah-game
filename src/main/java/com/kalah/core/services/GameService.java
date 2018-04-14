package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.Player;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class GameService {
    public static final int NUMBER_OF_PLAYERS = 2;

    final private static Log logger = LogFactory.getLog(GameService.class);

    final private AppConfig gameConfig;

    private Integer currentPlayerRound = null;

    private Player[] players;

    @Autowired
    public GameService(AppConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    /**
     * Creates a new game, with default stones for every player.
     */
    public PlayersDTO initGame() {
        logger.info("Starting a new game.");
        this.currentPlayerRound = gameConfig.getDefaultFirstPlayer();
        this.players = new Player[NUMBER_OF_PLAYERS];
        this.players[0] = new Player("Player 01", gameConfig.getNumberStones());
        this.players[1] = new Player("Player 02", gameConfig.getNumberStones());
        logger.info("First round: " + this.players[this.currentPlayerRound].getPlayerName());
        return PlayersDTO.builder()
                .setLastUpdate(new Date())
                .setPlayers(this.players)
                .createPlayersDTO();
    }

    public Integer getCurrentPlayerRound() {
        return currentPlayerRound;
    }

    /**
     * Plays the desired index for current player.
     * Service must know who's current player.
     *
     * @param indexPit
     */
    public void play(int indexPit) {
        logger.info(String.format("[%d] - plays pit -> %d", this.currentPlayerRound, indexPit));

        if (hasGameEnded()) {
            logger.info("End Game!");
            logger.info("Checking the winner...");
        } else {
            int nextPlayer = verifyNextPlayer();
            if (this.currentPlayerRound == nextPlayer) {
                logger.info(String.format("[%d] - has a new turn", this.currentPlayerRound));
            } else {
                this.currentPlayerRound = nextPlayer;
                logger.info(String.format("[%d] - is the next player", this.currentPlayerRound));
            }
        }

    }

    //TODO: implement this
    private int verifyNextPlayer() {
        return -1;
    }

    public boolean hasGameEnded() {
        return Arrays.stream(this.players).anyMatch(Player::hasAllPitsEmpty);
    }

}
