Battleship (WIP)
================

This is a multiplayer version of [battleship](http://en.wikipedia.org/wiki/Battleship_\(game\)).

Build
-----

Battleship can be built with [gradle](http://www.gradle.org/). To do so run ```./gradlew build```.

Start
-----

First you need to start an instance of MongoDB with

```mongod --config /usr/local/etc/mongod.conf```

then run

```./gradlew run```

and open [localhost:8080](http://localhost:8080/) in your browser.