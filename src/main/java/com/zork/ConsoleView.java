package com.zork;

import java.util.List;
import java.util.Scanner;

public class ConsoleView implements GameView {

  private Scanner scanner;

  public ConsoleView() {
    scanner = new Scanner(System.in);
  }

  // === BASIC OUTPUT ===

  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  // === ROOM & WORLD ===

  @Override
  public void displayRoom(Room room) {
    System.out.println(room.getLocationDescription());
    System.out.println(room.getLongDescription());

    if (!room.getItemsInRoom().isEmpty()) {
      System.out.println("\nYou see the following items:");
      for (Item item : room.getItemsInRoom()) {
        System.out.println("- " + item.getDescription());
      }
    }
    if (!room.getNpcsInRoom().isEmpty()) {
      System.out.println("\nYou see " + room.getNpcsInRoom().get(0).getName());
    }
  }

  @Override
  public void displayMenu(List<PurchasableItem> menu) {
    System.out.println("Menu:");
    for (PurchasableItem item : menu) {
      System.out.println(
        "- " +
        item.getName() +
        ": " +
        item.getDescription() +
        " (Price: " +
        item.getPrice() +
        ")"
      );
    }
  }

  @Override
  public void displayWelcome(String playerName) {
    System.out.println();
    System.out.println("Welcome " + playerName + ", to The Usual!");
    System.out.println(
      "It's a Friday evening and you are standing outside one of your favourite places on earth - the pub"
    );
    System.out.println("Type 'help' if you need help.");
    System.out.println();
  }

  @Override
  public void displayHelp(Room currentRoom) {
    System.out.println("Available commands:");
    System.out.println("- go [direction]");
    System.out.println("- look");
    System.out.println("- collect [item]");
    System.out.println("- inventory");
    System.out.println("- check [item]");
    System.out.println("- talk [npc]");
    System.out.println("- drop [item]");
    System.out.println("- eat [item]");
    System.out.println("- drink [item]");
    System.out.println("- save");
    System.out.println("- open [item]");
    System.out.println("- move [item]");
    System.out.println("- play [item]");
    System.out.println("- knock [item]");
    System.out.println("- quit");

    if (currentRoom.getLocationDescription().contains("outside")) {
      System.out.println("You are outside, why don't you go to the pub :D");
    } else if (
      currentRoom.getLocationDescription().contains("Mario's Chipper")
    ) {
      System.out.println(
        "You are in Mario's Chipper, the best place for chips! Howeever, you can always go back to the pub after getting some grub."
      );
    } else if (
      currentRoom.getLocationDescription().contains("Corrigan's Bar")
    ) {
      System.out.println(
        "You are in Corrigan's Bar. Hooray! You should stay here for a good while and support your local"
      );
    } else if (currentRoom.getLocationDescription().contains("cab")) {
      System.out.println(
        "You are outside a cab. If you want to go home you should talk to the taxi driver."
      );
    } else if (currentRoom.getLocationDescription().contains("home")) {
      System.out.println(
        "You are finally home. You can go west outside the house or south to a bathroom."
      );
    } else if (currentRoom.getLocationDescription().contains("alleyway")) {
      System.out.println(
        "You are in a dark and creepy alleyway. You can go west to outside."
      );
    } else if (currentRoom.getLocationDescription().contains("bathroom")) {
      System.out.println("You are in a bathroom. You can go south to the pub.");
    } else if (currentRoom.getLocationDescription().contains("bathroom")) {
      System.out.println(
        "You are in a bathroom. You can go west to Mario's Chipper."
      );
    } else if (
      currentRoom.getLocationDescription().contains("outside the house")
    ) {
      System.out.println(
        "You are outside the house. You can go east to your house."
      );
    }
  }

  // === PLAYER STATS ===

  @Override
  public void displayInventory(List<Item> inventory) {
    if (inventory.isEmpty()) {
      System.out.println("Your inventory is empty.");
    }
    System.out.println("Your Inventory:");
    for (Item item : inventory) {
      System.out.println("- " + item.getName() + ": " + item.getDescription());
    }
  }

  @Override
  public void displayTime(String formattedTime) {
    System.out.println("Current time: " + formattedTime);
  }

  @Override
  public void displayMoney(int money) {
    System.out.println("You have " + money + " euro in your wallet.");
  }

  @Override
  public void displayHealth(int health) {
    System.out.println("You have " + health + " health.");
  }

  // === MOVEMENT ===

  @Override
  public void displayInvalidDirection() {
    System.out.println("That's not a valid direction!");
    System.out.println("Try: north, south, east, west, order, or back");
  }

  @Override
  public void displayNoExit() {
    System.out.println("There is no door!");
  }

  // === ITEMS ===

