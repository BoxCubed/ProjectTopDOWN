ProjectTopDown
================
_A simple Top Down shooter_
## To play
- Start the server in desktop\me\boxcubed\main\desktop\server
- Then launch the desktop launcher in the desktop module
- To switch between multiplayer and singleplayer find this line in the Gamestate class...
```java
player = new Player(gameWORLD,1);
```
and change the one to 0 for singleplayer or leave it as one for multiplayer.(The server must be on for multiplayer)

## To import
- Just import using your favourite IDE and run the gradle build files
- Or use the gradle and git terminal if you are _werid_ like that
