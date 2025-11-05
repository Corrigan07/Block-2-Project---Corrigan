import java.util.List;

public class NPC extends Character {

  private List<String> welcomeLines;
  private List<String> dialogueLines;

  public NPC(String name, Room startingRoom, List<String> dialogueLines) {
    super(name, startingRoom);
    this.dialogueLines = dialogueLines;
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
        System.out.println(line);
      }
    }
  }
}
