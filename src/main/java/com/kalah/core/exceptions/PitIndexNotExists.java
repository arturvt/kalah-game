package com.kalah.core.exceptions;

public class PitIndexNotExists extends IllegalArgumentException {
    public PitIndexNotExists(int index) {
        super(String.format("Pit %d does not exists", (index)));
    }
}
