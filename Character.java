import java.util.ArrayList;
import java.util.List;

public class Character {

  private String name;
  private Room currentRoom;

  public Character(String name, Room startingRoom) {
    this.name = name;
    this.currentRoom = startingRoom;
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
}
