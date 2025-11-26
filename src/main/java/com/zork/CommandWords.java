package com.zork;

import java.util.HashMap;
import java.util.Map;

public class CommandWords {

  private Map<String, CommandType> validCommands;

  public CommandWords() {
    validCommands = new HashMap<>();

    for (CommandType command : CommandType.values()) {
      if (command != CommandType.UNKNOWN) {
        validCommands.put(command.getCommand(), command);
      }
    }
  }

  public boolean isCommand(String commandWord) {
    return validCommands.containsKey(commandWord);
  }

  public CommandType getCommandType(String commandWord) {
    if (commandWord == null) {
      return CommandType.UNKNOWN;
    }

    CommandType type = validCommands.get(commandWord.toLowerCase());
    if (type != null) {
      return type;
    } else {
      return CommandType.UNKNOWN;
    }
  }

  public void showAll() {
    System.out.print("Valid commands are: ");
    for (String command : validCommands.keySet()) {
      System.out.print(command + " ");
    }
    System.out.println();
  }
}
