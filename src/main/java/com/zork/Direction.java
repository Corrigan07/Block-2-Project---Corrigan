package com.zork;

public enum Direction {
  NORTH("north"),
  SOUTH("south"),
  EAST("east"),
  WEST("west"),
  ORDER("order"),
  BACK("back"),
  UNKNOWN("?");

  private final String direction;

  Direction(String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }

  public static Direction fromString(String directionStr) {
    for (Direction dir : Direction.values()) {
      if (dir.getDirection().equalsIgnoreCase(directionStr)) {
        return dir;
      }
    }
    return UNKNOWN;
  }
}
