package com.kalah.core.services;

public interface IGameService {


    void verifyGameConfig();

    /**
     * Creates a new game, with default stones for every player.
     */
    void initGame();


    void play(int indexPit);


}
