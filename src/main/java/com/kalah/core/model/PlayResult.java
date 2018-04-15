package com.kalah.core.model;

public class PlayResult {
    private int resultantStones;
    private int captureIndex;
    private boolean perfectMovement;
    private boolean captureMovement;

    private PlayResult(int resultantStones, int captureIndex, boolean perfectMovement, boolean captureMovement) {
        this.captureIndex = captureIndex;
        this.perfectMovement = perfectMovement;
        this.resultantStones = resultantStones;
        this.captureMovement = captureMovement;
    }

    final public int getResultantStones() {
        return resultantStones;
    }

    final public boolean isPerfectMovement() {
        return perfectMovement;
    }

    final public int getCaptureIndex() {
        return captureIndex;
    }

    final public boolean isCaptureMovement() {
        return captureMovement;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private int resultantStones = 0;
        private int captureIndex = -1;
        private boolean perfectMovement = false;
        private boolean captureMovement = false;

        public Builder setResultantStones(int resultantStones) {
            this.resultantStones = resultantStones;
            return this;
        }

        public Builder setCaptureIndex(int captureIndex) {
            this.captureIndex = captureIndex;
            return this;
        }

        public Builder setPerfectMovement(boolean perfectMovement) {
            this.perfectMovement = perfectMovement;
            return this;
        }

        public Builder setCaptureMovement(boolean captureMovement) {
            this.captureMovement = captureMovement;
            return this;
        }

        public PlayResult createPlayResult() {
            return new PlayResult(resultantStones, captureIndex, perfectMovement, captureMovement);
        }
    }

}
