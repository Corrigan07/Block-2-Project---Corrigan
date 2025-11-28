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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ZorkULGame {

  private Parser parser;
  private Player player;
  private String gameChoice;
  private String playerName;
  private Room outside, chipper, pub, cab, house, alleyway, bathroomPub, bathroomChipper, outsideHouse, bar, chipperCounter;
  private Drink pint, water;
  private Food chips, burger, kebab;
  private Weapon bottle, hurl;
  private ArrayList<PurchasableItem> menu = new ArrayList<>();
  private DiceGame diceGame;
  private NPC bartender, lad, chipperOwner, billy, cabDriver, wife;
  private boolean billyEncountered = false;

  public ZorkULGame() {
    Scanner scanner = new Scanner(System.in);
    System.out.println(
      "Would you like to start a new game or load a saved game? (new/load)"
    );
    gameChoice = scanner.nextLine().trim().toLowerCase();
    createRooms();
    createNPCs();
    createItems();
    parser = new Parser();
    diceGame = new DiceGame();
  }

  private void createRooms() {
    // create rooms
    outside = new Room(
      "in the middle of town",
      "You are outside on your favourite street, with the pub to your west and Mario's Chipper to your north.",
      false,
      false
    );
    chipper = new Room(
      "in the Mario's Chipper",
      "Not the tidiest place, but the chips are the best in town.",
      false,
      false
    );
    pub = new Room(
      "in Corrigan's Bar",
      "The place is hopping with Friday night football on the telly.",
      false,
      false
    );
    cab = new Room(
      "in a cab",
      "The driver seems friendly but his car could use a clean.",
      false,
      false
    );
    house = new Room(
      "finally home",
      "Now the fear sets in, will she be happy with you?",
      false,
      true
    );
    alleyway = new Room(
      "in a dark and creepy alleyway",
      "What could you possibly want here?",
      true,
      false
    );
    bathroomPub = new Room(
      "in a bathroom",
      "A few reasons to be here, but none of them good.",
      false,
      false
    );

    bathroomChipper = new Room(
      "in a bathroom",
      "You really should have gone before you left the pub, this is horrible.",
      false,
      false
    );
    outsideHouse = new Room(
      "outside the house",
      "You better sneak in and pray herself is asleep.",
      true,
      false
    );
    bar = new Room(
      "at the bar counter",
      "Ready to order a pint or two or three or four...",
      false,
      false
    );
    chipperCounter = new Room(
      "at the chipper counter",
      "Your mouth is watering at the though of a curry cheese chip.",
      false,
      false
    );

    // initialise room exits
    outside.setExit(Direction.NORTH, chipper);
    outside.setExit(Direction.SOUTH, cab);
    outside.setExit(Direction.WEST, pub);
    outside.setExit(Direction.EAST, alleyway);

    chipper.setExit(Direction.SOUTH, outside);
    chipper.setExit(Direction.EAST, bathroomChipper);
    chipper.setExit(Direction.ORDER, chipperCounter);

    chipperCounter.setExit(Direction.BACK, chipper);
    pub.setExit(Direction.EAST, outside);
    pub.setExit(Direction.NORTH, bathroomPub);
    pub.setExit(Direction.ORDER, bar);

    bar.setExit(Direction.BACK, pub);

    cab.setExit(Direction.NORTH, outside);

    alleyway.setExit(Direction.WEST, outside);

    bathroomPub.setExit(Direction.SOUTH, pub);
    bathroomChipper.setExit(Direction.WEST, chipper);
    outsideHouse.setExit(Direction.EAST, house);

    if (gameChoice.equals("load")) {
      Player loaded = Player.loadPlayerState();
      if (loaded != null) {
        player = loaded;
        // restore runtime-only fields
        if (player.getCurrentRoom() == null) {
          player.setCurrentRoom(outside);
        }
        // ensure playerName matches the loaded player so welcome text is correct
        playerName = player.getName();
        System.out.println(
          "Game loaded successfully! Welcome back, " + playerName + "!"
        );
      } else {
        System.out.println("No saved game found. Starting a new game.");
        System.out.println("Hello there, what is your name?");
        playerName = new Scanner(System.in).nextLine();
        player = new Player(
          playerName,
          outside,
          100,
          100,
          0,
          (18 * 60),
          100,
          false
        );
      }
    } else if (gameChoice.equals("new")) {
      System.out.println("Hello there, what is your name?");
      playerName = new Scanner(System.in).nextLine();
      player = new Player(
        playerName,
        outside,
        100,
        100,
        0,
        (18 * 60),
        100,
        false
      );
    } else {
      // create the player character and start outside
      System.out.println("Unknown command. Starting a new game.");
      System.out.println("Hello there, what is your name?");
      playerName = new Scanner(System.in).nextLine();
      player = new Player(
        playerName,
        outside,
        100,
        100,
        0,
        (18 * 60),
        100,
        false
      );
    }
  }

  public void createItems() {
    pint = new Drink("pint", "A refreshing pint of beer.", false, true, 5);
    bar.addItem(pint);
    water = new Drink(
      "water",
      "A bottle of water to keep you hydrated.",
      false,
      true,
      2
    );
    bottle = new Weapon("bottle", "A glass bottle", 10, true, true);
    Box<Weapon> bottleBox = new Box<>(
      "box",
      "A box containing a bottle",
      false,
      true
    );
    bottleBox.setValue(bottle);
    bathroomPub.addItem(bottleBox);

    chipperCounter.addItem(water);
    chips = new Food("chips", "A delicious serving of chips.", false, true, 3);
    chipperCounter.addItem(chips);
    menu.add(chips);

    hurl = new Weapon("hurl", "A hurling stick", 15, true, true);
    chipper.addItem(hurl);

    Box<Food> box = new Box<>("box", "A takeaway box", false, true);
    box.setValue(chips);
    chipper.addItem(box);
    Box<Drink> waterBox = new Box<>("crate", "A crate of water", false, true);
    waterBox.setValue(water);
    chipper.addItem(waterBox);
    menu.add(water);
    burger = new Food("burger", "A juicy beef burger.", true, true, 6);
    menu.add(burger);
    kebab = new Food("kebab", "A lovely doner kebab.", true, true, 7);
    menu.add(kebab);
  }

  public void createNPCs() {
    bartender = new NPC(
      "John the Bartender",
      pub,
      List.of(
        "That'll be 5 euro, please " + playerName + ".",
        "Sláinte! Enjoy your pint!"
      ),
      List.of("Well bud, how are things?", "Can I get you a pint?")
    );
    bar.addNpc(bartender);

    lad = new NPC(
      "Bob",
      pub,
      List.of(
        "You'll surely stay out for a while tonight, you've been under that woman's thumb for weeks now",
        "United are playing like!",
        "Anyway, go get yourself a pint there!",
        "Fancy a game of dice? Type 'play dice' to challenge me!",
        "Maybe some other time",
        "Better luck next time!"
      ),
      List.of(
        "Well lad, what's the craic?",
        "Thank god it's Friday that's for sure!"
      )
    );
    pub.addNpc(lad);

    chipperOwner = new NPC(
      "Mario",
      chipper,
      List.of(
        "That'll be 7 euro for the curry cheese chips, my friend.",
        "Enjoy your meal!",
        "Anything else my friend?"
      ),
      List.of(
        "Hello my friend!, Welcome to Mario's Chipper!",
        "Is it really Friday already? The usual I suppose?"
      )
    );
    chipperCounter.addNpc(chipperOwner);

    billy = new NPC("Billy", alleyway, List.of("..."), List.of("..."));
    alleyway.addNpc(billy);

    cabDriver = new NPC(
      "Steve the Cab Driver",
      cab,
      List.of("Hop in mate, I'll get you home safe and sound."),
      List.of("Hello there!")
    );
    cab.addNpc(cabDriver);

    wife = new NPC(
      "Your Wife",
      house,
      List.of("Oh thank you!"),
      List.of("Well look who it is. How are you?")
    );
    house.addNpc(wife);
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
    System.out.println("Welcome " + playerName + ", to The Usual!");
    System.out.println(
      "It's a Friday evening and you are standing outside one of your favourite places on earth - the pub"
    );
    System.out.println("Type 'help' if you need help.");
    System.out.println();
    System.out.println(player.getCurrentRoom().getLongDescription());
  }

  private boolean processCommand(Command command) {
    CommandType commandType = command.getCommandWord();

    if (commandType == CommandType.UNKNOWN) {
      System.out.println("I don't understand your command...");
      return false;
    }

    switch (commandType) {
      case HELP:
        printHelp();
        break;
      case GO, MOVE:
        goRoom(command);
        break;
      case QUIT:
        if (command.hasSecondWord()) {
          System.out.println("Quit what?");
          return false;
        } else {
          return true; // signal to quit
        }
      case LOOK:
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
        if (player.getCurrentRoom().equals(chipper)) {
          System.out.println("Menu:");
          for (PurchasableItem menuItem : menu) {
            System.out.println(
              "- " + menuItem.getName() + ": " + menuItem.getPrice() + " euro"
            );
          }
        }
        break;
      case COLLECT:
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
      case INVENTORY:
        player.showInventory();
        break;
      case CHECK:
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
          } else if (checkWhat.equalsIgnoreCase("time")) {
            System.out.println(
              "Current time: " + player.getCurrentTimeFormatted()
            );
          } else {
            System.out.println("You can't check that!");
          }
        }
        break;
      case TALK:
        if (player.getCurrentRoom().getNpcsInRoom().isEmpty()) {
          System.out.println("There is no one here to talk to.");
        } else {
          NPC npc = player.getCurrentRoom().getNpcsInRoom().get(0);
          System.out.println("You talk to " + npc.getName() + ":");
          npc.welcomePlayer();
          player.incrementTime(4);
          String response = parser.getInput();

          if (npc.getName().equals("John the Bartender")) {
            if ("yes".equalsIgnoreCase(response)) {
              npc.talk(0, 0);
              if (player.getMoney() >= 5) {
                player.removeMoney(5);
                player.addItemToInventory(pint);
                npc.talk(1, 1);
              } else {
                System.out.println(
                  "You don't have enough money to buy a pint."
                );
              }
            } else if ("no".equalsIgnoreCase(response)) {
              System.out.println("Maybe next time!");
            } else {
              System.out.println("I don't understand your response.");
            }
          } else if (npc.getName().equals("Mario")) {
            if ("yes".equalsIgnoreCase(response)) {
              System.out.println(npc.getDialogueLines().get(0));
              if (player.getMoney() >= chips.getPrice()) {
                player.removeMoney(chips.getPrice());
                player.addItemToInventory(chips);
                npc.talk(1, 3);
                String response2 = parser.getInput();
                if ("no".equalsIgnoreCase(response2)) {
                  System.out.println("\"See you around so!\"");
                } else if (!tryPurchaseFromMenu(response2, player, npc)) {
                  System.out.println("\"I don't understand your response.\"");
                }
              } else {
                System.out.println(
                  "\"You don't have enough money to buy chips.\""
                );
              }
            } else if ("no".equalsIgnoreCase(response)) {
              System.out.println("\"Maybe next time!\"");
              npc.talk(2, 3);
              String reply = parser.getInput();
              if ("no".equalsIgnoreCase(reply)) {
                System.out.println("\"See you around so!\"");
              } else if (!tryPurchaseFromMenu(reply, player, npc)) {
                System.out.println("\"I don't understand your response.\"");
              }
            }
          } else if (npc.getName().equals("Bob")) {
            npc.talk(0, 4);
          } else if (npc.getName().equals("Steve the Cab Driver")) {
            npc.talk(0, 0);
            System.out.println("Do you want to go home? (yes/no)");
            String cabResponse = parser.getInput();
            if ("yes".equalsIgnoreCase(cabResponse)) {
              System.out.println("You get into the cab.");
              player.setCurrentRoom(outsideHouse);
              player.incrementTime(15);
              System.out.println("The cab drops you off outside your house.");
              System.out.println(player.getCurrentRoom().getLongDescription());
            } else if ("no".equalsIgnoreCase(cabResponse)) {
              System.out.println("You decide to stay out a bit longer.");
            } else {
              System.out.println("I don't understand your response.");
            }
          }
        }
        break;
      case DROP:
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
      case DRINK:
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
          System.out.println("You don't have that.");
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
          player.incrementTime(5);
        } else {
          System.out.println("You drank " + itemToDrink.getName() + ".");
          player.decrementPintCount();
          player.incrementTime(2);
        }

        // Remove the item after drinking
        player.removeItemFromInventory(itemToDrink);
        break;
      case SAVE:
        player.savePlayerState();
        System.out.println("Game saved successfully.");
        break;
      case EAT:
        if (!command.hasSecondWord()) {
          System.out.println("Eat what?");
          break;
        }

        String meal = command.getSecondWord();
        Item itemToEat = null;

        // Check if player has the item at all
        boolean hasGrub = false;
        for (Item item : player.getInventory()) {
          if (item.getName().equalsIgnoreCase(meal)) {
            hasGrub = true;
            // If it’s a food, mark it as eatable
            if (item instanceof Food) {
              itemToEat = item;
            }
            break;
          }
        }

        // Now respond based on what we found
        if (!hasGrub) {
          System.out.println("You don't have that.");
          break;
        }

        if (itemToEat == null) {
          System.out.println("You can't eat that!");
          break;
        }
        // Handle special case: possible future food types
        //if (itemToEat.getName().equalsIgnoreCase("pint")) {
        //System.out.println("You drank a pint.");
        //player.removeHealth(5);
        //player.incrementPintCount();
        else {
          System.out.println("You ate " + itemToEat.getName() + ".");
          player.addHealth(5);
          player.incrementTime(3);
        }

        // Remove the item after eating
        player.removeItemFromInventory(itemToEat);
        break;
      case OPEN:
        if (!command.hasSecondWord()) {
          System.out.println("Open what?");
          break;
        }

        String boxName = command.getSecondWord();
        Item boxToOpen = null;

        for (Item item : player.currentRoom.getItemsInRoom()) {
          if (item.getName().equalsIgnoreCase(boxName) && item instanceof Box) {
            boxToOpen = item;
            break;
          }
        }

        if (boxToOpen == null) {
          System.out.println("There is no such box here.");
        } else {
          Box<?> boxItem = (Box<?>) boxToOpen;
          System.out.println(
            "You opened the " +
            boxItem.getName() +
            " and found: " +
            boxItem.getValue().getDescription()
          );
          player.addItemToInventory(boxItem.getValue());
          player.currentRoom.removeItem(boxItem);
        }

        break;
      case PLAY:
        if (!player.getCurrentRoom().equals(pub)) {
          System.out.println("You can only play the dice game in the pub.");
        } else if (!command.hasSecondWord()) {
          System.out.println("Play what?");
        } else {
          String gameToPlay = command.getSecondWord();
          if (gameToPlay.equalsIgnoreCase("dice")) {
            playDiceGame(pub.getNpcsInRoom().get(0));
          } else {
            System.out.println("Don't know that game.");
          }
        }
        break;
      case KNOCK:
        if (player.currentRoom != outsideHouse) {
          System.out.println("There is nothing to knock here.");
        } else {
          System.out.println("You knock on the door...");
          player.incrementTime(1);
          player.setHasKnocked(true);
          System.out.println("Your wife opens the door and greets you.");
          wife.welcomePlayer();
          player.setCurrentRoom(house);
          ending();
          System.exit(0);
        }
        break;
      case UNKNOWN:
        System.out.println("I don't understand your command...");
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
    } else if (player.getCurrentRoom() == cab) {
      System.out.println(
        "You are outside a cab. If you want to go home you should talk to the taxi driver."
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

    Direction dir = Direction.fromString(direction);

    if (dir == Direction.UNKNOWN) {
      System.out.println("That's not a valid direction!");
      System.out.println("Try: north, south, east, west, order, or back");
      return;
    }

    Room nextRoom = player.getCurrentRoom().getExit(dir);
    Room previousRoom = player.getCurrentRoom();

    if (nextRoom == null) {
      System.out.println("There is no door!");
    } else {
      if (previousRoom == outsideHouse && nextRoom == house) {
        if (nextRoom.isLocked()) {
          System.out.println("The door is locked!");
          if (!player.hasKey()) {
            System.out.println(
              "You must've dropped your key! Maybe try to \"knock\"?"
            );
            return;
          } else {
            System.out.println("You quietly unlock the door with your key...");
            player.setCurrentRoom(nextRoom);
            System.out.println(
              player.getCurrentRoom().getLocationDescription()
            );
            System.out.println(player.getCurrentRoom().getLongDescription());

            ending();
            System.exit(0);
            return;
          }
        } else {
          player.setCurrentRoom(nextRoom);
          System.out.println(player.getCurrentRoom().getLocationDescription());
          System.out.println(player.getCurrentRoom().getLongDescription());

          ending();
          System.exit(0);
          return;
        }
      }

      player.setCurrentRoom(nextRoom);
      System.out.println(player.getCurrentRoom().getLocationDescription());
      System.out.println(player.getCurrentRoom().getLongDescription());

      if (nextRoom == alleyway && !billyEncountered) {
        billyFight();
        billyEncountered = true;
      }

      if (isMajorLocation(previousRoom) && isMajorLocation(nextRoom)) {
        player.incrementTime(10);
      }
    }
  }

  private boolean tryPurchaseFromMenu(String itemName, Player player, NPC npc) {
    for (Item menuItem : menu) {
      if (menuItem.getName().equalsIgnoreCase(itemName)) {
        double price = 0;
        if (menuItem instanceof Food) {
          price = ((Food) menuItem).getPrice();
        } else if (menuItem instanceof Drink) {
          price = ((Drink) menuItem).getPrice();
        }

        if (player.getMoney() >= price) {
          player.removeMoney((int) price);
          player.addItemToInventory(menuItem);
          System.out.println("\"" + npc.getDialogueLines().get(1) + "\"");
          return true;
        } else {
          System.out.println(
            "You don't have enough money to buy a " + menuItem.getName() + "."
          );
          return true;
        }
      }
    }
    return false; // Item not found in menu
  }

  private void playDiceGame(NPC bob) {
    System.out.println("\n" + "=".repeat(50));
    System.out.println("DICE GAME with Bob");
    System.out.println("=".repeat(50));
    System.out.println("Rules: Highest roll wins. You both roll 2 dice.");
    System.out.println(
      "Bet: " + diceGame.getMinBet() + " to " + diceGame.getMaxBet()
    );
    System.out.println("Your money: " + player.getMoney());

    boolean validBet = false;
    int result = 0;

    while (!validBet) {
      System.out.println(
        "\nHow much do you want to bet? (or 'quit' to cancel)"
      );
      String betInput = parser.getInput().trim();

      if (
        betInput.equalsIgnoreCase("quit") || betInput.equalsIgnoreCase("cancel")
      ) {
        System.out.println("Bob: " + bob.getDialogueLines().get(3));
        return;
      }

      try {
        int betAmount = Integer.parseInt(betInput);
        result = diceGame.playRound(betAmount, player.getMoney());

        if (result != -999) {
          validBet = true;
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid bet amount! Please enter a number.");
      }
    }

    // Process the result after valid bet
    if (result > 0) {
      player.addMoney(result);
      System.out.println("You win! You gain " + result + ".");
    } else if (result < 0) {
      player.removeMoney(Math.abs(result));
      System.out.println("Bob: " + bob.getDialogueLines().get(4));
      System.out.println("You lose " + result + ".");
    } else if (result == 0) {
      System.out.println("It's a draw! No money won or lost.");
    }

    System.out.println("Your current money: " + player.getMoney());
    player.incrementTime(15);
    System.out.println("\nType 'play dice' to play again!");
    System.out.println("=".repeat(50) + "\n");
  }

  private boolean isMajorLocation(Room room) {
    return (
      room == outside ||
      room == chipper ||
      room == pub ||
      room == cab ||
      room == house
    );
  }

  private void billyFight() {
    System.out.println("\n" + "=".repeat(50));
    System.out.println("A shadowy figure steps out from the darkness...");
    System.out.println("It's Billy! He's blocking your path!");
    System.out.println("=".repeat(50));

    player.incrementTime(2);

    Weapon weapon = player.getBestWeapon();

    if (weapon != null) {
      // Player has a weapon - reduced robbery
      System.out.println("\nYou pull out your " + weapon.getName() + "!");
      if (weapon.equals(bottle)) {
        System.out.println("You smash the bottle off the wall!");
      }
      System.out.println("Billy eyes it nervously...");

      int damage = weapon.getDamage();
      int moneyStolen;
      int itemsToDrop;

      if (damage >= 15) {
        moneyStolen = 10;
        itemsToDrop = 2;
        System.out.println(
          "\"Alright, alright! Just give me a bit and I'll leave you alone!\""
        );
      } else {
        moneyStolen = 20;
        itemsToDrop = 3;
        System.out.println(
          "\"That's not gonna stop me, but I'll go easy on ya!\""
        );
      }

      int actualStolen = Math.min(player.getMoney(), moneyStolen);
      if (actualStolen > 0) {
        System.out.println(
          "\nBilly snatches " + actualStolen + " from your wallet!"
        );
        player.removeMoney(actualStolen);
      }

      player.dropRandomItems(itemsToDrop, alleyway);
      System.out.println(
        "Billy grabs some of your items and tosses them on the ground!"
      );
      System.out.println(
        "\"Consider yourself lucky!\" Billy disappears into the shadows."
      );
    } else {
      // No weapon
      System.out.println("\nYou have nothing to defend yourself with!");
      System.out.println("Billy shoves you against the wall...");

      int moneyStolen = Math.min(player.getMoney(), 30);
      if (moneyStolen > 0) {
        System.out.println(
          "\nBilly empties your wallet! " + moneyStolen + " stolen!"
        );
        player.removeMoney(moneyStolen);
      }

      // Drop all items
      int itemCount = player.getInventory().size();
      if (itemCount > 0) {
        player.dropRandomItems(itemCount, alleyway);
        System.out.println(
          "Billy rummages through your pockets and throws everything on the ground!"
        );
      }

      System.out.println(
        "\n\"That'll teach ya to wander into my alley unarmed!\""
      );
      System.out.println("Billy runs off laughing...");
    }

    System.out.println(
      "\nYou dust yourself off. Your items are scattered on the ground here."
    );
    System.out.println("=".repeat(50) + "\n");

    alleyway.removeNpc(billy);
  }

  private void ending() {
    System.out.println("\n" + "=".repeat(50));
    System.out.println("You encounter Herself...");
    System.out.println("=".repeat(50));

    boolean hasFood = false;
    for (Item item : player.getInventory()) {
      if (item instanceof Food && item != kebab) {
        hasFood = true;
        break;
      }
    }

    boolean hasKey = false;
    for (Item item : player.getInventory()) {
      if (item.getName().equalsIgnoreCase("key")) {
        hasKey = true;
        break;
      }
    }

    WifeRequirements wifeReq = new WifeRequirements(
      hasFood,
      hasKey,
      player.hasKnocked(),
      player.getPintCount(),
      player.getMoney(),
      player.getCurrentTime(),
      player.getHealth()
    );

    System.out.println(wifeReq.getScoreBreakdown());
    System.out.println(wifeReq.getEndingMessage());

    System.out.println("=".repeat(50) + "\n");
    if (wifeReq.isWifeHappy()) {
      System.out.println(
        "Congratulations! You have successfully pleased your wife and avoided total war."
      );
    } else {
      System.out.println(
        "Oh no! Your wife is not happy. Better find a pillow and blanket for the couch tonight."
      );
    }

    System.out.println("=".repeat(50));
    System.out.println("\n=== GAME OVER ===");
    System.out.println("Thanks for playing The Usual!");
    System.out.println("\nFinal Stats:");
    System.out.println("- Pints drunk: " + player.getPintCount());
    System.out.println("- Money left: " + player.getMoney());
    System.out.println("- Health: " + player.getHealth());
    System.out.println(
      "- Time arrived home: " + player.getCurrentTimeFormatted()
    );
  }

  public static void main(String[] args) {
    ZorkULGame game = new ZorkULGame();
    game.play();
  }
}
