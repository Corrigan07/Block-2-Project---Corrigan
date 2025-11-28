package com.zork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Serializable {

  private String description;
  private Map<Direction, Room> exits; // Map direction to neighboring Room
  private final List<Item> items = new ArrayList<>();
  private final List<NPC> npcs = new ArrayList<>();
  private String longDescription;
  private boolean isDark;
  private boolean isLocked;

  public Room(
    String description,
    String longDescription,
    boolean isDark,
    boolean isLocked
  ) {
    this.description = description;
    this.longDescription = longDescription;
    exits = new HashMap<>();
    this.isDark = isDark;
    this.isLocked = isLocked;
  }

  public String getDescription() {
    return description;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public void setExit(Direction direction, Room neighbor) {
    exits.put(direction, neighbor);
  }

  public Room getExit(Direction direction) {
    return exits.get(direction);
  }

  public String getExitString() {
    StringBuilder sb = new StringBuilder();
    for (Direction direction : exits.keySet()) {
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

  public void removeItem(Item item) {
    items.remove(item);
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

  public boolean isDark() {
    return isDark;
  }

  public boolean isLocked() {
    return isLocked;
  }
}
