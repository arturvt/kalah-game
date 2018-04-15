package com.kalah.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kalah.core.model.Player;
import com.kalah.core.util.GameStatus;

import java.util.Date;

public class PlayersDTO {

    @JsonProperty("gameStatus")
    private final String gameStatus;
    @JsonProperty("players")
    private final Player[] players;
    @JsonProperty("lastUpdate")
    private final Date lastUpdate;
    @JsonProperty("playerTurn")
    private final String playerTurn;

    private PlayersDTO(GameStatus gameStatus, String playerTurn, Date lastUpdate, Player[] players) {
        this.gameStatus = gameStatus.getValue();
        this.lastUpdate = lastUpdate;
        this.players = players;
        this.playerTurn = playerTurn;
    }

    public static Builder builder() {
        return new Builder();
    }

    final public Date getLastUpdate() {
        return lastUpdate;
    }

    final public Player[] getPlayers() {
        return players;
    }

    public static class Builder {
        private GameStatus gameStatus = GameStatus.NOT_STARTED;
        private Date lastUpdate;
        private Player[] players;
        private String playerTurn;

        public Builder setPlayerTurn(String playerTurn) {
            this.playerTurn = playerTurn;
            return this;
        }

        public Builder setGameStatus(GameStatus gameStatus) {
            this.gameStatus = gameStatus;
            return this;
        }

        public Builder setLastUpdate(Date lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Builder setPlayers(Player[] players) {
            this.players = players;
            return this;
        }

        public PlayersDTO createPlayersDTO() {
            return new PlayersDTO(gameStatus, playerTurn, lastUpdate, players);
        }
    }

}
