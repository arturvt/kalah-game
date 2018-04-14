package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.management.BadAttributeValueExpException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class GameServiceTest {

    private static final int INIT_STONES_SIZE = 6;

    private static GameService gameService;

    @BeforeClass
    public static void setupClass() {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(1);
        config.setRulesEmptyHouse(true);
        config.setStonesNumber(INIT_STONES_SIZE);
        gameService = new GameService(config);
    }


    /**
     * Validates if when game starts, it follows config settings
     */
    @Test
    public void initGame() {
        // it starts null.
        assertThat(gameService.getCurrentPlayerRound()).isNull();
        PlayersDTO dto = gameService.initGame();
        assertThat(gameService.getCurrentPlayerRound()).isEqualTo(1);
        assertThat(dto.getPlayers()).isNotNull();
        for (int i = 0; i < GameService.NUMBER_OF_PLAYERS; i++) {
            assertThat(dto.getPlayers()[i].getHouse()).isEqualTo(0);
            Arrays.stream(dto.getPlayers()[i].getPits()).forEach(pit -> {
                assertThat(pit).isEqualTo(INIT_STONES_SIZE);
            });
        }
    }

    @Test
    public void play() throws BadAttributeValueExpException {
        gameService.initGame();
        gameService.play(1);
    }

    @Test
    public void gameShouldNotEndWhenJustBegin() {
        gameService.initGame();
        assertThat(gameService.hasGameEnded()).isFalse();
    }

    @Test
    public void gameShouldEndWhenAPlayerHasNotPits() {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(1);
        config.setRulesEmptyHouse(true);
        config.setStonesNumber(0); // No stones!
        GameService service = new GameService(config);
        service.initGame();
        assertThat(service.hasGameEnded()).isTrue();
    }
}