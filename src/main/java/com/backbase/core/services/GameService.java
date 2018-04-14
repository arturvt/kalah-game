package com.backbase.core.services;

import com.backbase.core.config.AppConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService {

    final private static Log logger = LogFactory.getLog(GameService.class);

    final private AppConfig gameConfig;

    private Integer currentPlayerRound = null;

    @Autowired
    public GameService(AppConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public void verifyGameConfig() {
        if (this.gameConfig.getNumberStones() > 0) {
            logger.info("Game can start....");
        }
    }

    /**
     * Creates a new game, with default stones for every player.
     */
    public void initGame() {
        logger.info("Starting a new game.");
        this.currentPlayerRound = gameConfig.getDefaultFirstPlayer();
        logger.info("First player: " + this.currentPlayerRound);
    }


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

    //TODO: implement this
    private boolean hasGameEnded() {
        return false;
    }

}
