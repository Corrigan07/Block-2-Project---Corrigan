package com.zork;

public class Command {

  private CommandType command;
  private String secondWord;

  public Command(CommandType firstWord, String secondWord) {
    this.command = firstWord;
    this.secondWord = secondWord;
  }

  public CommandType getCommandWord() {
    return command;
  }

  public String getSecondWord() {
    return secondWord;
  }

  public boolean isUnknown() {
    return command == CommandType.UNKNOWN;
  }

  public boolean hasSecondWord() {
    return secondWord != null;
  }
}
