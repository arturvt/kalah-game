package com.kalah.core.model;

public class PlayResult {
    private int resultantStones;
    private boolean perfectMovement;

    public PlayResult(int resultantStones, boolean perfectMovement) {
        this.perfectMovement = perfectMovement;
        this.resultantStones = resultantStones;
    }

    final public int getResultantStones() {
        return resultantStones;
    }

    final public boolean isPerfectMovement() {
        return perfectMovement;
    }
}
