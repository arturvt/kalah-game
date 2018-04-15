# Kalah Game
[![Build Status](https://travis-ci.org/arturvt/kalah-game.svg?branch=master)](https://travis-ci.org/arturvt/kalah-game)
## About

A Kalah based games, following the most used rules. 


## Architecture

### The Controller

A REST controller that returns a JSON object with information related with the game; generally the dto with current players status.

### The game service

This service is responsible to handle all controller requests and return a DTO.
Game service i also responsible to apply rules and stones distribution among players as players doesn't know about others
data.

### The player object

This object has the responsability to deal only with it's own data. It means that a player can perform a play in any 
pit and it's own house. In case of remaining stones this object shall inform to caller. The possibilities of a play:

* A regular play. Some n stones distributed in n-1 pits.
* A perfect play. The n stones is n + 1 pits (the +1 is the the house/kalah)
* A capture play. The last stone of a regular play stops in an empty pit. Player shall notify the caller that it was a
capture movement. The caller should determine which rule is going to be applied.
