# Kalah Game
[![Build Status](https://travis-ci.org/arturvt/kalah-game.svg?branch=master)](https://travis-ci.org/arturvt/kalah-game)
## About

A Kalah based games, following the most used rules.


## Architecture

### The Controller

A REST controller that returns a JSON object with information related with the game; generally the dto with current players status.

### The game service

This service is responsible to handle all controller requests and return a DTO.
This service does not allows more than one game running in parallel.

### The Game

Game object is responsible to manage a game with two players with n-stones and 6 pits. Every play is handled by this object.


### The player object

This object has the responsibility to deal only with it's own data. It means that a player can perform a play in any 
pit and it's own house. In case of remaining stones this object shall inform to caller. The possibilities of a play:

* A regular play. Some n stones distributed in n-1 pits.
* A perfect play. The n stones is n + 1 pits (the +1 is the the house/kalah)
* A redistributed play. The n stones in z pits and the kalah. The remaining stones goes to the other player.
* A capture play. The last stone of a regular play stops in an empty pit. Player shall notify the caller that it was a
capture movement. The caller should determine which rule is going to be applied.

## Configuring the project

There is an `application.properties` file in `/src/main/resources/application.properties` where you can configure:

 - Initial stones number (6)
 - Empty House rule (true)
    - When true, a capture opponents opposite pit content when it's a capture
 - Default first player (0)
 - Remaining stones goes to owner at the end of game (false)


## Running project

### Docker

You can run the already build and published image in docker hub by running: ```docker-compose up``` from /docker path.
You also can build a new local image with gradle ou makefile.
    - Gradle: builds a new image ```gradle buildDockerImage``` 
    - Makefile: ```make build-image```and then ```make run``` or ```docker-compose up``` from /docker path


### Gradle

This project requires Gradle 4.x or higher. Use ```gradle bootRun``` to run. By default it'll be listening in port 8080.
You can change that on application.properties file.


### Jar

Run: ```java -jar core.jar``` you can find the core.jar file in $ProjectPath/build/libs after running ```gradle build```


## Usage

### Web

Just open you browser the url: localhost:8080  and have fun. 

### API

All available API endpoints (only GET methods):

 - Home: `localhost:8080/` - this is the only method that does not returns a JSON.
 - Init a game: `localhost:8080/api/init`
 - Play: `localhost:8080/api/play/{index_id}`
    - Please note that it isn't required to determine who's going to be the player. API methods are only allowed to play some index.
    


## For the future

- Website should return a message with info about a play, for example it the result is a Capture ou a play again.
- Improve controller unit tests.