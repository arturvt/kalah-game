package com.kalah.core.model;

import com.kalah.core.GameStatus;
import com.kalah.core.config.AppConfig;
import com.kalah.core.exceptions.BadMovementException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class GameTest {

    private static final int INIT_STONES_SIZE = 6;
    private static final int FIRST_PLAYER_INDEX = 0;

    private static Game gameService;

    @BeforeClass
    public static void setupClass() {
        gameService = new Game(generateDefaultAppConfig());
    }

    private static AppConfig generateDefaultAppConfig() {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(FIRST_PLAYER_INDEX);
        config.setRulesEmptyHouse(true);
        config.setRemainingGoesToOwner(true);
        config.setStonesNumber(INIT_STONES_SIZE);
        return config;
    }


    /**
     * Validates if when game starts, it follows config settings
     */
    @Test
    public void initGame() {
        Game service = new Game(generateDefaultAppConfig());
        assertThat(service.getGameStatus()).isEqualByComparingTo(GameStatus.NOT_STARTED);
        service.initGame();
        assertThat(service.getGameStatus()).isEqualByComparingTo(GameStatus.RUNNING);
        assertThat(service.getCurrentPlayerRound()).isEqualTo(FIRST_PLAYER_INDEX);

    }

    @Test
    public void shouldSwitchPlayerWhenSimplePlay() throws BadMovementException {
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

    /**
     * This is a complext scenario. The last movement should be from player 2 and result in a capture.
     * The tricky part is that the capture happens after one loop of distribution among players, as moved pit have 13
     * stones. Is expected that it adds stones into player 2 house, all pits from player one and two and capture
     * the content of 1st pit from player one.
     */
    @Test
    public void shouldCaptureWhenReachLastMovement() throws BadMovementException {
        gameService.initGame();
        Player p1 = gameService.getPlayers()[gameService.getCurrentPlayerRound()];
        Player p2 = gameService.getPlayers()[gameService.getNextPlayerIndex()];

        // Preparing the environment
        IntStream.range(1, Player.NUMBER_HOUSES).forEach(index -> {
            try {
                gameService.play(index); // p1
                gameService.printPlayersStatus();
                if (index == Player.NUMBER_HOUSES - 1) {
                    System.out.println("-----------");
                    return;
                }
                gameService.play(index); // p2
                gameService.printPlayersStatus();
            } catch (BadMovementException e) {
                fail("This house shouldn't be empty!");
            }
        });

        // Check if the scenario is what we want.
        assertThat(p1.getHouse()).isEqualTo(5);
        assertThat(p1.getPits()[0]).isEqualTo(12);
        assertThat(p1.getPits()[1]).isEqualTo(5);
        assertThat(p1.getPits()[2]).isEqualTo(4);
        assertThat(p1.getPits()[3]).isEqualTo(3);
        assertThat(p1.getPits()[4]).isEqualTo(2);
        assertThat(p1.getPits()[5]).isEqualTo(0);

        assertThat(p2.getHouse()).isEqualTo(4);
        assertThat(p2.getPits()[0]).isEqualTo(12);
        assertThat(p2.getPits()[1]).isEqualTo(5);
        assertThat(p2.getPits()[2]).isEqualTo(4);
        assertThat(p2.getPits()[3]).isEqualTo(2);
        assertThat(p2.getPits()[4]).isEqualTo(1);
        assertThat(p2.getPits()[5]).isEqualTo(13);

        gameService.play(Player.NUMBER_HOUSES - 1); // now the movement.

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
        Game service = new Game(config);
        service.initGame();
        assertThat(service.hasGameEnded()).isTrue();
    }


    /**
     * In this scenario we'll play 4 stones. The third move should result in a capture.
     * The rule is turned on. The capture is the sum of player and next player pit stones.
     */
    @Test
    public void playerOneShouldCaptureFromPlayerTwo() throws BadMovementException {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(FIRST_PLAYER_INDEX);
        config.setRulesEmptyHouse(true);
        config.setStonesNumber(4);
        Game service = new Game(config);
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
    public void playerOneShouldCaptureOnlyItsOwn() throws BadMovementException {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(FIRST_PLAYER_INDEX);
        config.setRulesEmptyHouse(false);
        config.setStonesNumber(4);
        Game service = new Game(config);
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
        assertThat(Game.calculateOppositeIndex(0)).isEqualTo(5);
        assertThat(Game.calculateOppositeIndex(1)).isEqualTo(4);
        assertThat(Game.calculateOppositeIndex(2)).isEqualTo(3);
        assertThat(Game.calculateOppositeIndex(3)).isEqualTo(2);
        assertThat(Game.calculateOppositeIndex(4)).isEqualTo(1);
        assertThat(Game.calculateOppositeIndex(5)).isEqualTo(0);
    }


    @Test
    public void gameShouldEndWhenNotStonesInOnePlayerPitsRemainingGoesToWinner() {
        Random random = new Random();
        gameService.initGame();
        while (!gameService.hasGameEnded()) {
            int index = random.nextInt(Player.NUMBER_HOUSES);
            try {
                gameService.play(index);
            } catch (BadMovementException e) {
                // ignores.
            }
        }

        int indexWinner = gameService.getPlayers()[0].hasAllPitsEmpty() ? 0 : 1;
        int indexLoser = -(indexWinner - 1);

        gameService.printPlayersStatus();
        gameService.finishGame();
        gameService.printPlayersStatus();
    }
}