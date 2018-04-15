package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.PlayResult;
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
     * This method calculates the index number of opposite pit by a given current index.
     *
     * @param currentIndex
     * @return
     */
    public static int calculateOppositeIndex(int currentIndex) {
        return -(currentIndex - (Player.NUMBER_HOUSES - 1));
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

        PlayResult playResult = this.players[this.currentPlayerRound].play(indexPit);

        boolean switchPlayer = true;
        if (playResult.isPerfectMovement()) {
            logger.info(String.format("[%d] - has a new turn!", this.currentPlayerRound));
            switchPlayer = false;
        } else if (playResult.isCaptureMovement()) {
            captureStones(playResult.getCaptureIndex());
        } else if (playResult.getResultantStones() > 0) {
            distributeStones(playResult.getResultantStones());

        }
        if (switchPlayer) {
            switchPlayer();
        }

        if (hasGameEnded()) {
            logger.info("End Game!");
            logger.info("Checking the winner...");
        }
    }

    private void switchPlayer() {
        this.currentPlayerRound = getNextPlayerIndex();
        logger.info(String.format("[%d] - is the next player", this.currentPlayerRound));
    }

    public int getNextPlayerIndex() {
        return this.currentPlayerRound == 0 ? 1 : 0;
    }

    public void printPlayersStatus() {
        getCurrentPlayer().printCurrentStatus();
        getNextPlayer().printCurrentStatus();
    }

    /**
     * Distribute stones among players.
     * Current player should aways add into it's house if remaining reaches there.
     *
     * @param playResult
     */
    private void distributeStones(int playResult) {
        while (playResult != 0) {
            playResult = addStonesToNextPlayer(playResult);
            playResult = addStonesToCurrentPlayer(playResult);
        }
    }

    private int addStonesToNextPlayer(int stones) {
        return getNextPlayer().distributeStones(stones, false);
    }

    private int addStonesToCurrentPlayer(int stones) {
        return getCurrentPlayer().distributeStones(stones, true);
    }

    /**
     * @param indexPit
     */
    private void captureStones(int indexPit) {
        // remove stones from a pit and sends them to house.
        int currentPlayerCapture = getCurrentPlayer().removeStonesFromIndex(indexPit);
        getCurrentPlayer().addIntoHouse(currentPlayerCapture);

        // When this is on, capture's opponent as well.
        if (gameConfig.isRuleEmptyHouse()) {
            int opponentCapturedStones = getNextPlayer().removeStonesFromIndex(calculateOppositeIndex(indexPit));
            getCurrentPlayer().addIntoHouse(opponentCapturedStones);
        }
    }

    private Player getCurrentPlayer() {
        return this.players[currentPlayerRound];
    }

    private Player getNextPlayer() {
        return this.players[getNextPlayerIndex()];
    }

    public boolean hasGameEnded() {
        return Arrays.stream(this.players).anyMatch(Player::hasAllPitsEmpty);
    }

}
