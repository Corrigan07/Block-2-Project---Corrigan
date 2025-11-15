import java.util.HashMap;
import java.util.Map;

public class CommandWords {

  private Map<String, String> validCommands;

  public CommandWords() {
    validCommands = new HashMap<>();
    validCommands.put("go", "Move to another room");
    validCommands.put("quit", "End the game");
    validCommands.put("help", "Show help");
    validCommands.put("look", "Look around");
    validCommands.put("eat", "Eat something");
    validCommands.put("collect", "Collect an item");
    validCommands.put("inventory", "Show your inventory");
    validCommands.put("check", "check something");
    validCommands.put("talk", "Talk to an NPC");
    validCommands.put("yes", "respond yes");
    validCommands.put("no", "respond no");
    validCommands.put("drop", "Drop an item");
    validCommands.put("drink", "drink something");
    validCommands.put("save", "Save your game");
    validCommands.put("eat", "Eat an item");
  }

  public boolean isCommand(String commandWord) {
    return validCommands.containsKey(commandWord);
  }

  public void showAll() {
    System.out.print("Valid commands are: ");
    for (String command : validCommands.keySet()) {
      System.out.print(command + " ");
    }
    System.out.println();
  }
}
