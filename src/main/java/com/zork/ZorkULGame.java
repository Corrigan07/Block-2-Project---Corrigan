/* This game is a classic text-based adventure set in a rural environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   a chipper, pub, alleyway, and car using simple text commands (e.g., "go east", "go west").
    The game provides descriptions of each location and lists possible exits.

Key features include:
Room navigation: Moving among interconnected rooms with named exits.
Simple command parser: Recognizes a limited set of commands like "go", "help", and "quit".
Player character: Tracks current location and handles moving between rooms.
Text descriptions: Provides immersive text output describing the player's surroundings and available options.
Help system: Lists valid commands to guide the player.
Overall, it recreates the classic Zork interactive fiction experience with a familiar-themed setting, 
emphasizing exploration and simple command-driven gameplay
*/
package com.zork;

public class ZorkULGame {

  public static void main(String[] args) {
    GameView view = new ConsoleView();
    GameController controller = new GameController(view);

    controller.startGame();

    while (!controller.isGameOver()) {
      Command command = controller.getCommand();
      controller.processCommand(command);
    }
  }
}
