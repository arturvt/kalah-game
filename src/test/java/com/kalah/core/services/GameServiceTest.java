package com.kalah.core.services;

import com.kalah.core.config.AppConfig;
import com.kalah.core.dto.PlayersDTO;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

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
}