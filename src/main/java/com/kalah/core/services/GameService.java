package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.Player;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
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

    final public Integer getCurrentPlayerRound() {
        return currentPlayerRound;
    }

    /**
     * This getter should only be used for testing purposes. Only this
     *
     * @return
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Plays the desired index for current player.
     * Service must know who's current player.
     *
     * @param indexPit
     */
    public void play(int indexPit) throws BadAttributeValueExpException {
        logger.info(String.format("[%d] - plays pit -> %d", this.currentPlayerRound, indexPit));

        if (hasGameEnded()) {
            logger.info("End Game!");
            logger.info("Checking the winner...");
        } else {
            int playResult = this.players[this.currentPlayerRound].play(indexPit);

            if (playResult == 0) {
                logger.info(String.format("[%d] - has a new turn!", this.currentPlayerRound));
            } else if (playResult > 0) {
                distributeStones(playResult);
            } else {
                captureStones(-playResult);
            }
        }
    }

    private void switchPlayer() {
        this.currentPlayerRound = getNextPlayerIndex();
        logger.info(String.format("[%d] - is the next player", this.currentPlayerRound));
    }


    private int getNextPlayerIndex() {
        return this.currentPlayerRound == 0 ? 1 : 0;
    }

    public void printPlayersStatus() {
        this.players[0].printCurrentStatus();
        this.players[1].printCurrentStatus();
    }

    /**
     * Distribute stones among players.
     * Current player should aways add into it's house if remaining reaches there.
     * @param playResult
     */
    private void distributeStones(int playResult) {
        while (playResult != 0) {
            playResult = addStonesToNextPlayer(playResult);
            playResult = addStonesToCurrentPlayer(playResult);
        }
        switchPlayer();
    }

    private int addStonesToNextPlayer(int stones) {
        return this.players[getNextPlayerIndex()].distributeStones(stones, false);
    }

    private int addStonesToCurrentPlayer(int stones) {
        return this.players[currentPlayerRound].distributeStones(stones, true);
    }

    private void captureStones(int indexPit) {
        switchPlayer();
        //TODO: Implement this.
        // It should check if capture is allowed.
        // It should send user's indexPit to house.
    }

    //TODO: implement this
    private void updateNextPlayer() {
        // nothing now.
    }

    public boolean hasGameEnded() {
        return Arrays.stream(this.players).anyMatch(Player::hasAllPitsEmpty);
    }

}
