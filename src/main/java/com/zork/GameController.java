package com.zork;

import java.util.ArrayList;
import java.util.List;

public class GameController {

  private GameView view;
  private Parser parser;
  private Player player;
  private String gameChoice = "";
  private String playerName = "";
  private Room outside, chipper, pub, cab, house, alleyway, bathroomPub, bathroomChipper, outsideHouse, bar, chipperCounter;
  private Drink pint, barPint, water;
  private Food chips, burger, kebab, counterChips;
  private Weapon bottle, hurl;
  private ArrayList<PurchasableItem> menu = new ArrayList<>();
  private DiceGame diceGame;
  private NPC bartender, lad, chipperOwner, billy, cabDriver, wife;
  private boolean gameOver = false;
  private boolean warningPlayed = false;

  public GameController(GameView view) {
    this.view = view;
    this.parser = new Parser();
    this.diceGame = new DiceGame();

    createRooms();
    createNPCs();
    createItems();
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
      "in Mario's Chipper",
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
      "outside a cab",
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
    // Player will be initialized in startGame()
  }

  private void createItems() {
    pint = new Drink("pint", "A refreshing pint of beer.", true, true, 5);
    barPint = new Drink(
      "pint",
      "A refreshing foamy pint of beer on the bar",
      false,
      true,
      0
    );
    bar.addItem(barPint);
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
    chips = new Food("chips", "A delicious serving of chips.", true, true, 7);
    counterChips = new Food(
      "chips",
      "A delicious serving of chips on the counter.",
      false,
      false,
      0
    );
    chipperCounter.addItem(counterChips);
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

  private void createNPCs() {
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
      List.of(
        "Hop in mate, I'll get you home safe and sound.",
        "That'll be a tenner so bud"
      ),
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

  public void startGame() {
    // For console mode, create a default player if none exists
    if (player == null && view instanceof ConsoleView) {
      view.displayMessage("Welcome to The Usual!");
      view.displayMessage(
        "Would you like to start a new game or load a saved game?"
      );
      view.displayMessage(
        "Type 'new' for a new game or 'load' to load a saved game:"
      );
      String choice = view.getUserInput().trim().toLowerCase();

      if (choice.equals("load")) {
        Player loadedPlayer = Player.loadPlayerState();
        if (loadedPlayer != null) {
          setPlayer(loadedPlayer);
          displayGameStart();
          view.displayMessage("Game loaded successfully!");
        } else {
          view.displayMessage("No saved game found. Starting a new game...");
          view.displayMessage("What is your name?");
          String name = view.getUserInput();
          createNewPlayer(name);
        }
      } else {
        view.displayMessage("What is your name?");
        String name = view.getUserInput();
        createNewPlayer(name);
      }
    }
    // For GUI mode, initialization is handled through createNewPlayer or loadGame
  }

  public void createNewPlayer(String playerName) {
    this.playerName = playerName;
    this.player = new Player(
      playerName,
      outside,
      100,
      100,
      0,
      (18 * 60),
      100,
      false
    );
    displayGameStart();
  }

  public void setPlayer(Player player) {
    this.player = player;
    this.playerName = player.getName();
  }

  public Player getPlayer() {
    return player;
  }

  public Room getOutsideRoom() {
    return outside;
  }

  public void setPlayerRoom(Room room) {
    if (player != null) {
      player.setCurrentRoom(room);
    }
  }

  public void displayGameStart() {
    view.displayWelcome(playerName);
    view.displayRoom(player.getCurrentRoom(), null);
    if (view instanceof GuiView) {
      ((GuiView) view).updateInventoryDisplay(player.getInventory());
    } else {
      view.displayInventory(player.getInventory());
    }
    view.displayHealth(player.getHealth());
    view.displayMoney(player.getMoney());
    view.displayTime(player.getCurrentTimeFormatted());
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public Command getCommand() {
    return parser.getCommand();
  }

  public void processCommand(Command command) {
    CommandType commandType = command.getCommandWord();

    if (commandType == CommandType.UNKNOWN) {
      view.displayMessage("I don't understand your command...");
      return;
    }

    switch (commandType) {
      case HELP:
        handleHelp();
        break;
      case GO, MOVE:
        handleGo(command);
        break;
      case QUIT:
        handleQuit(command);
        break;
      case LOOK:
        handleLook();
        break;
      case COLLECT:
        handleCollect(command);
        break;
      case INVENTORY:
        handleInventory();
        break;
      case CHECK:
        handleCheck(command);
        break;
      case TALK:
        handleTalk();
        break;
      case DROP:
        handleDrop(command);
        break;
      case DRINK:
        handleDrink(command);
        break;
      case EAT:
        handleEat(command);
        break;
      case SAVE:
        handleSave();
        break;
      case OPEN:
        handleOpen(command);
        break;
      case PLAY:
        handlePlay(command);
        break;
      case KNOCK:
        handleKnock();
        break;
      case UNKNOWN:
        view.displayMessage("I don't know what you mean...");
        break;
    }
  }

  private void handleHelp() {
    view.displayHelp(player.getCurrentRoom());
  }

  private void handleQuit(Command command) {
    if (command.hasSecondWord()) {
      view.displayMessage("Quit what?");
    } else {
      gameOver = true;
      view.displayMessage("Thank you for playing. Goodbye.");
      System.exit(0);
    }
  }

  private void handleLook() {
    Room currentRoom = player.getCurrentRoom();
    view.displayMessage(currentRoom.getLocationDescription());
    view.displayMessage(currentRoom.getLongDescription());

    if (currentRoom.getItemsInRoom().isEmpty()) {
      view.displayMessage("There are no items here.");
    } else {
      view.displayMessage("You see the following items:");
      for (Item item : currentRoom.getItemsInRoom()) {
        view.displayMessage("- " + item.getDescription());
      }
    }

    if (currentRoom.getNpcsInRoom().isEmpty()) {
      view.displayMessage("There is no one to talk to in here");
    } else {
      view.displayMessage(
        "You see " + currentRoom.getNpcsInRoom().get(0).getName()
      );
    }

    if (player.getCurrentRoom().equals(chipper)) {
      view.displayMessage("Menu:");
      for (PurchasableItem menuItem : menu) {
        view.displayMessage(
          "- " + menuItem.getName() + ": " + menuItem.getPrice() + " euro"
        );
      }
    }
  }

  private void handleCollect(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Collect what?");
      return;
    }

    String itemName = command.getSecondWord();
    Item itemToPick = null;

    for (Item item : player.getCurrentRoom().getItemsInRoom()) {
      if (item.getName().equalsIgnoreCase(itemName)) {
        itemToPick = item;
        break;
      }
    }

    if (itemToPick == null) {
      view.displayNoSuchItem();
    } else if (!itemToPick.isCollectible()) {
      view.displayItemNotCollectible();
    } else {
      player.pickUpItem(itemToPick);
      view.displayItemCollected(itemToPick.getName());
      if (view instanceof GuiView) {
        ((GuiView) view).updateInventoryDisplay(player.getInventory());
        // Update room image only for chipper (hurl changes the image)
        if (player.getCurrentRoom().equals(chipper)) {
          ((GuiView) view).updateRoomImage(player.getCurrentRoom());
        }
      }
    }
  }

  private void handleInventory() {
    List<Item> inventory = player.showInventory();
    view.displayInventory(inventory);
  }

  private void handleCheck(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Check what?");
      return;
    }

    String checkWhat = command.getSecondWord();
    if (checkWhat.equalsIgnoreCase("wallet")) {
      view.displayMoney(player.getMoney());
    } else if (checkWhat.equalsIgnoreCase("health")) {
      view.displayHealth(player.getHealth());
    } else if (checkWhat.equalsIgnoreCase("time")) {
      if (view instanceof ConsoleView) {
        view.displayMessage(
          "Current time: " + player.getCurrentTimeFormatted()
        );
      } else {
        view.displayTime(player.getCurrentTimeFormatted());
      }
    } else {
      view.displayMessage("You can't check that!");
    }
  }

  private void handleDrop(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Drop what?");
      return;
    }

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
      view.displayItemDropped(itemToDrop.getName());
      if (view instanceof GuiView) {
        ((GuiView) view).updateInventoryDisplay(player.getInventory());
        // Update room image only for chipper (hurl changes the image)
        if (player.getCurrentRoom().equals(chipper)) {
          ((GuiView) view).updateRoomImage(player.getCurrentRoom());
        }
      }
    } else {
      view.displayNotInInventory();
    }
  }

  // Placeholder methods for remaining commands - will be implemented in next segments
  private void handleTalk() {
    if (player.getCurrentRoom().getNpcsInRoom().isEmpty()) {
      view.displayNoNPCHere();
      return;
    }

    NPC npc = player.getCurrentRoom().getNpcsInRoom().get(0);
    view.displayMessage("You talk to " + npc.getName() + ":");

    // Display welcome lines
    for (String line : npc.getWelcomeLines()) {
      view.displayNPCDialogue(npc.getName(), line);
    }

    player.incrementTime(8);
    view.displayTime(player.getCurrentTimeFormatted());
    checkTimeBasedAudio();
    checkTimeBasedAudio();

    if (npc.getName().equals("John the Bartender")) {
      if (view instanceof GuiView) {
        ((GuiView) view).requestUserInput("", response ->
            handleBartenderConversation(npc, response)
          );
      } else {
        String response = view.getUserInput();
        handleBartenderConversation(npc, response);
      }
    } else if (npc.getName().equals("Mario")) {
      if (view instanceof GuiView) {
        ((GuiView) view).requestUserInput("", response ->
            handleMarioConversation(npc, response)
          );
      } else {
        String response = view.getUserInput();
        handleMarioConversation(npc, response);
      }
    } else if (npc.getName().equals("Bob")) {
      // Bob just talks, doesn't expect a response
      for (String line : npc.getDialogueLines().subList(0, 4)) {
        view.displayNPCDialogue(npc.getName(), line);
      }
    } else if (npc.getName().equals("Steve the Cab Driver")) {
      handleCabDriverConversation(npc);
    }
  }

  private void handleBartenderConversation(NPC npc, String response) {
    if ("yes".equalsIgnoreCase(response)) {
      view.displayMessage("\"" + npc.getDialogueLines().get(0) + "\"");
      if (player.getMoney() >= 5) {
        player.removeMoney(5);
        view.displayMoney(player.getMoney());
        player.addItemToInventory(pint);
        view.displayMessage("\"" + npc.getDialogueLines().get(1) + "\"");
        if (view instanceof GuiView) {
          ((GuiView) view).updateInventoryDisplay(player.getInventory());
        }
      } else {
        view.displayMessage("You don't have enough money for a pint.");
      }
    } else if ("no".equalsIgnoreCase(response)) {
      view.displayMaybeNextTime();
    } else {
      view.displayUnknownResponse();
    }
  }

  private void handleMarioConversation(NPC npc, String response) {
    if ("yes".equalsIgnoreCase(response)) {
      view.displayMessage(npc.getDialogueLines().get(0));
      if (player.getMoney() >= chips.getPrice()) {
        player.removeMoney(chips.getPrice());
        view.displayMoney(player.getMoney());
        player.addItemToInventory(chips);
        if (view instanceof GuiView) {
          ((GuiView) view).updateInventoryDisplay(player.getInventory());
        }
        view.displayMessage("\"" + npc.getDialogueLines().get(1) + "\"");
        view.displayMessage("\"" + npc.getDialogueLines().get(2) + "\"");

        if (view instanceof GuiView) {
          ((GuiView) view).requestUserInput("", response2 ->
              handleMarioSecondResponse(response2)
            );
        } else {
          String response2 = view.getUserInput();
          handleMarioSecondResponse(response2);
        }
      } else {
        view.displayMessage("\"You don't have enough money to buy chips.\"");
      }
    } else if ("no".equalsIgnoreCase(response)) {
      view.displayMaybeNextTime();
      view.displayMessage("\"" + npc.getDialogueLines().get(2) + "\"");

      if (view instanceof GuiView) {
        ((GuiView) view).requestUserInput("", reply ->
            handleMarioSecondResponse(reply)
          );
      } else {
        String reply = view.getUserInput();
        handleMarioSecondResponse(reply);
      }
    }
  }

  private void handleMarioSecondResponse(String response) {
    if ("no".equalsIgnoreCase(response)) {
      view.displayGoodbye();
    } else if (!tryPurchaseFromMenu(response)) {
      view.displayUnknownResponse();
    }
  }

  private void handleCabDriverConversation(NPC npc) {
    for (String line : npc.getDialogueLines().subList(0, 1)) {
      view.displayNPCDialogue(npc.getName(), line);
    }
    view.displayCabPrompt();

    if (view instanceof GuiView) {
      ((GuiView) view).requestUserInput("", cabResponse ->
          processCabResponse(cabResponse)
        );
    } else {
      String cabResponse = view.getUserInput();
      processCabResponse(cabResponse);
    }
  }

  private void processCabResponse(String cabResponse) {
    if ("yes".equalsIgnoreCase(cabResponse)) {
      if (player.getMoney() >= 10) {
        view.displayMessage("\"" + cabDriver.getDialogueLines().get(1) + "\"");
        player.removeMoney(10);
        view.displayMoney(player.getMoney());
        view.displayCabRide();
        player.setCurrentRoom(outsideHouse);
        player.incrementTime(20);
        view.displayTime(player.getCurrentTimeFormatted());
        checkTimeBasedAudio();
        view.displayRoom(player.getCurrentRoom(), null);
      } else {
        view.displayMessage(
          "Ah here, you're lucky I'm in a good mood tonight."
        );
        view.displayCabRide();
        player.setCurrentRoom(outsideHouse);
        player.incrementTime(20);
        view.displayTime(player.getCurrentTimeFormatted());
        checkTimeBasedAudio();
        view.displayRoom(player.getCurrentRoom(), null);
      }
    } else if ("no".equalsIgnoreCase(cabResponse)) {
      view.displayStayOut();
    } else {
      view.displayUnknownResponse();
    }
  }

  private boolean tryPurchaseFromMenu(String itemName) {
    for (Item menuItem : menu) {
      if (menuItem.getName().equalsIgnoreCase(itemName)) {
        int price = 0;
        if (menuItem instanceof Food) {
          price = ((Food) menuItem).getPrice();
        } else if (menuItem instanceof Drink) {
          price = ((Drink) menuItem).getPrice();
        }

        if (player.getMoney() >= price) {
          player.removeMoney((int) price);
          view.displayMoney(player.getMoney());
          player.addItemToInventory(menuItem);
          view.displayMessage(
            "\"That'll be " +
            price +
            " euro for the " +
            menuItem.getName() +
            ", my friend.\""
          );
          if (view instanceof GuiView) {
            ((GuiView) view).updateInventoryDisplay(player.getInventory());
          }
          return true;
        } else {
          view.displayMessage(
            "You don't have enough money to buy a " + menuItem.getName() + "."
          );
          return true;
        }
      }
    }
    return false; // Item not found in menu
  }

  private void handleDrink(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Drink what?");
      return;
    }

    String beverage = command.getSecondWord();
    Item itemToDrink = null;

    // Check if player has the item at all
    boolean hasItem = false;
    for (Item item : player.getInventory()) {
      if (item.getName().equalsIgnoreCase(beverage)) {
        hasItem = true;
        // If it's a drink, mark it as drinkable
        if (item instanceof Drink) {
          itemToDrink = item;
        }
        break;
      }
    }

    // Now respond based on what we found
    if (!hasItem) {
      view.displayNoItemToConsume();
      return;
    }

    if (itemToDrink == null) {
      view.displayCannotConsume();
      return;
    }

    // Handle special case: pint
    if (itemToDrink.getName().equalsIgnoreCase("pint")) {
      player.removeHealth(5);
      int newHealth = player.getHealth();
      player.incrementPintCount();
      player.incrementTime(10);

      view.displayDrinkPint(5, newHealth);
      view.displayTime(player.getCurrentTimeFormatted());
      checkTimeBasedAudio();
    } else {
      view.displayDrinkWater();
      player.incrementTime(5);
      view.displayTime(player.getCurrentTimeFormatted());
      checkTimeBasedAudio();
    }

    // Remove the item after drinking
    player.removeItemFromInventory(itemToDrink);
    if (view instanceof GuiView) {
      ((GuiView) view).updateInventoryDisplay(player.getInventory());
    }
  }

  private void handleEat(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Eat what?");
      return;
    }

    String meal = command.getSecondWord();
    Item itemToEat = null;

    // Check if player has the item at all
    boolean hasGrub = false;
    for (Item item : player.getInventory()) {
      if (item.getName().equalsIgnoreCase(meal)) {
        hasGrub = true;
        // If it's a food, mark it as eatable
        if (item instanceof Food) {
          itemToEat = item;
        }
        break;
      }
    }

    // Now respond based on what we found
    if (!hasGrub) {
      view.displayNoItemToConsume();
      return;
    }

    if (itemToEat == null) {
      view.displayCannotEat();
      return;
    }

    // Eat the food
    player.addHealth(5);
    int newHealth = player.getHealth();
    player.incrementTime(8);

    view.displayEatFood(itemToEat.getName(), 5, newHealth);
    view.displayTime(player.getCurrentTimeFormatted());
    checkTimeBasedAudio();

    // Remove the item after eating
    player.removeItemFromInventory(itemToEat);
    if (view instanceof GuiView) {
      ((GuiView) view).updateInventoryDisplay(player.getInventory());
    }
  }

  private void handleSave() {
    player.savePlayerState();
    view.displaySaveSuccess();
  }

  private void handleOpen(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Open what?");
      return;
    }

    String boxName = command.getSecondWord();
    Item boxToOpen = null;

    for (Item item : player.getCurrentRoom().getItemsInRoom()) {
      if (item.getName().equalsIgnoreCase(boxName) && item instanceof Box) {
        boxToOpen = item;
        break;
      }
    }

    if (boxToOpen == null) {
      view.displayMessage("There is no such box here.");
    } else {
      Box<?> boxItem = (Box<?>) boxToOpen;
      view.displayBoxOpened(
        boxItem.getName(),
        boxItem.getValue().getDescription()
      );
      player.addItemToInventory(boxItem.getValue());
      player.getCurrentRoom().removeItem(boxItem);
      if (view instanceof GuiView) {
        ((GuiView) view).updateInventoryDisplay(player.getInventory());
      }
    }
  }

  private void handlePlay(Command command) {
    if (!player.getCurrentRoom().equals(pub)) {
      view.displayMessage("You can only play the dice game in the pub.");
      return;
    }

    if (!command.hasSecondWord()) {
      view.displayMessage("Play what?");
      return;
    }

    String gameToPlay = command.getSecondWord();
    if (gameToPlay.equalsIgnoreCase("dice")) {
      playDiceGame(lad); // Bob is the 'lad' NPC
    } else {
      view.displayUnknownGame();
    }
  }

  private void playDiceGame(NPC bob) {
    view.displayDiceGameIntro(
      diceGame.getMinBet(),
      diceGame.getMaxBet(),
      player.getMoney()
    );

    // Check if view is GuiView
    if (view instanceof GuiView) {
      // GUI mode - use callback
      ((GuiView) view).requestUserInput(
          "\nHow much do you want to bet? (or 'quit' to cancel)",
          betInput -> processDiceBet(bob, betInput)
        );
    } else {
      // Console mode - use blocking input
      String betInput = view.getUserInput(
        "\nHow much do you want to bet? (or 'quit' to cancel)"
      );
      processDiceBet(bob, betInput);
    }
  }

  private void processDiceBet(NPC bob, String betInput) {
    betInput = betInput.trim();

    if (
      betInput.equalsIgnoreCase("quit") || betInput.equalsIgnoreCase("cancel")
    ) {
      view.displayDiceGameQuit(bob.getDialogueLines().get(4));
      return;
    }

    try {
      int betAmount = Integer.parseInt(betInput);
      DiceGameResult result = diceGame.playRound(betAmount, player.getMoney());

      if (result == null) {
        view.displayMessage(
          "Bet must be between " +
          diceGame.getMinBet() +
          " and " +
          diceGame.getMaxBet() +
          ", and you must have enough money."
        );
        return;
      }

      view.displayDiceGameRolls(result.getPlayerRoll(), result.getBobRoll());

      if (result.isWin()) {
        int winAmount = result.getMoneyChange();
        player.addMoney(winAmount);
        view.displayMoney(player.getMoney());
        view.displayMessage("You win! You gain €" + winAmount + ".");
      } else if (result.isLoss()) {
        int lossAmount = Math.abs(result.getMoneyChange());
        player.removeMoney(lossAmount);
        view.displayMoney(player.getMoney());
        view.displayMessage("Bob: " + bob.getDialogueLines().get(5));
        view.displayMessage("You lose €" + lossAmount + ".");
      } else if (result.isTie()) {
        view.displayMessage("It's a draw! No money won or lost.");
      }

      view.displayMessage("Your current money: €" + player.getMoney());
      player.incrementTime(25);
      view.displayTime(player.getCurrentTimeFormatted());
      checkTimeBasedAudio();
    } catch (NumberFormatException e) {
      view.displayInvalidBet();
    }
  }

  private void handleKnock() {
    if (player.getCurrentRoom() != outsideHouse) {
      view.displayNothingToKnock();
      return;
    }

    view.displayKnock();
    player.incrementTime(3);
    view.displayTime(player.getCurrentTimeFormatted());
    checkTimeBasedAudio();
    player.setHasKnocked(true);
    player.setCurrentRoom(house);

    // Trigger ending
    handleEnding();
  }

  private void handleEnding() {
    // Check for food in inventory
    boolean hasFood = false;
    for (Item item : player.getInventory()) {
      if (item instanceof Food) {
        hasFood = true;
        break;
      }
    }

    // Check for key in inventory
    boolean hasKey = player.hasKey();

    // Create WifeRequirements and display ending
    WifeRequirements wifeReq = new WifeRequirements(
      hasFood,
      hasKey,
      player.hasKnocked(),
      player.getPintCount(),
      player.getMoney(),
      player.getCurrentTime(),
      player.getHealth()
    );

    view.displayEnding(wifeReq, player);
    gameOver = true;
  }

  private void handleGo(Command command) {
    if (!command.hasSecondWord()) {
      view.displayMessage("Go where?");
      return;
    }

    String direction = command.getSecondWord();

    Direction dir = Direction.fromString(direction);

    if (dir == Direction.UNKNOWN) {
      view.displayInvalidDirection();
      view.displayMessage("Try: north, south, east, west, order, or back");
      return;
    }

    Room nextRoom = player.getCurrentRoom().getExit(dir);
    Room previousRoom = player.getCurrentRoom();

    if (nextRoom == null) {
      view.displayNoExit();
    } else {
      // Special handling for house entry
      if (previousRoom == outsideHouse && nextRoom == house) {
        if (nextRoom.isLocked()) {
          view.displayDoorLocked();
          if (!player.hasKey()) {
            view.displayMessage(
              "You must've dropped your key! Maybe try to \"knock\"?"
            );
            return;
          } else {
            view.displayDoorUnlocked();
            player.setCurrentRoom(nextRoom);
            view.displayRoom(player.getCurrentRoom(), previousRoom);

            handleEnding();
            return;
          }
        } else {
          player.setCurrentRoom(nextRoom);
          view.displayRoom(player.getCurrentRoom(), previousRoom);

          handleEnding();
          return;
        }
      }

      // Normal room movement
      player.setCurrentRoom(nextRoom);
      view.displayRoom(player.getCurrentRoom(), previousRoom);

      if (nextRoom == alleyway && alleyway.getNpcsInRoom().contains(billy)) {
        billyFight();
      }

      // Time increment for major location changes
      if (isMajorLocation(previousRoom) && isMajorLocation(nextRoom)) {
        player.incrementTime(15);
        view.displayTime(player.getCurrentTimeFormatted());
        checkTimeBasedAudio();
      }
    }
  }

  private void billyFight() {
    view.displayBillyIntro();

    player.incrementTime(5);
    view.displayTime(player.getCurrentTimeFormatted());
    checkTimeBasedAudio();

    Weapon weapon = player.getBestWeapon();

    if (weapon != null) {
      // Player has a weapon - reduced robbery
      if (weapon.equals(bottle)) {
        view.displayBillyWeaponThreat(weapon.getName(), true);
      } else {
        view.displayBillyWeaponThreat(weapon.getName(), false);
      }

      int damage = weapon.getDamage();
      int moneyStolen;
      int itemsToDrop;
      int healthLoss;

      view.displayBillyWeaponResponse(damage);

      if (damage >= 15) {
        moneyStolen = 10;
        itemsToDrop = 2;
        healthLoss = 5;
      } else {
        moneyStolen = 20;
        itemsToDrop = 3;
        healthLoss = 10;
      }

      int actualStolen = Math.min(player.getMoney(), moneyStolen);
      if (actualStolen > 0) {
        view.displayBillyRobberyWithWeapon(actualStolen);
        player.removeMoney(actualStolen);
        view.displayMoney(player.getMoney());
      }

      // Remove health from player
      player.removeHealth(healthLoss);
      view.displayHealth(player.getHealth());
      view.displayHealthLost(healthLoss, player.getHealth());

      player.dropRandomItems(itemsToDrop, alleyway);
      if (view instanceof GuiView) {
        ((GuiView) view).updateInventoryDisplay(player.getInventory());
      }
    } else {
      // No weapon
      int moneyStolen = Math.min(player.getMoney(), 30);
      if (moneyStolen > 0) {
        view.displayBillyRobberyNoWeapon(moneyStolen);
        player.removeMoney(moneyStolen);
        view.displayMoney(player.getMoney());
      }

      // Remove health from player (more damage without weapon)
      int healthLoss = 15;
      player.removeHealth(healthLoss);
      view.displayHealth(player.getHealth());
      view.displayHealthLost(healthLoss, player.getHealth());

      // Drop all items
      int itemCount = player.getInventory().size();
      if (itemCount > 0) {
        player.dropRandomItems(itemCount, alleyway);
        if (view instanceof GuiView) {
          ((GuiView) view).updateInventoryDisplay(player.getInventory());
        }
      }
    }

    view.displayBillyEscape();

    alleyway.removeNpc(billy);
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

  // Check if it's 10pm and play warning audio
  private void checkTimeBasedAudio() {
    if (!warningPlayed && player.getHourOfDay() >= 22) {
      warningPlayed = true;
      if (view instanceof GuiView) {
        ((GuiView) view).playAudioFromController("/audio/smb_warning.wav");
      }
      view.displayMessage(
        "\n*** WARNING: It's getting late! Your wife expects you home soon! ***\n"
      );
    }
  }
}
