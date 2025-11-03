import java.util.ArrayList;
import java.util.List;

public class Character {

  private String name;
  private Room currentRoom;
  private List<Item> inventory;

  public Character(String name, Room startingRoom) {
    this.name = name;
    this.currentRoom = startingRoom;
    this.inventory = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }

  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  public void move(String direction) {
    Room nextRoom = currentRoom.getExit(direction);
    if (nextRoom != null) {
      currentRoom = nextRoom;
      System.out.println("You moved to: " + currentRoom.getDescription());
    } else {
      System.out.println("You can't go that way!");
    }
  }

  public void pickUpItem(Item item) {
    if (item != null && currentRoom.getItemsInRoom().contains(item)) {
      inventory.add(item);
      currentRoom.getItemsInRoom().remove(item);
      System.out.println("You picked up: " + item.getName());
    } else {
      System.out.println("Item not found in the room.");
    }
  }

  public List<Item> showInventory() {
    System.out.println("Inventory:");
    for (Item item : inventory) {
      System.out.println("- " + item.getName());
    }
    return inventory;
  }
}
