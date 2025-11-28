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

  public void pickUpItem(Item item) {
    Room room = getCurrentRoom();
    if (item != null && room != null && room.getItemsInRoom().contains(item)) {
      inventory.add(item);
      room.getItemsInRoom().remove(item);
      System.out.println("You picked up: " + item.getName());
    } else {
      System.out.println("Item not found in the room.");
    }
  }

  public void dropItem(Item item) {
    Room room = getCurrentRoom();
    if (item != null && inventory.contains(item)) {
      inventory.remove(item);
      room.addItem(item);
      System.out.println("You dropped: " + item.getName());
    } else {
      System.out.println("You don't have that item.");
    }
  }

  public void removeItemFromInventory(Item item) {
    if (item != null && inventory.contains(item)) {
      inventory.remove(item);
      System.out.println("Removed from inventory: " + item.getName());
    } else {
      System.out.println("Item not found in inventory.");
    }
  }

  public List<Item> showInventory() {
    if (inventory.isEmpty()) {
      System.out.println("Your inventory is empty.");
      return inventory;
    } else {
      System.out.println("Inventory:");
      for (Item item : inventory) {
        System.out.println("- " + item.getName());
      }
      return inventory;
    }
  }

  public void addItemToInventory(Item item) {
    if (item != null) {
      inventory.add(item);
      System.out.println("Added to inventory: " + item.getName());
    }
  }

  public List<Item> getInventory() {
    return inventory;
  }

  public int getMoney() {
    return money;
  }

  public void addMoney(int amount) {
    if (amount > 0) {
      money += amount;
      System.out.println(
        "You received " + amount + " money. Total money: " + money
      );
    }
  }

  public void removeMoney(int amount) {
    if (amount > 0 && amount <= money) {
      money -= amount;
      System.out.println(
        "You lose " + amount + " money. Remaining money: " + money
      );
    } else {
      System.out.println("Insufficient funds.");
    }
  }

  public int getHealth() {
    return health;
  }

  public void removeHealth(int amount) {
    if (amount > 0) {
      health -= amount;
      if (health < 0) {
        health = 0;
      }
      System.out.println(
        "You lost " + amount + " health. Remaining health: " + health
      );
    }
  }

  public void addHealth(int amount) {
    if (amount > 0) {
      health += amount;
      if (health < 0) {
        health = 0;
      }
      System.out.println(
        "You gained " + amount + " health. Remaining health: " + health
      );
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

  public void savePlayerState() {
    try (
      ObjectOutputStream out = new ObjectOutputStream(
        new FileOutputStream("player.ser")
      )
    ) {
      out.writeObject(this);
      System.out.println("Player state saved successfully.");
    } catch (Exception e) {
      System.out.println("Error saving player state: " + e.getMessage());
    }
  }

  public static Player loadPlayerState() {
    try (
      ObjectInputStream in = new ObjectInputStream(
        new FileInputStream("player.ser")
      )
    ) {
      Player player = (Player) in.readObject();
      System.out.println("Player state loaded successfully.");
      return player;
    } catch (Exception e) {
      System.out.println("Error loading player state: " + e.getMessage());
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
    } else {
      System.out.println("Invalid time increment.");
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

    // Create list of items to potentially drop
    ArrayList<Item> droppableItems = new ArrayList<>(inventory);

    // Ensure we don't try to drop more items than player has
    int actualDropCount = Math.min(count, droppableItems.size());

    // Shuffle and drop random items
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
