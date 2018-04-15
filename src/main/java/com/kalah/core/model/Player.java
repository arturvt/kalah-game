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
    private final String playerName;
    @JsonProperty("pits")
    private final int[] pits;
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
     * @return playResult  {@link PlayResult}
     *  - isPerfectMove?
     *  - isCaptureMove?
     *  - remainingStones
     *  - captureMoveIndexPit
     */
    final public PlayResult play(int indexPit) throws BadAttributeValueExpException {
        PlayResult.Builder builder = PlayResult.builder();


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
                builder.setCaptureIndex(i);
                builder.setCaptureMovement(true);
            }
            totalStonesToDistribute--;
            this.pits[i]++;
        }

        if (totalStonesToDistribute > 0) {
            this.house++;
            totalStonesToDistribute--;
            builder.setPerfectMovement(totalStonesToDistribute == 0);
        }
        builder.setResultantStones(totalStonesToDistribute);

        return builder.createPlayResult();
    }

    /**
     * Distributes stones around player pits.
     * Returns the resultant number.
     * If shouldAddToHouse means that house can receive stones.
     *
     * @param numberOfStones
     * @return resultant number of stones - int
     */
    final public int distributeStones(int numberOfStones, boolean shouldAddToHouse) {
        for (int i = 0; i < NUMBER_HOUSES && numberOfStones > 0; i++) {
            this.pits[i]++;
            numberOfStones--;
        }
        if (shouldAddToHouse && numberOfStones > 0) {
            this.house++;
            numberOfStones--;
        }
        return numberOfStones;
    }

    final public int removeStonesFromIndex(int index) {
        int res = this.pits[index];
        this.pits[index] = 0;
        return res;
    }

    /**
     * This method is called when a capture happens. The caller is allowed to add stones into house
     * even when they're not in player's pits.
     * @param stones
     */
    final public void addIntoHouse(int stones) {
        this.house+=stones;
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
