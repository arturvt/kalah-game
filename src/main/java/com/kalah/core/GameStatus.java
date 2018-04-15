package com.kalah.core;

public enum GameStatus {
    NOT_STARTED("Not Started"),
    RUNNING("Game is Running"), DRAW("Ended with a Draw"),
    PLAYER_1_WON("Player 01 Won!"),
    PLAYER_2_WON("Player 02 Won!");


    private String value;

    GameStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
