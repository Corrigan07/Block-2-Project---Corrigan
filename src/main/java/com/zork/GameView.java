package com.zork;

import java.util.List;

public interface GameView {
  // === BASIC OUTPUT ===
  void displayMessage(String message);

  // === ROOM & WORLD ===
  void displayRoom(Room room, Room previousRoom);
  void displayMenu(List<PurchasableItem> menu);
  void displayWelcome(String playerName);
  void displayHelp(Room currentRoom);

  // === PLAYER STATS ===
  void displayInventory(List<Item> inventory);
  void displayTime(String formattedTime);
  void displayMoney(int money);
  void displayHealth(int health);

  // === MOVEMENT ===
  void displayInvalidDirection();
  void displayNoExit();

  // === ITEMS ===
  void displayItemCollected(String itemName);
  void displayItemDropped(String itemName);
  void displayNoSuchItem();
  void displayItemNotCollectible();
  void displayBoxOpened(String boxName, String contents);

  // === CONSUMPTION ===
  void displayDrinkPint(int healthLoss, int newHealth);
  void displayDrinkWater();
  void displayEatFood(String foodName, int healthGain, int newHealth);
  void displayNoItemToConsume();
  void displayCannotConsume();
  void displayCannotEat();

  // === NPC INTERACTIONS ===
  void displayNPCDialogue(String npcName, String dialogue);
  void displayNoNPCHere();
  void displayPurchaseSuccess(String itemName, int price, int remainingMoney);
  void displayPurchaseFailure(String reason);
  void displayCabRide();

  // === DICE GAME ===
  void displayDiceGameIntro(int minBet, int maxBet, int playerMoney);
  void displayDiceGameRolls(int playerRoll, int bobRoll);
  void displayDiceGameResult(
    int result,
    int playerMoney,
    String bobLoseDialogue
  );
  void displayInvalidBet();
  void displayDiceGameQuit(String bobDialogue);

  // === BILLY ENCOUNTER ===
  void displayBillyIntro();
  void displayBillyWeaponThreat(String weaponName, boolean isBottle);
  void displayBillyWeaponResponse(int damage);
  void displayBillyRobberyWithWeapon(int moneyStolen);
  void displayBillyRobberyNoWeapon(int moneyStolen);
  void displayBillyEscape();

  // === HOUSE ENTRY ===
  void displayDoorLocked();
  void displayDoorUnlocked();
  void displayKnock();
  void displayNothingToKnock();

  // === SAVE/LOAD ===
  void displaySaveSuccess();
  void displayLoadSuccess(String playerName);
  void displayLoadFailure();

  // === GAME END ===
  void displayEnding(WifeRequirements ending, Player player);

  // === USER INPUT ===
  String getUserInput();
  String getUserInput(String prompt);

  // === PLAYER FEEDBACK ===
  void displayItemAddedToInventory(String itemName);
  void displayItemRemovedFromInventory(String itemName);
  void displayMoneyGained(int amount, int newTotal);
  void displayMoneyLost(int amount, int remaining);
  void displayHealthLost(int amount, int remaining);
  void displayHealthGained(int amount, int remaining);

  // === NPC CONVERSATION ===
  void displayMaybeNextTime();
  void displayGoodbye();
  void displayUnknownResponse();

  // === CAB INTERACTION ===
  void displayCabPrompt();
  void displayStayOut();

  // === GAMEPLAY ===
  void displayUnknownGame();
  void displayNotInInventory();
}
