package com.zork;

import java.util.List;

public class NPC extends Character {

  private List<String> welcomeLines;
  private List<String> dialogueLines;

  public NPC(
    String name,
    Room startingRoom,
    List<String> dialogueLines,
    List<String> welcomeLines
  ) {
    super(name, startingRoom);
    this.dialogueLines = dialogueLines;
    this.welcomeLines = welcomeLines;
  }

  public List<String> getDialogueLines() {
    return dialogueLines;
  }

  public void setDialogueLines(List<String> dialogueLines) {
    this.dialogueLines = dialogueLines;
  }

  public List<String> getWelcomeLines() {
    return welcomeLines;
  }

  public void setWelcomeLines(List<String> welcomeLines) {
    this.welcomeLines = welcomeLines;
  }

  public void welcomePlayer() {
    if (welcomeLines != null) {
      for (String line : welcomeLines) {
        System.out.println("\"" + line + "\"");
      }
    }
  }

  public void talk(int startOfDialogue, int endOfDialogue) {
    if (dialogueLines != null) {
      for (String line : dialogueLines.subList(
        startOfDialogue,
        endOfDialogue
      )) {
        System.out.println("\"" + line + "\"");
      }
    }
  }
}
