import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Serializable {

  private String description;
  private Map<String, Room> exits; // Map direction to neighboring Room
  private final List<Item> items = new ArrayList<>();
  private final List<NPC> npcs = new ArrayList<>();
  private String longDescription;

  public Room(String description, String longDescription) {
    this.description = description;
    this.longDescription = longDescription;
    exits = new HashMap<>();
  }

  public String getDescription() {
    return description;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public void setExit(String direction, Room neighbor) {
    exits.put(direction, neighbor);
  }

  public Room getExit(String direction) {
    return exits.get(direction);
  }

  public String getExitString() {
    StringBuilder sb = new StringBuilder();
    for (String direction : exits.keySet()) {
      sb.append(direction).append(" ");
    }
    return sb.toString().trim();
  }

  public String getLocationDescription() {
    return "You are " + description + ".\nExits: " + getExitString();
  }

  public void addItem(Item item) {
    if (item == null) throw new IllegalArgumentException("item cannot be null");
    items.add(item);
  }

  public List<Item> getItemsInRoom() {
    return items;
  }

  public List<NPC> getNpcsInRoom() {
    return npcs;
  }

  public void addNpc(NPC npc) {
    if (npc == null) throw new IllegalArgumentException("npc cannot be null");
    npcs.add(npc);
  }
}
