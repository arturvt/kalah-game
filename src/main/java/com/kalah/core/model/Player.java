package com.kalah.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.BadAttributeValueExpException;
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

    final public boolean hasAllPitsEmpty() {
        return Arrays.stream(this.pits).allMatch(p -> p == 0);
    }

    /**
     * Plays the selected index and returns the number of stones that should
     * be passed to next player.
     *
     * @return playResult - int
     * 0: means that the last stone was in user's house.
     * +1: the number of stones for following user
     * -1: the index of a house's capture. This is when last stone was in an empty pits owned by this player.
     */
    final public int play(int indexPit) throws BadAttributeValueExpException {
        if (this.pits[indexPit] == 0) {
            logger.error("Attempt to play in an empty pit; Index: " + indexPit);
            throw new BadAttributeValueExpException("Invalid move!");
        }

        int totalStonesToDistribute = this.pits[indexPit];
        this.pits[indexPit] = 0;

        for (int i = indexPit + 1; i < NUMBER_HOUSES && totalStonesToDistribute > 0; i++) {
            // we check if the pit is empty and is the last stone
            // indicating that this is a capture!
            if (this.pits[i] == 0 && totalStonesToDistribute == 1) {
                totalStonesToDistribute = -i;
            } else {
                totalStonesToDistribute--;
            }
            this.pits[i]++;
        }

        // We must credit the house if it's missing stones.
        if (totalStonesToDistribute > 0) {
            this.house++;
            totalStonesToDistribute--;
        }

        return totalStonesToDistribute;
    }

    /**
     * Distributes stones around player pits.
     * Returns the resultant number.
     * @param numberOfStones
     * @return resultant number of stones - int
     */
    final public int distributeStones(int numberOfStones) {
        for(int i = 0; i < NUMBER_HOUSES && numberOfStones > 0; i++) {
            this.pits[i]++;
            numberOfStones--;
        }
        if (numberOfStones > 0) {
            this.house++;
            numberOfStones--;
        }
        return numberOfStones;
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
