/* This game is a classic text-based adventure set in a university environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   lecture theatre, campus pub, computing lab, and admin office using simple text commands (e.g., "go east", "go west").
    The game provides descriptions of each location and lists possible exits.

Key features include:
Room navigation: Moving among interconnected rooms with named exits.
Simple command parser: Recognizes a limited set of commands like "go", "help", and "quit".
Player character: Tracks current location and handles moving between rooms.
Text descriptions: Provides immersive text output describing the player's surroundings and available options.
Help system: Lists valid commands to guide the player.
Overall, it recreates the classic Zork interactive fiction experience with a university-themed setting, 
emphasizing exploration and simple command-driven gameplay
*/

import java.util.Scanner;

public class ZorkULGame {

  private Parser parser;
  private Character player;
  private Item item; // Placeholder for future item implementation

  public ZorkULGame() {
    createRooms();
    createItems();
    parser = new Parser();
  }

  private void createRooms() {
    Room outside, theatre, pub, lab, office, hallway;

    // create rooms
    outside = new Room("outside the main entrance of the university");
    theatre = new Room("in a lecture theatre");
    pub = new Room("in the campus pub");
    lab = new Room("in a computing lab");
    office = new Room("in the computing admin office");
    hallway = new Room("in the main hall");

    // add items to rooms
    pub.addItem(new Item("pint", "A refreshing pint of beer on the bar.", 1));

    // initialise room exits
    outside.setExit("east", theatre);
    outside.setExit("south", lab);
    outside.setExit("west", pub);
    outside.setExit("north", hallway);

    theatre.setExit("west", outside);

    pub.setExit("east", outside);

    lab.setExit("north", outside);
    lab.setExit("east", office);

    office.setExit("west", lab);

    hallway.setExit("south", outside);
    hallway.setExit("west", office);

    // create the player character and start outside
    player = new Character("player", outside);
  }

  private void createItems() {
    Item pint = new Item("pint", "A refreshing pint of beer on the bar.", 2);
    pint.setLocation("pub");
  }

  public void play() {
    printWelcome();

    boolean finished = false;
    while (!finished) {
      Command command = parser.getCommand();
      finished = processCommand(command);
    }
    System.out.println("Thank you for playing. Goodbye.");
  }

  private void printWelcome() {
    System.out.println();
    System.out.println("Welcome to the University adventure!");
    System.out.println("Type 'help' if you need help.");
    System.out.println();
    System.out.println(player.getCurrentRoom().getLongDescription());
  }

  private boolean processCommand(Command command) {
    String commandWord = command.getCommandWord();

    if (commandWord == null) {
      System.out.println("I don't understand your command...");
      return false;
    }

    switch (commandWord) {
      case "help":
        printHelp();
        break;
      case "go":
        goRoom(command);
        break;
      case "quit":
        if (command.hasSecondWord()) {
          System.out.println("Quit what?");
          return false;
        } else {
          return true; // signal to quit
        }
      case "look":
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.getLongDescription());

        if (currentRoom.getItemsInRoom().isEmpty()) {
          System.out.println("There are no items here.");
        } else {
          System.out.println("You see the following items:");
          for (Item item : currentRoom.getItemsInRoom()) {
            System.out.println("- " + item.getDescription());
          }
        }
        break;
      case "pickup":
        if (!command.hasSecondWord()) {
          System.out.println("Pick up what?");
        } else {
          String itemName = command.getSecondWord();
          Item itemToPick = null;
          for (Item item : player.getCurrentRoom().getItemsInRoom()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
              itemToPick = item;
              break;
            }
          }
          if (itemToPick != null) {
            player.pickUpItem(itemToPick);
          } else {
            System.out.println("There is no such item here.");
          }
        }
        break;
      case "inventory":
        player.showInventory();
        break;
    }
    return false;
  }

  private void printHelp() {
    System.out.println(
      "You are lost. You are alone. You wander around the university."
    );
    System.out.print("Your command words are: ");
    parser.showCommands();
  }

  private void goRoom(Command command) {
    if (!command.hasSecondWord()) {
      System.out.println("Go where?");
      return;
    }

    String direction = command.getSecondWord();

    Room nextRoom = player.getCurrentRoom().getExit(direction);

    if (nextRoom == null) {
      System.out.println("There is no door!");
    } else {
      player.setCurrentRoom(nextRoom);
      System.out.println(player.getCurrentRoom().getLongDescription());
    }
  }

  public static void main(String[] args) {
    ZorkULGame game = new ZorkULGame();
    game.play();
  }
}
