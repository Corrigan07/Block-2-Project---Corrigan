package com.zork;

import java.io.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends Character {

  private List<Item> inventory;
  private int money;
  private int health;
  private int pintCount;
  private int currentTime;
  private int startingMoney;
  private boolean hasKnocked;

  public Player(
    String name,
    Room startingRoom,
    int money,
    int health,
    int pintCount,
    int currentTime,
    int startingMoney,
    boolean hasKnocked
  ) {
    super(name, startingRoom);
    this.money = money;
    this.inventory = new ArrayList<>();
    this.health = health;
    this.pintCount = pintCount;
    this.currentTime = currentTime;
    this.startingMoney = startingMoney;
    this.hasKnocked = false;
    Item key = new UsableItem("key", "key to the house", true, true, true);
    inventory.add(key);
  }

  // PRINT REMOVED - Controller will call view.displayItemCollected()
  public void pickUpItem(Item item) {
    Room room = getCurrentRoom();
    if (item != null && room != null && room.getItemsInRoom().contains(item)) {
      inventory.add(item);
      room.getItemsInRoom().remove(item);
    }
  }

  // PRINT REMOVED - Controller will call view.displayItemDropped()
  public void dropItem(Item item) {
    Room room = getCurrentRoom();
    if (item != null && inventory.contains(item)) {
      inventory.remove(item);
      room.addItem(item);
    }
  }

  // PRINT REMOVED - Controller will call view.displayItemRemovedFromInventory()
  public void removeItemFromInventory(Item item) {
    if (item != null && inventory.contains(item)) {
      inventory.remove(item);
    }
  }

  // PRINT REMOVED - Controller will call view.displayInventory()
  public List<Item> showInventory() {
    return inventory;
  }

  // PRINT REMOVED - Controller will call view.displayItemAddedToInventory()
  public void addItemToInventory(Item item) {
    if (item != null) {
      inventory.add(item);
    }
  }

  public List<Item> getInventory() {
    return inventory;
  }

  public int getMoney() {
    return money;
  }

  // PRINT REMOVED - Controller will call view.displayMoneyGained()
  public void addMoney(int amount) {
    if (amount > 0) {
      money += amount;
    }
  }

  // PRINT REMOVED - Controller will call view.displayMoneyLost()
  public void removeMoney(int amount) {
    if (amount > 0 && amount <= money) {
      money -= amount;
    }
  }

  public int getHealth() {
    return health;
  }

  // PRINT REMOVED - Controller will call view.displayHealthLost()
  public void removeHealth(int amount) {
    if (amount > 0) {
      health -= amount;
      if (health < 0) {
        health = 0;
      }
    }
  }

  // PRINT REMOVED - Controller will call view.displayHealthGained()
  public void addHealth(int amount) {
    if (amount > 0) {
      health += amount;
      if (health < 0) {
        health = 0;
      }
    }
  }

  public int getPintCount() {
    return pintCount;
  }

  public void incrementPintCount() {
    pintCount++;
  }

  public void decrementPintCount() {
    if (pintCount > 0) {
      pintCount--;
    }
  }

  // PRINT REMOVED - Controller will call view.displaySaveSuccess()
  public void savePlayerState() {
    try (
      ObjectOutputStream out = new ObjectOutputStream(
        new FileOutputStream("player.ser")
      )
    ) {
      out.writeObject(this);
    } catch (Exception e) {
      System.out.println("Error saving player state: " + e.getMessage());
    }
  }

  // PRINT REMOVED - Controller will call view.displayLoadSuccess()
  public static Player loadPlayerState() {
    try (
      ObjectInputStream in = new ObjectInputStream(
        new FileInputStream("player.ser")
      )
    ) {
      Player player = (Player) in.readObject();
      return player;
    } catch (Exception e) {
      return null;
    }
  }

  public int getCurrentTime() {
    return currentTime;
  }

  public String getCurrentTimeFormatted() {
    int totalMinutes = currentTime % (24 * 60);
    int hours = totalMinutes / 60;
    int minutes = totalMinutes % 60;

    String period = "";
    if (hours >= 12) {
      period = "PM";
      if (hours > 12) {
        hours -= 12;
      }
    } else {
      period = "AM";
      if (hours == 0) {
        hours = 12;
      }
    }

    return String.format("%02d:%02d %s", hours, minutes, period);
  }

  public int getHourOfDay() {
    return (currentTime / 60);
  }

  public void incrementTime(int minutes) {
    if (minutes > 0) {
      currentTime += minutes;
    }
  }

  public boolean hasKnocked() {
    return hasKnocked;
  }

  public void setHasKnocked(boolean hasKnocked) {
    this.hasKnocked = hasKnocked;
  }

  public int getStartingMoney() {
    return startingMoney;
  }

  public boolean hasKey() {
    for (Item item : inventory) {
      if (item.getName().equalsIgnoreCase("key")) {
        return true;
      }
    }
    return false;
  }

  public Weapon getBestWeapon() {
    Weapon bestWeapon = null;
    int highestDamage = 0;

    for (Item item : inventory) {
      if (item instanceof Weapon) {
        Weapon weapon = (Weapon) item;
        if (weapon.getDamage() > highestDamage) {
          highestDamage = weapon.getDamage();
          bestWeapon = weapon;
        }
      }
    }

    return bestWeapon;
  }

  public void dropRandomItems(int count, Room targetRoom) {
    if (inventory.isEmpty() || count <= 0) {
      return;
    }

    ArrayList<Item> droppableItems = new ArrayList<>(inventory);
    int actualDropCount = Math.min(count, droppableItems.size());

    Random random = new Random();
    for (int i = 0; i < actualDropCount; i++) {
      if (droppableItems.isEmpty()) break;

      int randomIndex = random.nextInt(droppableItems.size());
      Item itemToDrop = droppableItems.get(randomIndex);

      targetRoom.addItem(itemToDrop);
      inventory.remove(itemToDrop);
      droppableItems.remove(randomIndex);
    }
  }
}