  @Override
  public void displayItemCollected(String itemName) {
    System.out.println("You picked up: " + itemName);
  }

  @Override
  public void displayItemDropped(String itemName) {
    System.out.println("You dropped: " + itemName);
  }

  @Override
  public void displayNoSuchItem() {
    System.out.println("There is no such item here.");
  }

  @Override
  public void displayItemNotCollectible() {
    System.out.println("You can't collect that item.");
  }

  @Override
  public void displayBoxOpened(String boxName, String contents) {
    System.out.println("You opened the " + boxName + " and found: " + contents);
  }

  // === CONSUMPTION ===

  @Override
  public void displayDrinkPint(int healthLoss, int newHealth) {
    System.out.println("You drank a pint.");
    System.out.println("Your health decreased by " + healthLoss + ".");
    System.out.println("You now have " + newHealth + " health.");
  }

  @Override
  public void displayDrinkWater() {
    System.out.println("You drank water.");
  }

  @Override
  public void displayEatFood(String foodName, int healthGain, int newHealth) {
    System.out.println("You ate " + foodName + ".");
    System.out.println("Your health increased by " + healthGain + ".");
    System.out.println("You now have " + newHealth + " health.");
  }

  @Override
  public void displayNoItemToConsume() {
    System.out.println("You don't have that.");
  }

  @Override
  public void displayCannotConsume() {
    System.out.println("You can't drink that!");
  }

  @Override
  public void displayCannotEat() {
    System.out.println("You can't eat that!");
  }

  // === NPC INTERACTIONS ===

  @Override
  public void displayNPCDialogue(String npcName, String dialogue) {
    System.out.println(npcName + ": \"" + dialogue + "\"");
  }

  @Override
  public void displayNoNPCHere() {
    System.out.println("There is no one here to talk to.");
  }

  @Override
  public void displayPurchaseSuccess(
    String itemName,
    int price,
    int remainingMoney
  ) {
    System.out.println(
      "You purchased " + itemName + " for " + price + " euro."
    );
    System.out.println("Remaining money: " + remainingMoney + " euro");
  }

  @Override
  public void displayPurchaseFailure(String reason) {
    System.out.println(reason);
  }

  @Override
  public void displayCabRide() {
    System.out.println("You get into the cab.");
    System.out.println("The cab drops you off outside your house.");
  }

  // === DICE GAME ===

  @Override
  public void displayDiceGameIntro(int minBet, int maxBet, int playerMoney) {
    System.out.println("\n" + "=".repeat(50));
    System.out.println("DICE GAME with Bob");
    System.out.println("=".repeat(50));
    System.out.println("Rules: Highest roll wins. You both roll 2 dice.");
    System.out.println("Bet: €" + minBet + " to €" + maxBet);
    System.out.println("Your money: €" + playerMoney);
    System.out.println("=".repeat(50) + "\n");
  }

  @Override
  public void displayDiceGameRolls(int playerRoll, int bobRoll) {
    System.out.println("\n --- Rolling dice... ---");
    System.out.println("You rolled: " + playerRoll);
    System.out.println("Bob rolled: " + bobRoll);
  }

  @Override
  public void displayDiceGameResult(
    int result,
    int playerMoney,
    String bobLoseDialogue
  ) {
    if (result > 0) {
      System.out.println("You win! You gain €" + result + ".");
    } else if (result < 0) {
      System.out.println("Bob: " + bobLoseDialogue);
      System.out.println("You lose €" + Math.abs(result) + ".");
    } else if (result == 0) {
      System.out.println("It's a draw! No money won or lost.");
    }

    System.out.println("Your current money: €" + playerMoney);
    System.out.println("\nType 'play dice' to play again!");
    System.out.println("=".repeat(50) + "\n");
  }

  @Override
  public void displayInvalidBet() {
    System.out.println("Invalid bet amount! Please enter a number.");
  }

  @Override
  public void displayDiceGameQuit(String bobDialogue) {
    System.out.println("Bob: " + bobDialogue);
  }

  // === BILLY ENCOUNTER ===

  @Override
  public void displayBillyIntro() {
    System.out.println("\n" + "=".repeat(50));
    System.out.println("A shadowy figure steps out from the darkness...");
    System.out.println("It's Billy! He's blocking your path!");
    System.out.println("=".repeat(50));
  }

  @Override
  public void displayBillyWeaponThreat(String weaponName, boolean isBottle) {
    System.out.println("\nYou pull out your " + weaponName + "!");
    if (isBottle) {
      System.out.println("You smash the bottle off the wall!");
    }
    System.out.println("Billy eyes it nervously...");
  }

