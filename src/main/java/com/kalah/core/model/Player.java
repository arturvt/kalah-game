package com.kalah.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;

public class Player {
    public static final int NUMBER_HOUSES = 6;
    final private static Log logger = LogFactory.getLog(Player.class);

    @JsonProperty("playerName")
    private String playerName;
    @JsonProperty("pits")
    private int[] pits;
    @JsonProperty("house")
    private int house;

    public Player(String playerName, int initStonesSize) {
        this.playerName = playerName;
        this.house = 0;
        this.pits = new int[NUMBER_HOUSES];

        for (int i = 0; i < NUMBER_HOUSES; i++) {
            this.pits[i] = initStonesSize;
        }

        printCurrentStatus();
    }


    final public int getHouse() {
        return house;
    }

    final public int[] getPits() {
        return pits;
    }

    final public String getPlayerName() {
        return playerName;
    }

    final public boolean hasAllPitsEmpty(){
        return Arrays.stream(this.pits).allMatch(p -> p == 0);
    }

    public void printCurrentStatus() {
        logger.info("-------");
        logger.info(String.format("[%s] House: %d", this.playerName, this.house));

        for (int i = 0; i < pits.length; i++) {
            logger.info(String.format("[%s] {%d} = %d", this.playerName, i, pits[i]));
        }

        logger.info("-------");
    }
}
