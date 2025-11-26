package com.zork;

import java.util.Scanner;

public class Parser {

  private CommandWords commands;
  private Scanner reader;

  public Parser() {
    commands = new CommandWords();
    reader = new Scanner(System.in);
  }

  public Scanner getReader() {
    return reader;
  }

  public Command getCommand() {
    System.out.print("> ");
    String inputLine = reader.nextLine();

    String word1 = null;
    String word2 = null;

    Scanner tokenizer = new Scanner(inputLine);
    if (tokenizer.hasNext()) {
      word1 = tokenizer.next();
      if (tokenizer.hasNext()) {
        word2 = tokenizer.next();
      }
    }

    CommandType commandType = commands.getCommandType(word1);
    return new Command(commandType, word2);
  }

  public void showCommands() {
    commands.showAll();
  }
}