  @Override
  public void displayBillyWeaponResponse(int damage) {
    if (damage >= 15) {
      System.out.println(
        "\"Alright, alright! Just give me a bit and I'll leave you alone!\""
      );
    } else {
      System.out.println(
        "\"That's not gonna stop me, but I'll go easy on ya!\""
      );
    }
  }

  @Override
  public void displayBillyRobberyWithWeapon(int moneyStolen) {
    if (moneyStolen > 0) {
      System.out.println(
        "\nBilly snatches " + moneyStolen + " from your wallet!"
      );
    }
    System.out.println(
      "Billy grabs some of your items and tosses them on the ground!"
    );
    System.out.println(
      "\"Consider yourself lucky!\" Billy disappears into the shadows."
    );
  }

  @Override
  public void displayBillyRobberyNoWeapon(int moneyStolen) {
    System.out.println("\nYou have nothing to defend yourself with!");
    System.out.println("Billy shoves you against the wall...");
    if (moneyStolen > 0) {
      System.out.println(
        "\nBilly empties your wallet! " + moneyStolen + " stolen!"
      );
    }
    System.out.println(
      "Billy rummages through your pockets and throws everything on the ground!"
    );
    System.out.println(
      "\n\"That'll teach ya to wander into my alley unarmed!\""
    );
    System.out.println("Billy runs off laughing...");
  }

  @Override
  public void displayBillyEscape() {
    System.out.println(
      "\nYou dust yourself off. Your items are scattered on the ground here."
    );
    System.out.println("=".repeat(50) + "\n");
  }

  // === HOUSE ENTRY ===

  @Override
  public void displayDoorLocked() {
    System.out.println("The door is locked!");
  }

  @Override
  public void displayDoorUnlocked() {
    System.out.println("You quietly unlock the door with your key...");
  }

  @Override
  public void displayKnock() {
    System.out.println("You knock on the door...");
    System.out.println("Your wife opens the door and greets you.");
  }

  @Override
  public void displayNothingToKnock() {
    System.out.println("There is nothing to knock here.");
  }

  // === SAVE/LOAD ===

  @Override
  public void displaySaveSuccess() {
    System.out.println("Game saved successfully.");
  }

  @Override
  public void displayLoadSuccess(String playerName) {
    System.out.println(
      "Game loaded successfully! Welcome back, " + playerName + "!"
    );
  }

  @Override
  public void displayLoadFailure() {
    System.out.println("No saved game found. Starting a new game.");
  }

  // === GAME END ===

  @Override
  public void displayEnding(WifeRequirements wifeReq, Player player) {
    System.out.println("\n" + "=".repeat(50));
    System.out.println("You encounter Herself...");
    System.out.println("=".repeat(50));

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

  // === USER INPUT ===

  @Override
  public String getUserInput() {
    System.out.print("> ");
    return scanner.nextLine();
  }

  @Override
  public String getUserInput(String prompt) {
    System.out.print(prompt + " ");
    return scanner.nextLine();
  }

  // === PLAYER FEEDBACK ===

  @Override
  public void displayItemAddedToInventory(String itemName) {
    System.out.println("Added to inventory: " + itemName);
  }

  @Override
  public void displayItemRemovedFromInventory(String itemName) {
    System.out.println("Removed from inventory: " + itemName);
  }

  @Override
  public void displayMoneyGained(int amount, int newTotal) {
    System.out.println(
      "You received " + amount + " euro. Total money: " + newTotal
    );
  }

  @Override
  public void displayMoneyLost(int amount, int remaining) {
    System.out.println(
      "You lost " + amount + " euro. Remaining money: " + remaining
    );
  }

  @Override
  public void displayHealthLost(int amount, int remaining) {
    System.out.println(
      "You lost " + amount + " health. Remaining health: " + remaining
    );
  }

  @Override
  public void displayHealthGained(int amount, int remaining) {
    System.out.println(
      "You gained " + amount + " health. Remaining health: " + remaining
    );
  }

  // === NPC CONVERSATION ===

  @Override
  public void displayMaybeNextTime() {
    System.out.println("Maybe next time!");
  }

  @Override
  public void displayGoodbye() {
    System.out.println("\"See you around so!\"");
  }

  @Override
  public void displayUnknownResponse() {
    System.out.println("I don't understand your response.");
  }

  // === CAB INTERACTION ===

  @Override
  public void displayCabPrompt() {
    System.out.println("Do you want to go home? (yes/no)");
  }

  @Override
  public void displayStayOut() {
    System.out.println("You decide to stay out a bit longer.");
  }

  // === GAMEPLAY ===

  @Override
  public void displayUnknownGame() {
    System.out.println("Don't know that game.");
  }

  @Override
  public void displayNotInInventory() {
    System.out.println("You don't have that item.");
  }
}
