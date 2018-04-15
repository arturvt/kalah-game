package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.model.Player;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.management.BadAttributeValueExpException;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
        PlayersDTO dto = gameService.initGame();
        assertThat(gameService.getCurrentPlayerRound()).isEqualTo(1);
        assertThat(dto.getPlayers()).isNotNull();
        for (int i = 0; i < GameService.NUMBER_OF_PLAYERS; i++) {
            assertThat(dto.getPlayers()[i].getHouse()).isEqualTo(0);
            Arrays.stream(dto.getPlayers()[i].getPits()).forEach(pit -> assertThat(pit).isEqualTo(INIT_STONES_SIZE));
        }
    }

    @Test
    public void shouldSwitchPlayerWhenSimplePlay() {
        gameService.initGame();
        // Should not start from the first as by now the number of houses is the same as stones
        IntStream.range(1, Player.NUMBER_HOUSES-1).forEach(index -> {
            try {
                gameService.play(index);
                gameService.play(index);
                //TODO: Falta implementar a captura!
                gameService.printPlayersStatus();
            } catch (BadAttributeValueExpException e) {
                fail("This house shouldn't be empty!");
            }

        });

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