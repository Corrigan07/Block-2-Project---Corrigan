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

  /**
   * Get command from console input (for console mode)
   */
  public Command getCommand() {
    System.out.print("> ");
    String inputLine = reader.nextLine();
    return parseCommand(inputLine);
  }

  /**
   * Parse a command from a string (for GUI mode)
   * @param inputLine the command string to parse
   * @return Command object with parsed command type and argument
   */
  public Command parseCommand(String inputLine) {
    String word1 = null;
    String word2 = null;

    try (Scanner tokenizer = new Scanner(inputLine)) {
      if (tokenizer.hasNext()) {
        word1 = tokenizer.next();
        if (tokenizer.hasNext()) {
          word2 = tokenizer.next();
        }
      }
    }

    CommandType commandType = commands.getCommandType(word1);
    return new Command(commandType, word2);
  }

  /**
   * Get direct user input (for NPC dialogues and minigames)
   * @return the user's input line
   */
  public String getInput() {
    return reader.nextLine();
  }

  public void showCommands() {
    commands.showAll();
  }
}
