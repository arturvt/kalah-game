package com.kalah.core.exceptions;

public class BadMovementException extends IllegalArgumentException {

    public BadMovementException(int index) {
        super(String.format("Invalid movement for pit: %d", (index+1)));
    }
}
