/* This game is a classic text-based adventure set in a university environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   a chipper, campus pub, alleyway, and car using simple text commands (e.g., "go east", "go west").
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

import java.util.List;
import java.util.Scanner;

public class ZorkULGame {

  private Parser parser;
  private Player player;
  private String playerName;
  private Room outside, chipper, pub, car, house, alleyway, bathroom1, bathroom2, bathroom3, outsideHouse, bar;

  public ZorkULGame() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Hello there, what is your name?");
    playerName = scanner.nextLine();
    createRooms();
    createNPCs();
    parser = new Parser();
  }

  private void createRooms() {
    // create rooms
    outside = new Room("in the middle of town");
    chipper = new Room("in the Mario's Chipper");
    pub = new Room("in Corrigan's Bar");
    car = new Room("in a cab");
    house = new Room("finally home");
    alleyway = new Room("in a dark and creepy alleyway");
    bathroom1 = new Room("in a bathroom");
    bathroom2 = new Room("in a bathroom");
    bathroom3 = new Room("in a bathroom");
    outsideHouse = new Room("outside the house");
    bar = new Room("at the bar counter");

    // add items to rooms
    Item pint = new Item("pint", "A refreshing pint of beer on the bar.", 1);
    bar.addItem(pint);

    // add npcs to rooms

    // initialise room exits
    outside.setExit("north", chipper);
    outside.setExit("south", car);
    outside.setExit("west", pub);
    outside.setExit("east", alleyway);

    chipper.setExit("south", outside);
    chipper.setExit("east", bathroom2);

    pub.setExit("east", outside);
    pub.setExit("north", bathroom1);
    pub.setExit("order", bar);

    bar.setExit("back", pub);

    car.setExit("north", outside);
    car.setExit("east", house);

    house.setExit("west", outsideHouse);
    house.setExit("south", bathroom3);

    alleyway.setExit("west", outside);

    bathroom1.setExit("south", pub);
    bathroom2.setExit("west", chipper);
    bathroom3.setExit("north", house);
    outsideHouse.setExit("east", house);

    // create the player character and start outside
    player = new Player(playerName, outside, 100, 100);
  }

  public void createNPCs() {
    NPC bartender = new NPC(
      "John the Bartender",
      pub,
      List.of(
        "That'll be 5 euro, please " + playerName + ".",
        "Sláinte! Enjoy your pint!"
      )
    );
    bartender.setWelcomeLines(
      List.of("Well bud, how are things?", "Can I get you a pint?")
    );
    bar.addNpc(bartender);
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
    System.out.println(
      "Welcome " + playerName + ", to a very relatable adventure!"
    );
    System.out.println(
      "It's a Friday evening and you are standing outside one of your favourite places on earth - the pub"
    );
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
      case "collect":
        if (!command.hasSecondWord()) {
          System.out.println("Collect what?");
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
      case "check":
        if (!command.hasSecondWord()) {
          System.out.println("Check what?");
        } else {
          String checkWhat = command.getSecondWord();
          if (checkWhat.equalsIgnoreCase("wallet")) {
            System.out.println(
              "You have " + player.getMoney() + " euro in your wallet."
            );
          } else if (checkWhat.equalsIgnoreCase("health")) {
            System.out.println("You have " + player.getHealth() + " health.");
          } else {
            System.out.println("You can't check that!");
          }
        }
        break;
      case "talk":
        if (player.getCurrentRoom().getNpcsInRoom().isEmpty()) {
          System.out.println("There is no one here to talk to.");
        } else {
          NPC npc = player.getCurrentRoom().getNpcsInRoom().get(0);
          System.out.println("You talk to " + npc.getName() + ":");
          for (String line : npc.getWelcomeLines()) {
            System.out.println("\"" + line + "\"");
          }
          String response = parser.getReader().nextLine();
          if (
            "yes".equalsIgnoreCase(response) &&
            npc.getName().equals("John the Bartender")
          ) {
            System.out.println(npc.getDialogueLines().get(0));
            if (player.getMoney() >= 5) {
              player.removeMoney(5);
              player.addItemToInventory(
                player.getCurrentRoom().getItemsInRoom().get(0)
              );
              System.out.println(npc.getDialogueLines().get(1));
            } else {
              System.out.println("You don't have enough money to buy a pint.");
            }
          } else if ("no".equalsIgnoreCase(response)) {
            System.out.println("Maybe next time!");
          }
        }
        break;
    }
    return false;
  }

  private void printHelp() {
    if (player.getCurrentRoom() == outside) {
      System.out.println("You are outside, why don't you go to the pub :D");
    } else if (player.getCurrentRoom() == chipper) {
      System.out.println(
        "You are in Mario's Chipper, the best place for chips! Howeever, you can always go back to the pub after getting some grub."
      );
    } else if (player.getCurrentRoom() == pub) {
      System.out.println(
        "You are in Corrigan's Bar. Hooray! You should stay here for a good while and support your local"
      );
    } else if (player.getCurrentRoom() == car) {
      System.out.println(
        "You are in a cab. It would be smart to get out and go to your house now."
      );
    } else if (player.getCurrentRoom() == house) {
      System.out.println(
        "You are finally home. You can go west outside the house or south to a bathroom."
      );
    } else if (player.getCurrentRoom() == alleyway) {
      System.out.println(
        "You are in a dark and creepy alleyway. You can go west to outside."
      );
    } else if (player.getCurrentRoom() == bathroom1) {
      System.out.println("You are in a bathroom. You can go south to the pub.");
    } else if (player.getCurrentRoom() == bathroom2) {
      System.out.println(
        "You are in a bathroom. You can go west to Mario's Chipper."
      );
    } else if (player.getCurrentRoom() == bathroom3) {
      System.out.println(
        "You are in a bathroom. You can go north to your house."
      );
    } else if (player.getCurrentRoom() == outsideHouse) {
      System.out.println(
        "You are outside the house. You can go east to your house."
      );
    }
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
