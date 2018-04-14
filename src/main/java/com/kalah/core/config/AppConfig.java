package com.kalah.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game")
public class AppConfig {

    private int stonesNumber;
    private int defaultFirstPlayer;
    private boolean rulesEmptyHouse;

    public void setStonesNumber(int stonesNumber) {
        this.stonesNumber = stonesNumber;
    }

    public void setRulesEmptyHouse(boolean rulesEmptyHouse) {
        this.rulesEmptyHouse = rulesEmptyHouse;
    }

    public int getNumberStones() {
        return stonesNumber;
    }

    public boolean isRuleEmptyHouse() {
        return rulesEmptyHouse;
    }

    public void setDefaultFirstPlayer(int defaultFirstPlayer) {
        this.defaultFirstPlayer = defaultFirstPlayer;
    }

    public int getDefaultFirstPlayer() {
        return defaultFirstPlayer;
    }
}
