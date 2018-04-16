package com.kalah.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kalah.core.model.Game;
import com.kalah.core.model.Player;
import com.kalah.core.util.GameStatus;

import java.util.Date;

public class PlayersDTO {

    @JsonProperty("gameStatus")
    private final String gameStatus;
    @JsonProperty("players")
    private final Player[] players;
    @JsonProperty("lastUpdate")
    private final long lastUpdate;
    @JsonProperty("playerTurn")
    private final int playerTurn;

    public PlayersDTO(Game game) {
        this.gameStatus = game.getGameStatus().getValue();
        this.playerTurn = game.getCurrentPlayerRound();
        this.lastUpdate = new Date().getTime();
        this.players = game.getPlayers();
    }

    final public Player[] getPlayers() {
        return this.players;
    }

    final public int getPlayerTurn() {
        return playerTurn;
    }

    final public long getLastUpdate() {
        return lastUpdate;
    }

    final public String getGameStatus() {
        return gameStatus;
    }
}
