package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import com.kalah.core.exceptions.PitIndexNotExists;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Arrays;

public class GameServiceTest {

    private static AppConfig generateDefaultAppConfig() {
        AppConfig config = new AppConfig();
        config.setDefaultFirstPlayer(0);
        config.setRulesEmptyHouse(true);
        config.setRemainingGoesToOwner(true);
        config.setStonesNumber(5);
        return config;
    }


    @Test
    public void initGame() {
        GameService service = new GameService(generateDefaultAppConfig());
        PlayersDTO dto = service.initGame();
        assertThat(dto.getPlayers()).isNotNull();
        for (int i = 0; i < 2; i++) {
            assertThat(dto.getPlayers()[i].getHouse()).isEqualTo(0);
            Arrays.stream(dto.getPlayers()[i].getPits()).forEach(pit -> assertThat(pit).isEqualTo(5));
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
}