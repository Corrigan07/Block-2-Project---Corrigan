import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

  private List<Item> inventory;
  private int money;
  private int health;
  private int pintCount;

  public Player(
    String name,
    Room startingRoom,
    int startingMoney,
    int health,
    int pintCount
  ) {
    super(name, startingRoom);
    this.money = startingMoney;
    this.inventory = new ArrayList<>();
    this.health = health;
    this.pintCount = pintCount;
    Item key = new Item("key", "key to the house", 2);
    inventory.add(key);
  }

  public void move(String direction) {
    Room currentRoom = getCurrentRoom();
    Room nextRoom = currentRoom.getExit(direction);
    if (nextRoom != null) {
      currentRoom = nextRoom;
      System.out.println("You moved to: " + currentRoom.getDescription());
    } else {
      System.out.println("You can't go that way!");
    }
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
        "You spent " + amount + " money. Remaining money: " + money
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
      System.out.println("You lost " + "health. Remaining health: " + health);
    }
  }

  public int getPintCount() {
    return pintCount;
  }

  public void incrementPintCount() {
    pintCount++;
  }
}
