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
  private Room outside, chipper, pub, car, house, alleyway, bathroomPub, bathroomHouse, bathroomChipper, outsideHouse, bar, chipperCounter;
  private Item pint, water;

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
    outside = new Room(
      "in the middle of town",
      "You are outside on your favourite street, with the pub to your west and Mario's Chipper to your north."
    );
    chipper = new Room(
      "in the Mario's Chipper",
      "Not the tidiest place, but the chips are the best in town."
    );
    pub = new Room(
      "in Corrigan's Bar",
      "The place is hopping with Friday night football on the telly."
    );
    car = new Room(
      "in a cab",
      "The driver seems friendly but his car could use a clean."
    );
    house = new Room(
      "finally home",
      "Now the fear sets in, will she be happy with you?"
    );
    alleyway = new Room(
      "in a dark and creepy alleyway",
      "What could you possibly want here?"
    );
    bathroomPub = new Room(
      "in a bathroom",
      "A few reasons to be here, but none of them good."
    );
    bathroomHouse = new Room(
      "in a bathroom",
      "The bathroom at home, thank god."
    );
    bathroomChipper = new Room(
      "in a bathroom",
      "You really should have gone before you left the pub, this is horrible."
    );
    outsideHouse = new Room(
      "outside the house",
      "You better sneak in and pray herself is asleep."
    );
    bar = new Room(
      "at the bar counter",
      "Ready to order a pint or two or three or four..."
    );
    chipperCounter = new Room(
      "at the chipper counter",
      "Your mouth is watering at the though of a curry cheese chip."
    );

    // add items to rooms
    pint = new Drink("pint", "A refreshing pint of beer.", 1, false, true);
    bar.addItem(pint);
    water = new Drink(
      "water",
      "A bottle of water to keep you hydrated.",
      1,
      false,
      true
    );
    chipperCounter.addItem(water);

    // add npcs to rooms

    // initialise room exits
    outside.setExit("north", chipper);
    outside.setExit("south", car);
    outside.setExit("west", pub);
    outside.setExit("east", alleyway);

    chipper.setExit("south", outside);
    chipper.setExit("east", bathroomChipper);
    chipper.setExit("order", chipperCounter);

    chipperCounter.setExit("back", chipper);

    pub.setExit("east", outside);
    pub.setExit("north", bathroomPub);
    pub.setExit("order", bar);

    bar.setExit("back", pub);

    car.setExit("north", outside);
    car.setExit("east", house);

    house.setExit("west", outsideHouse);
    house.setExit("south", bathroomHouse);

    alleyway.setExit("west", outside);

    bathroomPub.setExit("south", pub);
    bathroomChipper.setExit("west", chipper);
    bathroomHouse.setExit("north", house);
    outsideHouse.setExit("east", house);

    // create the player character and start outside
    player = new Player(playerName, outside, 100, 100, 0);
  }

  public void createNPCs() {
    NPC bartender = new NPC(
      "John the Bartender",
      pub,
      List.of(
        "That'll be 5 euro, please " + playerName + ".",
        "Sláinte! Enjoy your pint!"
      ),
      List.of("Well bud, how are things?", "Can I get you a pint?")
    );
    bar.addNpc(bartender);
    NPC lad = new NPC(
      "Bob",
      pub,
      List.of(
        "You'll surely stay out for a while tonight, you've been under that woman's thumb for weeks now",
        "United are playing like!",
        "Anyway, go get yourself a pint there!"
      ),
      List.of(
        "Well lad, what's the craic?",
        "Thank god it's Friday that's for sure!"
      )
    );
    pub.addNpc(lad);
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
        System.out.println(currentRoom.getLocationDescription());
        System.out.println(currentRoom.getLongDescription());

        if (currentRoom.getItemsInRoom().isEmpty()) {
          System.out.println("There are no items here.");
        } else {
          System.out.println("You see the following items:");
          for (Item item : currentRoom.getItemsInRoom()) {
            System.out.println("- " + item.getDescription());
          }
        }
        if (currentRoom.getNpcsInRoom().isEmpty()) {
          System.out.println("There is no one to talk to in here");
        } else {
          System.out.println(
            "You see " + currentRoom.getNpcsInRoom().get(0).getName()
          );
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

          if (itemToPick == null) {
            System.out.println("There is no such item here.");
          } else if (!itemToPick.isCollectible()) {
            System.out.println("You can't collect that item.");
          } else {
            player.pickUpItem(itemToPick);
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
              player.addItemToInventory(pint);
              System.out.println("\"" + npc.getDialogueLines().get(1) + "\"");
            } else {
              System.out.println("You don't have enough money to buy a pint.");
            }
          } else if ("no".equalsIgnoreCase(response)) {
            System.out.println("Maybe next time!");
          } else if (npc.getName().equals("Bob")) {
            System.out.println("\"" + npc.getDialogueLines().get(0) + "\"");
            System.out.println("\"" + npc.getDialogueLines().get(1) + "\"");
            System.out.println("\"" + npc.getDialogueLines().get(2) + "\"");
          } else {
            System.out.println("I don't understand your response.");
          }
        }
        break;
      case "drop":
        if (!command.hasSecondWord()) {
          System.out.println("Drop what?");
        } else {
          String itemName = command.getSecondWord();
          Item itemToDrop = null;
          for (Item item : player.getInventory()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
              itemToDrop = item;
              break;
            }
          }
          if (itemToDrop != null) {
            player.dropItem(itemToDrop);
          } else {
            System.out.println("You don't have that item.");
          }
        }
        break;
      case "drink":
        if (!command.hasSecondWord()) {
          System.out.println("Drink what?");
          break;
        }

        String beverage = command.getSecondWord();
        Item itemToDrink = null;

        // Check if player has the item at all
        boolean hasItem = false;
        for (Item item : player.getInventory()) {
          if (item.getName().equalsIgnoreCase(beverage)) {
            hasItem = true;
            // If it’s a drink, mark it as drinkable
            if (item instanceof Drink) {
              itemToDrink = item;
            }
            break;
          }
        }

        // Now respond based on what we found
        if (!hasItem) {
          System.out.println("You don't have that drink.");
          break;
        }

        if (itemToDrink == null) {
          System.out.println("You can't drink that!");
          break;
        }

        // Handle special case: pint
        if (itemToDrink.getName().equalsIgnoreCase("pint")) {
          System.out.println("You drank a pint.");
          player.removeHealth(5);
          player.incrementPintCount();
        } else {
          System.out.println("You drank " + itemToDrink.getName() + ".");
          player.addHealth(5);
        }

        // Remove the item after drinking
        player.removeItemFromInventory(itemToDrink);
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
    } else if (player.getCurrentRoom() == bathroomPub) {
      System.out.println("You are in a bathroom. You can go south to the pub.");
    } else if (player.getCurrentRoom() == bathroomChipper) {
      System.out.println(
        "You are in a bathroom. You can go west to Mario's Chipper."
      );
    } else if (player.getCurrentRoom() == bathroomHouse) {
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
      System.out.println(player.getCurrentRoom().getLocationDescription());
      System.out.println(player.getCurrentRoom().getLongDescription());
    }
  }

  public static void main(String[] args) {
    ZorkULGame game = new ZorkULGame();
    game.play();
  }
}
