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
    private static final int FIRST_PLAYER_INDEX = 0;

    private static GameService gameService;

    @BeforeClass
    public static void setupClass() {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(FIRST_PLAYER_INDEX);
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
        assertThat(gameService.getCurrentPlayerRound()).isEqualTo(FIRST_PLAYER_INDEX);
        assertThat(dto.getPlayers()).isNotNull();
        for (int i = 0; i < GameService.NUMBER_OF_PLAYERS; i++) {
            assertThat(dto.getPlayers()[i].getHouse()).isEqualTo(0);
            Arrays.stream(dto.getPlayers()[i].getPits()).forEach(pit -> assertThat(pit).isEqualTo(INIT_STONES_SIZE));
        }
    }

    @Test
    public void shouldSwitchPlayerWhenSimplePlay() throws BadAttributeValueExpException {
        gameService.initGame();
        Player p1 = gameService.getPlayers()[gameService.getCurrentPlayerRound()];
        // Sanity check
        assertThat(p1.getHouse()).isEqualTo(0);
        assertThat(p1.getPits()[0]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p1.getPits()[1]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p1.getPits()[2]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p1.getPits()[3]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p1.getPits()[4]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p1.getPits()[5]).isEqualTo(INIT_STONES_SIZE);
        // first move check
        gameService.play(1);
        assertThat(p1.getHouse()).isEqualTo(1);
        assertThat(p1.getPits()[0]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p1.getPits()[1]).isEqualTo(0);
        assertThat(p1.getPits()[2]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p1.getPits()[3]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p1.getPits()[4]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p1.getPits()[5]).isEqualTo(INIT_STONES_SIZE + 1);

        Player p2 = gameService.getPlayers()[gameService.getCurrentPlayerRound()];
        // Sanity check
        assertThat(p2.getHouse()).isEqualTo(0);
        assertThat(p2.getPits()[0]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p2.getPits()[1]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p2.getPits()[2]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p2.getPits()[3]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p2.getPits()[4]).isEqualTo(INIT_STONES_SIZE);
        assertThat(p2.getPits()[5]).isEqualTo(INIT_STONES_SIZE);
        gameService.play(1);
        // second move check
        assertThat(p2.getHouse()).isEqualTo(1);
        assertThat(p2.getPits()[0]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p2.getPits()[1]).isEqualTo(0);
        assertThat(p2.getPits()[2]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p2.getPits()[3]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p2.getPits()[4]).isEqualTo(INIT_STONES_SIZE + 1);
        assertThat(p2.getPits()[5]).isEqualTo(INIT_STONES_SIZE + 1);

    }

    @Test
    public void shouldCaptureWhenReachLastMovement() {
        gameService.initGame();
        Player p1 = gameService.getPlayers()[gameService.getCurrentPlayerRound()];
        Player p2 = gameService.getPlayers()[gameService.getNextPlayerIndex()];
        // Should not start from the first as by now the number of houses is the same as stones
        IntStream.range(1, Player.NUMBER_HOUSES).forEach(index -> {
            try {
                gameService.play(index);
                gameService.printPlayersStatus();
                gameService.play(index);
                gameService.printPlayersStatus();
            } catch (BadAttributeValueExpException e) {
                fail("This house shouldn't be empty!");
            }

        });

        // With this scenario, last movement makes a great capture. Checking if it happens.
        assertThat(p1.getHouse()).isEqualTo(5);
        assertThat(p1.getPits()[0]).isEqualTo(0);
        assertThat(p1.getPits()[1]).isEqualTo(6);
        assertThat(p1.getPits()[2]).isEqualTo(5);
        assertThat(p1.getPits()[3]).isEqualTo(4);
        assertThat(p1.getPits()[4]).isEqualTo(3);
        assertThat(p1.getPits()[5]).isEqualTo(1);

        assertThat(p2.getHouse()).isEqualTo(19);
        assertThat(p2.getPits()[0]).isEqualTo(13);
        assertThat(p2.getPits()[1]).isEqualTo(6);
        assertThat(p2.getPits()[2]).isEqualTo(5);
        assertThat(p2.getPits()[3]).isEqualTo(3);
        assertThat(p2.getPits()[4]).isEqualTo(2);
        assertThat(p2.getPits()[5]).isEqualTo(0);

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


    /**
     * In this scenario we'll play 4 stones. The third move should result in a capture.
     * The rule is turned on. The capture is the sum of player and next player pit stones.
     */
    @Test
    public void playerOneShouldCaptureFromPlayerTwo() throws BadAttributeValueExpException {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(FIRST_PLAYER_INDEX);
        config.setRulesEmptyHouse(true);
        config.setStonesNumber(4);
        GameService service = new GameService(config);
        service.initGame();
        service.play(Player.NUMBER_HOUSES - 2); // the one before last house
        service.printPlayersStatus();
        service.play(0); // the last house
        service.printPlayersStatus();
        service.play(0);

        assertThat(service.getPlayers()[0].getHouse()).isEqualTo(8);
        assertThat(service.getPlayers()[0].getPits()[0]).isEqualTo(0);
        assertThat(service.getPlayers()[0].getPits()[Player.NUMBER_HOUSES - 2]).isEqualTo(0);

        assertThat(service.getPlayers()[1].getHouse()).isEqualTo(0);
        assertThat(service.getPlayers()[1].getPits()[0]).isEqualTo(0);
        assertThat(service.getPlayers()[1].getPits()[1]).isEqualTo(0); // the house stealled!
    }

    /**
     * In this scenario we'll play 4 stones. The third move should result in a capture.
     * The rule is turned OFF. The capture is only it's own pit.
     */
    @Test
    public void playerOneShouldCaptureOnlyItsOwn() throws BadAttributeValueExpException {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(FIRST_PLAYER_INDEX);
        config.setRulesEmptyHouse(false);
        config.setStonesNumber(4);
        GameService service = new GameService(config);
        service.initGame();
        service.play(Player.NUMBER_HOUSES - 2); // the one before last house
        service.printPlayersStatus();
        service.play(0); // the last house
        service.printPlayersStatus();
        service.play(0);

        assertThat(service.getPlayers()[0].getHouse()).isEqualTo(2);
        assertThat(service.getPlayers()[0].getPits()[0]).isEqualTo(0);
        assertThat(service.getPlayers()[0].getPits()[Player.NUMBER_HOUSES - 2]).isEqualTo(0);

        assertThat(service.getPlayers()[1].getHouse()).isEqualTo(0);
        assertThat(service.getPlayers()[1].getPits()[0]).isEqualTo(0);
        assertThat(service.getPlayers()[1].getPits()[1]).isEqualTo(6); // opponent's pit maintained!
    }


    @Test
    public void verifyOppositePitIndex() {
        assertThat(GameService.calculateOppositeIndex(0)).isEqualTo(5);
        assertThat(GameService.calculateOppositeIndex(1)).isEqualTo(4);
        assertThat(GameService.calculateOppositeIndex(2)).isEqualTo(3);
        assertThat(GameService.calculateOppositeIndex(3)).isEqualTo(2);
        assertThat(GameService.calculateOppositeIndex(4)).isEqualTo(1);
        assertThat(GameService.calculateOppositeIndex(5)).isEqualTo(0);
    }


    @Test
    public void gameShouldEndWhenAPlayerHasNotStonesInPitsAfterSomeMoves() {
        fail("Not yet implemented");
    }
}