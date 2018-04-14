package com.kalah.core.model;

import org.junit.Test;

import javax.management.BadAttributeValueExpException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {
    private static final String P_NAME = "Player";
    private static final int STONE_SIZE = 6;

    @Test
    public void properlyInitAPlayer() {
        Player player = new Player(P_NAME, STONE_SIZE);
        assertThat(player.getHouse()).isEqualTo(0);
        assertThat(player.hasAllPitsEmpty()).isFalse();
        assertThat(player.getPlayerName()).isEqualTo(P_NAME);
    }


    @Test
    public void shouldReturnZeroWhenPlayEndsInHouse() throws BadAttributeValueExpException {
        int initialStones = 1;
        // A player with just 1 stone
        Player p = new Player(P_NAME, initialStones);
        // Play last house
        int res = p.play(Player.NUMBER_HOUSES - 1);

        assertThat(res).isEqualTo(0);
        assertThat(p.getHouse()).isEqualTo(1);

        boolean justTheLastIsModified = IntStream.range(0, Player.NUMBER_HOUSES - 2)
                .allMatch(i -> p.getPits()[i] == initialStones);

        assertThat(justTheLastIsModified).isTrue();
    }

    @Test
    public void shouldDistributeStonesAndIncrementHouseNoResultant() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int numberOfStones = 4;

        int result = p.distributeStones(numberOfStones);

        // just to make sure that this test is valid
        assertThat(numberOfStones).isLessThan(Player.NUMBER_HOUSES - 1);

        boolean justInitialChange = IntStream.range(0, numberOfStones - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        boolean missingDidntChange = IntStream.range(numberOfStones, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE);

        p.printCurrentStatus();
        assertThat(result).isEqualTo(0); // no resultant stones
        assertThat(justInitialChange).isTrue();
        assertThat(missingDidntChange).isTrue();
        assertThat(p.getHouse()).isEqualTo(0); // it shouldn't reach house

    }

    @Test
    public void shouldDistributeStonesAndIncrementHouseWithResultant() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int numberOfStones = 10;
        int result = p.distributeStones(numberOfStones);

        // just to make sure that this test is valid
        assertThat(numberOfStones).isGreaterThan(Player.NUMBER_HOUSES - 1);


        boolean allPitsIncreased = IntStream.range(0, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        p.printCurrentStatus();
        assertThat(allPitsIncreased).isTrue();
        assertThat(p.getHouse()).isEqualTo(1);
        int expectedResultant = numberOfStones - (Player.NUMBER_HOUSES + 1); // we add the house here
        assertThat(result).isEqualTo(expectedResultant);
    }

    /**
     * Even when the number of stones is much higher than the total of houses in the game,
     * this method shall fill once and return the resultant.
     */
    @Test
    public void shouldDistributeStonesAndIncrementHouseWithResultantEvenWithHighNumberStones() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int numberOfStones = 100; // Higher number!
        int result = p.distributeStones(numberOfStones);

        // just to make sure that this test is valid
        assertThat(numberOfStones).isGreaterThan(Player.NUMBER_HOUSES - 1);


        boolean allPitsIncreased = IntStream.range(0, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        p.printCurrentStatus();
        assertThat(allPitsIncreased).isTrue();
        assertThat(p.getHouse()).isEqualTo(1);
        int expectedResultant = numberOfStones - (Player.NUMBER_HOUSES + 1); // we add the house here
        assertThat(result).isEqualTo(expectedResultant);
    }

}