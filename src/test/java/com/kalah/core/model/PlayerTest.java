package com.kalah.core.model;

import org.junit.Test;

import javax.management.BadAttributeValueExpException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

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
    public void shouldBeAPerfectMovement() throws BadAttributeValueExpException {
        // A player starting with 6 stones
        Player p = new Player(P_NAME, STONE_SIZE);
        // Play last house
        PlayResult res = p.play(0);
        p.printCurrentStatus();
        assertThat(res.getResultantStones()).isEqualTo(0);
        assertThat(res.isPerfectMovement()).isEqualTo(true);
        assertThat(p.getHouse()).isEqualTo(1);

        boolean allButFirstModified = IntStream.range(1, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        assertThat(allButFirstModified).isTrue();
    }

    @Test
    public void shouldRemoveStonesFromGivenIndex() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int res = p.removeStonesFromIndex(3);
        assertThat(res).isEqualTo(STONE_SIZE);
        assertThat(p.getPits()[3]).isEqualTo(0);
    }

    @Test
    public void shouldAddStonesToHouse() {
        Player p = new Player(P_NAME, STONE_SIZE);
        assertThat(p.getHouse()).isEqualTo(0);
        p.addIntoHouse(3);
        assertThat(p.getHouse()).isEqualTo(3);
        p.addIntoHouse(3);
        assertThat(p.getHouse()).isEqualTo(6);


    }


    @Test
    public void shouldDistributeStonesAndIncrementHouseNoResultant() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int numberOfStones = 4;

        int result = p.distributeStones(numberOfStones, false);

        // just to make sure that this test is valid
        assertThat(numberOfStones).isLessThan(Player.NUMBER_HOUSES - 1);

        boolean justInitialChange = IntStream.range(0, numberOfStones - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        boolean missingDidNotChange = IntStream.range(numberOfStones, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE);

        p.printCurrentStatus();
        assertThat(result).isEqualTo(0); // no resultant stones
        assertThat(justInitialChange).isTrue();
        assertThat(missingDidNotChange).isTrue();
        assertThat(p.getHouse()).isEqualTo(0); // it shouldn't reach house

    }

    @Test
    public void shouldDistributeStonesInPitsAndDoNotUpdateHouse() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int numberOfStones = 10;
        int result = p.distributeStones(numberOfStones, false);

        // just to make sure that this test is valid
        assertThat(numberOfStones).isGreaterThan(Player.NUMBER_HOUSES - 1);


        boolean allPitsIncreased = IntStream.range(0, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        p.printCurrentStatus();
        assertThat(allPitsIncreased).isTrue();
        assertThat(p.getHouse()).isEqualTo(0);
        int expectedResultant = numberOfStones - (Player.NUMBER_HOUSES); // we add the house here
        assertThat(result).isEqualTo(expectedResultant);
    }

    /**
     * Even when the number of stones is much higher than the total of houses in the game,
     * this method shall fill once and return the resultant.
     */
    @Test
    public void shouldDistributeStonesAndDoNotUpdateHouseWithResultantEvenWithHighNumberStones() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int numberOfStones = 100; // Higher number!
        int result = p.distributeStones(numberOfStones, false);

        // just to make sure that this test is valid
        assertThat(numberOfStones).isGreaterThan(Player.NUMBER_HOUSES - 1);


        boolean allPitsIncreased = IntStream.range(0, Player.NUMBER_HOUSES - 1)
                .allMatch(i -> p.getPits()[i] == STONE_SIZE + 1);

        p.printCurrentStatus();
        assertThat(allPitsIncreased).isTrue();
        assertThat(p.getHouse()).isEqualTo(0);
        int expectedResultant = numberOfStones - (Player.NUMBER_HOUSES); // we add the house here
        assertThat(result).isEqualTo(expectedResultant);
    }

    /**
     * In this scenario, we want to distribute stones in a current user that just
     * played a pit with a large amount of stones. It should update house more than once.
     */
    @Test
    public void shouldUpdateHouseAsLongAsStonesRequires() {
        Player p = new Player(P_NAME, STONE_SIZE);
        int times = 5;
        int numberOfStones = STONE_SIZE * times; // Higher number!

        int rounds = 0;
        while (numberOfStones != 0) {
            rounds++;
            boolean shouldCheckAll = numberOfStones > Player.NUMBER_HOUSES;
            int numberOfPitsChanged = numberOfStones;
            numberOfStones = p.distributeStones(numberOfStones, true);
            p.printCurrentStatus();

            // it will cover all pits and house
            if (shouldCheckAll) {
                for (int i = 0; i < Player.NUMBER_HOUSES; i++) {
                    assertThat(p.getPits()[i] == STONE_SIZE + rounds).isTrue();
                }
                assertThat(p.getHouse()).isEqualTo(rounds);
            } else {
                // now only a few pits are going to be affected, no house.
                assertThat(p.getHouse()).isEqualTo(rounds - 1);
                for (int i = 0; i < numberOfPitsChanged; i++) {
                    assertThat(p.getPits()[i] == STONE_SIZE + rounds).isTrue();
                }

                for (int i = numberOfPitsChanged; i < Player.NUMBER_HOUSES; i++) {
                    assertThat(p.getPits()[i] == STONE_SIZE + (rounds - 1)).isTrue();
                }
            }
        }
    }

    @Test
    public void shouldThrowExceptionWhenInvalidMove() {
        Player p = new Player(P_NAME, 1);
        try {
            p.play(2);
        } catch (BadAttributeValueExpException e) {
            // ignore
            fail("Should not throw right now!");
        }

        // Running twice at the same pit will throw an exception as this pit should be empty.
        Throwable thrown = catchThrowable(() -> p.play(2));

        assertThat(thrown).isInstanceOf(BadAttributeValueExpException.class)
                .hasNoCause();

    }

    /**
     * In this scenario, we'll simulate a capture. As the capture rule might change, {@link Player} should
     * distribute properly, including the last and empty pit, but return the index of the pit where capture
     * can be applied.
     *
     * @throws BadAttributeValueExpException
     */
    @Test
    public void shouldDistributeAndReturnIsCapture() throws BadAttributeValueExpException {
        Player p = new Player(P_NAME, 2);

        int indexPitWillBeEmpty = Player.NUMBER_HOUSES - 1;
        int indexPit = Player.NUMBER_HOUSES - 3;

        PlayResult playResult = p.play(indexPitWillBeEmpty);

        // last house, one for Home, one for next player.
        assertThat(playResult.getResultantStones()).isEqualTo(1);
        assertThat(p.getHouse()).isEqualTo(1);
        p.printCurrentStatus();

        playResult = p.play(indexPit);
        assertThat(playResult.getResultantStones()).isEqualTo(0);
        assertThat(playResult.isPerfectMovement()).isFalse();
        assertThat(playResult.isCaptureMovement()).isTrue();
        assertThat(playResult.getCaptureIndex()).isEqualTo(indexPitWillBeEmpty);
        assertThat(p.getPits()[indexPit]).isEqualTo(0);
        assertThat(p.getHouse()).isEqualTo(1);
        p.printCurrentStatus();

    }

    /**
     * A play in a house with a large amount of stones must distribute and confirm if it is or not a capture.
     * In this scenario, it's a capture but opponents must receive the resultant stones.
     * @throws BadAttributeValueExpException
     */
    @Test
    public void shouldDistributeAndReturnAndCapture() throws BadAttributeValueExpException {
        Player p = new Player(P_NAME, 0);
        p.getPits()[0] = 1;
        p.getPits()[1] = 5;
        p.getPits()[2] = 4;
        p.getPits()[3] = 2;
        p.getPits()[4] = 1;
        p.getPits()[5] = 13;
        PlayResult playResult = p.play(5);

        assertThat(playResult.isCaptureMovement()).isTrue();
        assertThat(playResult.isPerfectMovement()).isFalse();
        assertThat(playResult.getResultantStones()).isEqualTo(12);
        assertThat(playResult.getCaptureIndex()).isEqualTo(5);
        assertThat(p.getPits()[0]).isEqualTo(1);
        assertThat(p.getPits()[1]).isEqualTo(5);
        assertThat(p.getPits()[2]).isEqualTo(4);
        assertThat(p.getPits()[3]).isEqualTo(2);
        assertThat(p.getPits()[4]).isEqualTo(1);
        assertThat(p.getPits()[5]).isEqualTo(0);
        assertThat(p.getHouse()).isEqualTo(1);
    }
}