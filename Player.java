import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

  private List<Item> inventory;
  private int money;

  public Player(String name, Room startingRoom, int startingMoney) {
    super(name, startingRoom);
    this.money = startingMoney;
    this.inventory = new ArrayList<>();
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

  public int getMoney() {
    return money;
  }
}
