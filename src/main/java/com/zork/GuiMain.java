package com.zork;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Very small JavaFX launcher — only a display area and a single input field.
 * Keeps the UI intentionally minimal so you can wire the game logic later.
 */
public class GuiMain extends Application {

  @Override
  public void start(Stage stage) {
    BorderPane root = new BorderPane();

    TextArea output = new TextArea();
    output.setEditable(false);
    output.setWrapText(true);
    output.setText("Welcome to The Usual (minimal GUI)\n\n");

    HBox bottom = new HBox(6);
    TextField input = new TextField();
    Button send = new Button("Send");
    HBox.setHgrow(input, Priority.ALWAYS);

    send.setOnAction(e -> {
      String text = input.getText();
      if (text != null && !text.isBlank()) {
        output.appendText("\n> " + text + "\n");
        input.clear();
      }
    });

    bottom.getChildren().addAll(input, send);

    root.setCenter(output);
    root.setBottom(bottom);

    Scene scene = new Scene(root, 800, 500);
    stage.setScene(scene);
    stage.setTitle("The Usual - Minimal GUI");
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
