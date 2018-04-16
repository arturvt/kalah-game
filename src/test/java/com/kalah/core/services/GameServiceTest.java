package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.exceptions.PitIndexNotExists;
import com.kalah.core.model.Player;
import com.kalah.core.util.GameStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class GameServiceTest {

    private static final int NUMBER_STONES = 6;

    private static AppConfig generateDefaultAppConfig() {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(0);
        config.setRulesEmptyHouse(true);
        config.setRemainingGoesToOwner(true);
        config.setStonesNumber(NUMBER_STONES);
        return config;
    }


    @Test
    public void initGame() {
        GameService service = new GameService(generateDefaultAppConfig());
        PlayersDTO dto = service.initGame();
        assertThat(dto.getPlayers()).isNotNull();
        for (int i = 0; i < 2; i++) {
            assertThat(dto.getPlayers()[i].getHouse()).isEqualTo(0);
            Arrays.stream(dto.getPlayers()[i].getPits()).forEach(pit -> assertThat(pit).isEqualTo(NUMBER_STONES));
        }
    }

    @Test
    public void whenInvalidPitRequested() {
        GameService service = new GameService(generateDefaultAppConfig());
        service.initGame();

        // Running twice at the same pit will throw an exception as this pit should be empty.
        Throwable thrown = catchThrowable(() -> service.play(10));

        assertThat(thrown).isInstanceOf(PitIndexNotExists.class)
                .hasNoCause();

    }

    /**
     * This is a sanity check that the game will finish no matter how many moves it's required.
     * Technically it can perform infinity loops and this approach could be improved in the future.
     */
    @Test
    public void properlyEndsGame() {
        GameService service = new GameService(generateDefaultAppConfig());
        service.initGame();

        Random random = new Random();

        while (true) {
            try {
                PlayersDTO dto = service.play(random.nextInt(Player.NUMBER_HOUSES));
                if (!dto.getGameStatus().equals(GameStatus.RUNNING.getValue())) {
                    assertThat(dto.getPlayerTurn()).isEqualTo(-1);
                    // at least one player should be empty
                    assertThat(Arrays.stream(dto.getPlayers()).anyMatch(Player::hasAllPitsEmpty)).isTrue();
                    int totalStones = dto.getPlayers()[0].getHouse() + dto.getPlayers()[1].getHouse();
                    assertThat(totalStones).isEqualTo(Player.NUMBER_HOUSES * NUMBER_STONES * 2);
                    System.out.println("End result:" + dto.getGameStatus());
                    break;
                }
            } catch (IllegalArgumentException e) {
                // ignores
            }
        }


    }
}