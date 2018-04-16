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
    private final Date lastUpdate;
    @JsonProperty("playerTurn")
    private final String playerTurn;

    public PlayersDTO(Game game) {
        this.playerTurn = game.getCurrentPlayer().getPlayerName();
        this.gameStatus = game.getGameStatus().getValue();
        this.lastUpdate = new Date();
        this.players = game.getPlayers();
    }

    public final Player[] getPlayers() {
        return this.players;
    }

}
