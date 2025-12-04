package com.zork;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuiView extends Application implements GameView {

  @FXML
  private BorderPane rootPane;

  @FXML
  private HBox statusBar;

  @FXML
  private Label titleLabel;

  @FXML
  private Label healthLabel;

  @FXML
  private Label moneyLabel;

  @FXML
  private Label timeLabel;

  @FXML
  private ImageView roomImageView;

  @FXML
  private ImageView mapImageView;

  @FXML
  private ListView<Item> inventoryListView;

  @FXML
  private TextArea outputTextArea;

  @FXML
  private TextField inputTextField;

  @FXML
  private Button sendButton;

  private GameController controller;
  private Parser parser;
  private CommandWords commandWords;
  private MediaPlayer mediaPlayer;
  private MediaPlayer backgroundMusicPlayer;

  // State management for initialization
  private boolean waitingForGameChoice = false;
  private boolean waitingForPlayerName = false;
  private String gameChoice = "";

  // Callback for when user provides input (dice game, NPC responses)
  private java.util.function.Consumer<String> inputCallback = null;

  @Override
  public void start(Stage stage) throws Exception {
    try {
      // Load the custom font first
      try {
        javafx.scene.text.Font.loadFont(
          getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"),
          12
        );
        System.out.println("Font loaded successfully");
      } catch (Exception e) {
        System.err.println("Failed to load font: " + e.getMessage());
      }

      System.out.println("Loading FXML...");
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/GUI.fxml")
      );
      System.out.println(
        "FXML resource found: " + getClass().getResource("/fxml/GUI.fxml")
      );
      loader.setController(this);
      Parent root = loader.load();
      System.out.println("FXML loaded successfully");

      Scene scene = new Scene(root);
      scene
        .getStylesheets()
        .add(getClass().getResource("/fonts/pressstart.css").toExternalForm());
      stage.setScene(scene);
      stage.setTitle("The Usual");
      stage.setMinWidth(800);
      stage.setMinHeight(600);
      stage.show();
      System.out.println("Stage shown");

      setupEventHandlers();
      System.out.println("Event handlers setup");

      parser = new Parser();
      commandWords = new CommandWords();
      controller = new GameController(this);
      System.out.println("GameController created");

      // Start initialization sequence through text input
      startInitialization();
      System.out.println("Initialization started");

      // Start background music
      startBackgroundMusic();
    } catch (Exception e) {
      System.err.println("ERROR in GuiView.start(): " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  private void setupEventHandlers() {
    sendButton.setOnAction(event -> handleCommand());

    inputTextField.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        handleCommand();
      }
    });

    inventoryListView.setCellFactory(listView ->
      new javafx.scene.control.ListCell<Item>() {
        private javafx.scene.image.ImageView imageView =
          new javafx.scene.image.ImageView();
        private javafx.scene.control.Label label =
          new javafx.scene.control.Label();
        private javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(
          10,
          imageView,
          label
        );

        @Override
        protected void updateItem(Item item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setGraphic(null);
          } else {
            imageView.setImage(loadItemIcon(item));
            imageView.setFitWidth(32);
            imageView.setFitHeight(32);
            imageView.setPreserveRatio(true);
            label.setText(item.getName());
            label.setStyle("-fx-text-fill: white;");
            setGraphic(hbox);
            setStyle("-fx-background-color: transparent;");
          }
        }
      }
    );
  }

  private void startInitialization() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("=== The Usual ===\n\n");
      outputTextArea.appendText(
        "Would you like to start a new game or load a saved game?\n"
      );
      outputTextArea.appendText("Type 'new' or 'load'\n\n");
      waitingForGameChoice = true;
    });
  }

  private void handleInitializationInput(String input) {
    if (waitingForGameChoice) {
      gameChoice = input.trim().toLowerCase();
      waitingForGameChoice = false;

      if (gameChoice.equals("load")) {
        Player loaded = Player.loadPlayerState();
        if (loaded != null) {
          controller.setPlayer(loaded);
          if (loaded.getCurrentRoom() == null) {
            controller.setPlayerRoom(controller.getOutsideRoom());
          }
          outputTextArea.appendText(
            "Game loaded successfully! Welcome back, " +
            loaded.getName() +
            "!\n\n"
          );
          controller.displayGameStart();
        } else {
          outputTextArea.appendText(
            "No saved game found. Starting a new game.\n\n"
          );
          promptForPlayerName();
        }
      } else if (gameChoice.equals("new")) {
        promptForPlayerName();
      } else {
        outputTextArea.appendText(
          "Unknown command. Please type 'new' or 'load'\n\n"
        );
        waitingForGameChoice = true;
      }
    } else if (waitingForPlayerName) {
      String playerName = input.trim();
      if (playerName.isEmpty()) {
        outputTextArea.appendText("Please enter a valid name.\n\n");
        return;
      }
      waitingForPlayerName = false;
      controller.createNewPlayer(playerName);
      controller.displayGameStart();
    }
  }

  private void promptForPlayerName() {
    outputTextArea.appendText("Hello there, what is your name?\n");
    waitingForPlayerName = true;
  }

  private void handleCommand() {
    String commandText = inputTextField.getText();
    if (commandText != null && !commandText.isBlank()) {
      outputTextArea.appendText("> " + commandText + "\n");
      inputTextField.clear();

      // Handle initialization inputs first
      if (waitingForGameChoice || waitingForPlayerName) {
        handleInitializationInput(commandText);
        return;
      }

      // Handle pending input callback (dice game, NPC conversations)
      if (inputCallback != null) {
        java.util.function.Consumer<String> callback = inputCallback;
        inputCallback = null;
        callback.accept(commandText);
        return;
      }

      // Parse the command text into a Command object
      String[] words = commandText.trim().split("\\s+");
      String commandWord = words[0].toLowerCase();
      String secondWord = (words.length > 1) ? words[1] : null;

      CommandType commandType = commandWords.getCommandType(commandWord);
      Command command = new Command(commandType, secondWord);

      controller.processCommand(command);

      if (controller.isGameOver()) {
        inputTextField.setDisable(true);
        sendButton.setDisable(true);
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void displayMessage(String message) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(message + "\n");
    });
  }

  @Override
  public void displayRoom(Room room, Room previousRoom) {
    javafx.application.Platform.runLater(() -> {
      roomImageView.setImage(loadRoomImage(room));
      mapImageView.setImage(loadMapImage(room));

      outputTextArea.appendText("\n" + room.getLocationDescription() + "\n");
      outputTextArea.appendText(room.getLongDescription() + "\n");

      // Play audio for chipperCounter room
      if (room.getLocationDescription().contains("at the chipper counter")) {
        playAudio("/audio/Its A Me, Mario - QuickSounds.com.mp3");
      }

      // Play hello sound only when entering chipper from outside
      if (
        room.getLocationDescription().contains("Mario's Chipper") &&
        previousRoom != null &&
        previousRoom.getLocationDescription().contains("in the middle of town")
      ) {
        playAudio("/audio/sm64_mario_hello.wav");
      }
    });
  }

  @Override
  public void displayMenu(java.util.List<PurchasableItem> menu) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Menu:\n");
      for (PurchasableItem item : menu) {
        outputTextArea.appendText(
          "- " +
          item.getName() +
          ": " +
          item.getDescription() +
          " (Price: " +
          item.getPrice() +
          ")\n"
        );
      }
    });
  }

  @Override
  public void displayWelcome(String playerName) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.clear();
      outputTextArea.appendText("\n");
      outputTextArea.appendText("Welcome " + playerName + ", to The Usual!\n");
      outputTextArea.appendText(
        "It's a Friday evening and you are standing outside one of your favourite places on earth - the pub\n"
      );
      outputTextArea.appendText("Type 'help' if you need help.\n");
      outputTextArea.appendText("\n");
    });
  }

  @Override
  public void displayHelp(Room currentRoom) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Available commands:\n");
      outputTextArea.appendText("- go [direction]\n");
      outputTextArea.appendText("- look\n");
      outputTextArea.appendText("- collect [item]\n");
      outputTextArea.appendText("- inventory\n");
      outputTextArea.appendText("- check [item]\n");
      outputTextArea.appendText("- talk [npc]\n");
      outputTextArea.appendText("- drop [item]\n");
      outputTextArea.appendText("- eat [item]\n");
      outputTextArea.appendText("- drink [item]\n");
      outputTextArea.appendText("- save\n");
      outputTextArea.appendText("- open [item]\n");
      outputTextArea.appendText("- move [item]\n");
      outputTextArea.appendText("- play [item]\n");
      outputTextArea.appendText("- knock [item]\n");
      outputTextArea.appendText("- quit\n\n");

      if (currentRoom.getLocationDescription().contains("outside")) {
        outputTextArea.appendText(
          "You are outside, why don't you go to the pub :D\n"
        );
      } else if (
        currentRoom.getLocationDescription().contains("Mario's Chipper")
      ) {
        outputTextArea.appendText(
          "You are in Mario's Chipper, the best place for chips! However, you can always go back to the pub after getting some grub.\n"
        );
      } else if (
        currentRoom.getLocationDescription().contains("Corrigan's Bar")
      ) {
        outputTextArea.appendText(
          "You are in Corrigan's Bar. Hooray! You should stay here for a good while and support your local\n"
        );
      } else if (currentRoom.getLocationDescription().contains("cab")) {
        outputTextArea.appendText(
          "You are outside a cab. If you want to go home you should talk to the taxi driver.\n"
        );
      } else if (currentRoom.getLocationDescription().contains("home")) {
        outputTextArea.appendText(
          "You are finally home. You can go west outside the house or south to a bathroom.\n"
        );
      } else if (currentRoom.getLocationDescription().contains("alleyway")) {
        outputTextArea.appendText(
          "You are in a dark and creepy alleyway. You can go west to outside.\n"
        );
      } else if (currentRoom.getLocationDescription().contains("bathroom")) {
        outputTextArea.appendText(
          "You are in a bathroom. You can go south to the pub.\n"
        );
      } else if (
        currentRoom.getLocationDescription().contains("outside the house")
      ) {
        outputTextArea.appendText(
          "You are outside the house. You should knock on the door or go east to the house.\n"
        );
      }
    });
  }

  @Override
  public void displayInventory(java.util.List<Item> inventory) {
    javafx.application.Platform.runLater(() -> {
      inventoryListView.getItems().clear();
      inventoryListView.getItems().addAll(inventory);

      if (inventory.isEmpty()) {
        outputTextArea.appendText("Your inventory is empty.\n");
      } else {
        outputTextArea.appendText("Your Inventory:\n");
        for (Item item : inventory) {
          outputTextArea.appendText(
            "- " + item.getName() + ": " + item.getDescription() + "\n"
          );
        }
      }
    });
  }

  // Update inventory ListView only, without printing to output area
  public void updateInventoryDisplay(java.util.List<Item> inventory) {
    javafx.application.Platform.runLater(() -> {
      inventoryListView.getItems().clear();
      inventoryListView.getItems().addAll(inventory);
    });
  }

  // Update room image only, without printing room description
  public void updateRoomImage(Room room) {
    javafx.application.Platform.runLater(() -> {
      roomImageView.setImage(loadRoomImage(room));
    });
  }

  @Override
  public void displayTime(String formattedTime) {
    javafx.application.Platform.runLater(() -> {
      timeLabel.setText("🕐 " + formattedTime);
    });
  }

  @Override
  public void displayMoney(int money) {
    javafx.application.Platform.runLater(() -> {
      moneyLabel.setText("💰 €" + money);
    });
  }

  @Override
  public void displayHealth(int health) {
    javafx.application.Platform.runLater(() -> {
      healthLabel.setText("❤ " + health);
    });
  }

  @Override
  public void displayInvalidDirection() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("That's not a valid direction!\n");
      outputTextArea.appendText(
        "Try: north, south, east, west, order, or back\n"
      );
    });
  }

  @Override
  public void displayNoExit() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("There is no door!\n");
    });
  }

  @Override
  public void displayItemCollected(String itemName) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You picked up: " + itemName + "\n");
    });
  }

  @Override
  public void displayItemDropped(String itemName) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You dropped: " + itemName + "\n");
    });
  }

  @Override
  public void displayNoSuchItem() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("There is no such item here.\n");
    });
  }

  @Override
  public void displayItemNotCollectible() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You can't collect that item.\n");
    });
  }

  @Override
  public void displayBoxOpened(String boxName, String contents) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(
        "You opened the " + boxName + " and found: " + contents + "\n"
      );
    });
  }

  @Override
  public void displayDrinkPint(int healthLoss, int newHealth) {
    javafx.application.Platform.runLater(() -> {
      healthLabel.setText("❤ " + newHealth);
      outputTextArea.appendText("You drank a pint.\n");
      outputTextArea.appendText(
        "Your health decreased by " + healthLoss + ".\n"
      );
      outputTextArea.appendText("You now have " + newHealth + " health.\n");
    });
  }

  @Override
  public void displayDrinkWater() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You drank water.\n");
    });
  }

  @Override
  public void displayEatFood(String foodName, int healthGain, int newHealth) {
    javafx.application.Platform.runLater(() -> {
      healthLabel.setText("❤ " + newHealth);
      outputTextArea.appendText("You ate " + foodName + ".\n");
      outputTextArea.appendText(
        "Your health increased by " + healthGain + ".\n"
      );
      outputTextArea.appendText("You now have " + newHealth + " health.\n");
    });
  }

  @Override
  public void displayNoItemToConsume() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You don't have that.\n");
    });
  }

  @Override
  public void displayCannotConsume() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You can't drink that!\n");
    });
  }

  @Override
  public void displayCannotEat() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You can't eat that!\n");
    });
  }

  @Override
  public void displayNPCDialogue(String npcName, String dialogue) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(npcName + ": \"" + dialogue + "\"\n");

      if (npcName.equalsIgnoreCase("Mario")) {
        playAudio("/audio/mlrpg3_mario_gibberish_4.wav");
      }
    });
  }

  @Override
  public void displayNoNPCHere() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("There is no one here to talk to.\n");
    });
  }

  @Override
  public void displayPurchaseSuccess(
    String itemName,
    int price,
    int remainingMoney
  ) {
    javafx.application.Platform.runLater(() -> {
      moneyLabel.setText("💰 €" + remainingMoney);
      outputTextArea.appendText(
        "You purchased " + itemName + " for " + price + " euro.\n"
      );
      outputTextArea.appendText(
        "Remaining money: " + remainingMoney + " euro\n"
      );
    });
  }

  @Override
  public void displayPurchaseFailure(String reason) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(reason + "\n");
    });
  }

  @Override
  public void displayCabRide() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You get into the cab.\n");
      outputTextArea.appendText("The cab drops you off outside your house.\n");
    });
  }

  @Override
  public void displayDiceGameIntro(int minBet, int maxBet, int playerMoney) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("\n" + "=".repeat(50) + "\n");
      outputTextArea.appendText("DICE GAME with Bob\n");
      outputTextArea.appendText("=".repeat(50) + "\n");
      outputTextArea.appendText(
        "Rules: Highest roll wins. You both roll 2 dice.\n"
      );
      outputTextArea.appendText("Bet: €" + minBet + " to €" + maxBet + "\n");
      outputTextArea.appendText("Your money: €" + playerMoney + "\n");
      outputTextArea.appendText("=".repeat(50) + "\n\n");
    });
  }

  @Override
  public void displayDiceGameRolls(int playerRoll, int bobRoll) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("\n --- Rolling dice... ---\n");
      outputTextArea.appendText("You rolled: " + playerRoll + "\n");
      outputTextArea.appendText("Bob rolled: " + bobRoll + "\n");
    });
  }

  @Override
  public void displayDiceGameResult(
    int result,
    int playerMoney,
    String bobLoseDialogue
  ) {
    javafx.application.Platform.runLater(() -> {
      if (result > 0) {
        outputTextArea.appendText("You win! You gain €" + result + ".\n");
      } else if (result < 0) {
        outputTextArea.appendText("Bob: " + bobLoseDialogue + "\n");
        outputTextArea.appendText("You lose €" + Math.abs(result) + ".\n");
      } else if (result == 0) {
        outputTextArea.appendText("It's a draw! No money won or lost.\n");
      }

      outputTextArea.appendText("Your current money: €" + playerMoney + "\n");
      outputTextArea.appendText("\nType 'play dice' to play again!\n");
      outputTextArea.appendText("=".repeat(50) + "\n\n");
    });
  }

  @Override
  public void displayInvalidBet() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Invalid bet amount! Please enter a number.\n");
    });
  }

  @Override
  public void displayDiceGameQuit(String bobDialogue) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Bob: " + bobDialogue + "\n");
    });
  }

  @Override
  public void displayBillyIntro() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("\n" + "=".repeat(50) + "\n");
      outputTextArea.appendText(
        "A shadowy figure steps out from the darkness...\n"
      );
      outputTextArea.appendText("It's Billy! He's blocking your path!\n");
      outputTextArea.appendText("=".repeat(50) + "\n");
    });
  }

  @Override
  public void displayBillyWeaponThreat(String weaponName, boolean isBottle) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("\nYou pull out your " + weaponName + "!\n");
      if (isBottle) {
        outputTextArea.appendText("You smash the bottle off the wall!\n");
      }
      outputTextArea.appendText("Billy eyes it nervously...\n");
    });
  }

  @Override
  public void displayBillyWeaponResponse(int damage) {
    javafx.application.Platform.runLater(() -> {
      if (damage >= 15) {
        outputTextArea.appendText(
          "\"Alright, alright! Just give me a bit and I'll leave you alone!\"\n"
        );
      } else {
        outputTextArea.appendText(
          "\"That's not gonna stop me, but I'll go easy on ya!\"\n"
        );
      }
    });
  }

  @Override
  public void displayBillyRobberyWithWeapon(int moneyStolen) {
    javafx.application.Platform.runLater(() -> {
      if (moneyStolen > 0) {
        outputTextArea.appendText(
          "\nBilly snatches " + moneyStolen + " from your wallet!\n"
        );
      }
      outputTextArea.appendText(
        "Billy grabs some of your items and tosses them on the ground!\n"
      );
      outputTextArea.appendText(
        "\"Consider yourself lucky!\" Billy disappears into the shadows.\n"
      );
    });
  }

  @Override
  public void displayBillyRobberyNoWeapon(int moneyStolen) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(
        "\nYou have nothing to defend yourself with!\n"
      );
      outputTextArea.appendText("Billy shoves you against the wall...\n");
      if (moneyStolen > 0) {
        outputTextArea.appendText(
          "\nBilly empties your wallet! " + moneyStolen + " stolen!\n"
        );
      }
      outputTextArea.appendText(
        "Billy rummages through your pockets and throws everything on the ground!\n"
      );
      outputTextArea.appendText(
        "\n\"That'll teach ya to wander into my alley unarmed!\"\n"
      );
      outputTextArea.appendText("Billy runs off laughing...\n");
    });
  }

  @Override
  public void displayBillyEscape() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(
        "\nYou dust yourself off. Your items are scattered on the ground here.\n"
      );
      outputTextArea.appendText("=".repeat(50) + "\n\n");
    });
  }

  @Override
  public void displayDoorLocked() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("The door is locked!\n");
    });
  }

  @Override
  public void displayDoorUnlocked() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(
        "You quietly unlock the door with your key...\n"
      );
    });
  }

  @Override
  public void displayKnock() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You knock on the door...\n");
      outputTextArea.appendText("Your wife opens the door and greets you.\n");
    });
  }

  @Override
  public void displayNothingToKnock() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("There is nothing to knock here.\n");
    });
  }

  @Override
  public void displaySaveSuccess() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Game saved successfully.\n");
    });
  }

  @Override
  public void displayLoadSuccess(String playerName) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(
        "Game loaded successfully! Welcome back, " + playerName + "!\n"
      );
    });
  }

  @Override
  public void displayLoadFailure() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("No saved game found. Starting a new game.\n");
    });
  }

  @Override
  public void displayEnding(WifeRequirements wifeReq, Player player) {
    javafx.application.Platform.runLater(() -> {
      // Stop background music so ending audio can play
      if (backgroundMusicPlayer != null) {
        backgroundMusicPlayer.stop();
        backgroundMusicPlayer.dispose();
      }

      // Display wife image based on score
      int score = wifeReq.calculateScore();
      String wifeImageName = getWifeImageName(score);
      roomImageView.setImage(loadWifeImage(wifeImageName));

      outputTextArea.appendText("\n" + "=".repeat(50) + "\n");
      outputTextArea.appendText("You encounter Herself...\n");
      outputTextArea.appendText("=".repeat(50) + "\n\n");

      outputTextArea.appendText(wifeReq.getScoreBreakdown() + "\n");
      outputTextArea.appendText(wifeReq.getEndingMessage() + "\n\n");

      outputTextArea.appendText("=".repeat(50) + "\n\n");
      if (wifeReq.isWifeHappy()) {
        outputTextArea.appendText(
          "Congratulations! You have successfully pleased your wife and avoided total war.\n"
        );
      } else {
        outputTextArea.appendText(
          "Oh no! Your wife is not happy. Better find a pillow and blanket for the couch tonight.\n"
        );
      }

      outputTextArea.appendText("=".repeat(50) + "\n");
      outputTextArea.appendText("\n=== GAME OVER ===\n");
      outputTextArea.appendText("Thanks for playing The Usual!\n");
      outputTextArea.appendText("\nFinal Stats:\n");
      outputTextArea.appendText(
        "- Pints drunk: " + player.getPintCount() + "\n"
      );
      outputTextArea.appendText("- Money left: " + player.getMoney() + "\n");
      outputTextArea.appendText("- Health: " + player.getHealth() + "\n");
      outputTextArea.appendText(
        "- Time arrived home: " + player.getCurrentTimeFormatted() + "\n"
      );
    });
  }

  @Override
  public String getUserInput(String prompt) {
    // For GUI, display the prompt and next input captured via callback
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(prompt + "\n");
    });

    return "";
  }

  public void requestUserInput(
    String prompt,
    java.util.function.Consumer<String> callback
  ) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText(prompt + "\n");
      this.inputCallback = callback;
    });
  }

  @Override
  public String getUserInput() {
    return getUserInput("Enter your response:");
  }

  @Override
  public void displayItemAddedToInventory(String itemName) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Added to inventory: " + itemName + "\n");
    });
  }

  @Override
  public void displayItemRemovedFromInventory(String itemName) {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Removed from inventory: " + itemName + "\n");
    });
  }

  @Override
  public void displayMoneyGained(int amount, int newTotal) {
    javafx.application.Platform.runLater(() -> {
      moneyLabel.setText("💰 €" + newTotal);
      outputTextArea.appendText(
        "You received " + amount + " euro. Total money: " + newTotal + "\n"
      );
    });
  }

  @Override
  public void displayMoneyLost(int amount, int remaining) {
    javafx.application.Platform.runLater(() -> {
      moneyLabel.setText("💰 €" + remaining);
      outputTextArea.appendText(
        "You lost " + amount + " euro. Remaining money: " + remaining + "\n"
      );
    });
  }

  @Override
  public void displayHealthLost(int amount, int remaining) {
    javafx.application.Platform.runLater(() -> {
      healthLabel.setText("❤ " + remaining);
      outputTextArea.appendText(
        "You lost " + amount + " health. Remaining health: " + remaining + "\n"
      );
    });
  }

  @Override
  public void displayHealthGained(int amount, int remaining) {
    javafx.application.Platform.runLater(() -> {
      healthLabel.setText("❤ " + remaining);
      outputTextArea.appendText(
        "You gained " +
        amount +
        " health. Remaining health: " +
        remaining +
        "\n"
      );
    });
  }

  @Override
  public void displayMaybeNextTime() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Maybe next time!\n");
    });
  }

  @Override
  public void displayGoodbye() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("\"See you around so!\"\n");
      playAudio("/audio/mario-64-ds-_bye-bye_-made-with-Voicemod.mp3");
    });
  }

  @Override
  public void displayUnknownResponse() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("I don't understand your response.\n");
    });
  }

  @Override
  public void displayCabPrompt() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Do you want to go home? (yes/no)\n");
    });
  }

  @Override
  public void displayStayOut() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You decide to stay out a bit longer.\n");
    });
  }

  @Override
  public void displayUnknownGame() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("Don't know that game.\n");
    });
  }

  @Override
  public void displayNotInInventory() {
    javafx.application.Platform.runLater(() -> {
      outputTextArea.appendText("You don't have that item.\n");
    });
  }

  private String getRoomImageName(Room room) {
    String location = room.getLocationDescription();

    if (location.contains("in the middle of town")) {
      return "betterOutside.png";
    } else if (location.contains("in Mario's Chipper")) {
      // Check if hurl is in the room
      boolean hasHurl = room
        .getItemsInRoom()
        .stream()
        .anyMatch(item -> item.getName().equalsIgnoreCase("hurl"));
      return hasHurl ? "chipper.png" : "chipperNoHurl.png";
    } else if (location.contains("in Corrigan's Bar")) {
      return "bobPub.png";
    } else if (location.contains("outside a cab")) {
      return "taxi.png";
    } else if (location.contains("finally home")) {
      return "outsideHouse.png";
    } else if (location.contains("in a dark and creepy alleyway")) {
      return "alleyway.png";
    } else if (
      location.contains("in a bathroom") &&
      room.getLongDescription().contains("reasons")
    ) {
      return "pubBathroom.png";
    } else if (
      location.contains("in a bathroom") &&
      room.getLongDescription().contains("horrible")
    ) {
      return "chipperBathroom.png";
    } else if (location.contains("outside the house")) {
      return "outsideHouse.png";
    } else if (location.contains("at the bar counter")) {
      return "atBar.png";
    } else if (location.contains("at the chipper counter")) {
      return "chipperCounter.png";
    } else {
      return null;
    }
  }

  private String getMapImageName(Room room) {
    // Use the single map image for all locations
    return "Map.png";
  }

  private String getWifeImageName(int score) {
    if (score >= 5) {
      playAudio("/audio/52ac54_super_mario_bros_stage_clear_sound_effect.mp3");
      return "wifeHappy.png";
    } else if (score >= 3) {
      playAudio("/audio/neutral.mp3");
      return "wifeNeutral.png";
    } else if (score >= 1) {
      playAudio("/audio/super-mario-bros_2.mp3");
      return "wifeMad.png";
    } else {
      playAudio("/audio/26-game-over-1.mp3");
      return "wifeFurious.png";
    }
  }

  private javafx.scene.image.Image loadImage(
    String imagePath,
    int width,
    int height
  ) {
    if (imagePath == null) {
      System.out.println("Image path is null, returning blank image");
      return createBlankImage(width, height);
    }

    try {
      System.out.println("Attempting to load image: " + imagePath);
      java.io.InputStream stream = getClass().getResourceAsStream(imagePath);
      if (stream == null) {
        System.err.println("Resource not found: " + imagePath);
        return createBlankImage(width, height);
      }

      javafx.scene.image.Image image = new javafx.scene.image.Image(
        stream,
        width,
        height,
        true,
        false
      );

      if (image.isError()) {
        System.err.println("Image loading error: " + imagePath);
        return createBlankImage(width, height);
      }

      System.out.println("Successfully loaded: " + imagePath);
      return image;
    } catch (Exception e) {
      System.err.println("Failed to load image: " + imagePath);
      e.printStackTrace();
      return createBlankImage(width, height);
    }
  }

  private javafx.scene.image.Image loadRoomImage(Room room) {
    String imageName = getRoomImageName(room);
    System.out.println(
      "Loading room image for: " + room.getLocationDescription()
    );
    System.out.println("Image name: " + imageName);
    if (imageName == null) {
      System.err.println("No image name found for room");
      return createBlankImage(600, 400);
    }
    return loadImage("/images/rooms/" + imageName, 600, 400);
  }

  private javafx.scene.image.Image loadMapImage(Room room) {
    String imageName = getMapImageName(room);
    if (imageName == null) {
      return createBlankImage(300, 225);
    }
    return loadImage("/images/maps/" + imageName, 300, 225);
  }

  private javafx.scene.image.Image loadWifeImage(String imageName) {
    if (imageName == null) {
      return createBlankImage(600, 400);
    }
    return loadImage("/images/wife/" + imageName, 600, 400);
  }

  private javafx.scene.image.Image loadItemIcon(Item item) {
    String itemName = item.getName().toLowerCase();
    return loadImage("/images/items/" + itemName + ".png", 32, 32);
  }

  private javafx.scene.image.Image createBlankImage(int width, int height) {
    return new javafx.scene.image.WritableImage(width, height);
  }

  private void playAudio(String audioPath) {
    try {
      // Stop any currently playing audio
      if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
      }

      // Load and play the new audio
      String audioResource = getClass().getResource(audioPath).toExternalForm();
      Media media = new Media(audioResource);
      mediaPlayer = new MediaPlayer(media);

      // Set volume (optional, adjust between 0.0 and 1.0)
      mediaPlayer.setVolume(0.7);

      // Auto-stop at end
      mediaPlayer.setOnEndOfMedia(() -> {
        mediaPlayer.stop();
      });

      mediaPlayer.play();
      System.out.println("Playing audio: " + audioPath);
    } catch (Exception e) {
      System.err.println("Failed to play audio: " + audioPath);
      e.printStackTrace();
    }
  }

  // Public method for GameController to trigger audio
  public void playAudioFromController(String audioPath) {
    javafx.application.Platform.runLater(() -> {
      playAudio(audioPath);
    });
  }

  private void startBackgroundMusic() {
    try {
      System.out.println("Starting background music...");
      String musicPath = "/audio/62. Staff Roll.mp3";
      String audioResource = getClass().getResource(musicPath).toExternalForm();
      Media media = new Media(audioResource);
      backgroundMusicPlayer = new MediaPlayer(media);

      // Set volume lower for background music (adjust between 0.0 and 1.0)
      backgroundMusicPlayer.setVolume(0.6);

      // Loop the music indefinitely
      backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

      backgroundMusicPlayer.play();
      System.out.println("Background music started");
    } catch (Exception e) {
      System.err.println("Failed to start background music");
      e.printStackTrace();
    }
  }
}
