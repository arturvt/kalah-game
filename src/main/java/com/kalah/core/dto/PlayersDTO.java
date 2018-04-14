package com.kalah.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kalah.core.model.Player;

import java.util.Date;

public class PlayersDTO {
    @JsonProperty("lastUpdate")
    private Date lastUpdate;

    @JsonProperty("players")
    private Player[] players;

    private PlayersDTO(Date lastUpdate, Player[] players) {
        this.lastUpdate = lastUpdate;
        this.players = players;
    }


    final public Date getLastUpdate() {
        return lastUpdate;
    }

    final public Player[] getPlayers() {
        return players;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Date lastUpdate;
        private Player[] players;

        public Builder setLastUpdate(Date lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Builder setPlayers(Player[] players) {
            this.players = players;
            return this;
        }

        public PlayersDTO createPlayersDTO() {
            return new PlayersDTO(lastUpdate, players);
        }
    }

}
