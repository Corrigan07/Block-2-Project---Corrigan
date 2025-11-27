package com.zork;

public enum CommandType {
  GO("go"),
  QUIT("quit"),
  HELP("help"),
  LOOK("look"),
  EAT("eat"),
  COLLECT("collect"),
  INVENTORY("inventory"),
  CHECK("check"),
  TALK("talk"),
  DROP("drop"),
  DRINK("drink"),
  SAVE("save"),
  OPEN("open"),
  MOVE("move"),
  PLAY("play"),
  UNKNOWN("?");

  private final String command;

  CommandType(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }

  public static CommandType fromString(String commandStr) {
    for (CommandType type : CommandType.values()) {
      if (type.getCommand().equalsIgnoreCase(commandStr)) {
        return type;
      }
    }
    return UNKNOWN;
  }
}
