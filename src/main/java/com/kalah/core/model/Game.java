package com.kalah.core.model;

import com.kalah.core.config.AppConfig;
import com.kalah.core.exceptions.BadMovementException;
import com.kalah.core.util.GameStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;


public class Game {
    private static final int NUMBER_OF_PLAYERS = 2;
    final private static Log logger = LogFactory.getLog(Game.class);
    final private AppConfig gameConfig;
    private GameStatus gameStatus;
    private Integer currentPlayerRound = null;

    private Player[] players;

    public Game(AppConfig gameConfig) {
        this.gameConfig = gameConfig;
        this.gameStatus = GameStatus.NOT_STARTED;
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
    public void initGame() {
        logger.info("Starting a new game.");
        this.currentPlayerRound = gameConfig.getDefaultFirstPlayer();
        this.players = new Player[NUMBER_OF_PLAYERS];
        this.players[0] = new Player(0, "Player 01", gameConfig.getNumberStones());
        this.players[1] = new Player(1, "Player 02", gameConfig.getNumberStones());
        this.gameStatus = GameStatus.RUNNING;
        logger.info("First round: " + this.players[this.currentPlayerRound].getPlayerName());
    }

    final public Integer getCurrentPlayerRound() {
        return this.currentPlayerRound;
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
    public void play(int indexPit) throws BadMovementException {
        logger.info(String.format("[%d] - plays pit -> %d", this.currentPlayerRound, indexPit));

        PlayResult playResult = this.players[this.currentPlayerRound].play(indexPit);

        boolean switchPlayer = true;

        distributeStones(playResult.getResultantStones());

        if (playResult.isPerfectMovement()) {
            logger.info(String.format("[%d] - has a new turn!", this.currentPlayerRound));
            switchPlayer = false;
        } else if (playResult.isCaptureMovement()) {
            captureStones(playResult.getCaptureIndex());
        }

        if (switchPlayer) {
            switchPlayer();
        }

        if (hasGameEnded()) {
            finishGame();
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
     * Return missing distributed stones.
     *
     * @param playResult - number of stones to be distributed
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

    public Player getCurrentPlayer() {
        return this.players[currentPlayerRound];
    }

    private Player getNextPlayer() {
        return this.players[getNextPlayerIndex()];
    }

    public boolean hasGameEnded() {
        return Arrays.stream(this.players).anyMatch(Player::hasAllPitsEmpty);
    }


    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void finishGame() {
        logger.info("Game is over! Checking the result!");
        if (gameConfig.isRemainingGoesToOwner()) {
            int indexStillMissingStones = this.players[0].hasAllPitsEmpty() ? 1 : 0;
            logger.info("Remaining stones in pits going to player: "+this.players[indexStillMissingStones].getPlayerName());
            this.players[indexStillMissingStones].moveAllStonesToHouse();
        }

        int player01Amount = this.players[0].getHouse();
        int player02Amount = this.players[1].getHouse();


        if (player01Amount == player02Amount) {
            this.gameStatus = GameStatus.DRAW;
        } else if (player01Amount > player02Amount) {
            this.gameStatus = GameStatus.PLAYER_1_WON;
        } else {
            this.gameStatus = GameStatus.PLAYER_2_WON;
        }
        this.currentPlayerRound = -1;
        logger.info("Game final result: " + this.gameStatus.getValue());

    }

}
