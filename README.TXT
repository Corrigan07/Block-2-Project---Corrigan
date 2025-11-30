# The Usual - JavaZork Text Adventure Game

A JavaFX-based text adventure game where you navigate a night out at the pub and try to make it home in time to keep your wife happy!

## Game Story

It's Friday evening, and you're starting outside your favourite pub. Your objective is to enjoy the night, manage your money and health, and get home before it's too late. Make the right choices to keep your wife happy!

## Requirements

- **Java JDK 24** (or compatible version)
- **Maven 3.x** for building
- **JavaFX SDK 25.0.1** (already bundled in the executable JAR)

## Quick Start - Using the Executable JAR

The easiest way to play:

1. Navigate to the `target` folder
2. Run the executable JAR:
   ```
   java -jar JavaZork-executable.jar
   ```

That's it! The JAR includes all dependencies.

## Building from Source

### Compile the Project

```bash
mvn clean package
```

This creates two JAR files in the `target` folder:
- `JavaZork-1.0-SNAPSHOT.jar` - Standard JAR (requires JavaFX on module path)
- `JavaZork-executable.jar` - Fat JAR with all dependencies bundled

### Run Without Building (Console Version)

```bash
mvn compile exec:java -Dexec.mainClass="com.zork.ZorkULGame"
```

### Run the GUI Version (if you have JavaFX SDK)

```bash
java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp target/JavaZork-1.0-SNAPSHOT.jar com.zork.GuiMain
```

Replace `path/to/javafx-sdk/lib` with your actual JavaFX SDK lib folder path.

## How to Play

### Starting the Game

When you launch the game, you'll be prompted to:
1. Start a **new game** or **load** a saved game (type `new` or `load`)
2. Enter your character name

### Game Commands

- `go [direction]` - Move between locations (north, south, east, west, order, back)
- `look` - Examine your current location
- `inventory` - View items you're carrying
- `collect [item]` - Pick up an item
- `drop [item]` - Drop an item from inventory
- `eat [food]` - Consume food to restore health
- `drink [beverage]` - Drink something (affects health/stats)
- `talk [npc]` - Speak with characters
- `check [item]` - Examine an item
- `open [box]` - Open containers
- `move [item]` - Move objects
- `play dice` - Play dice game with Bob at the pub
- `knock` - Knock on a door
- `save` - Save your game progress
- `quit` - Exit the game
- `help` - Display available commands

### Game Mechanics

**Health**: Starts at 100. Drinking pints decreases health, eating food restores it.

**Money**: You start with cash to spend at the pub and chipper. Manage it wisely!

**Time**: Time passes as you play. Getting home late upsets your wife!

**Inventory**: Collect items throughout your journey. Some are required to progress.

**Scoring**: Your wife's happiness is based on:
- Bringing home food
- Having your house key (not knocking)
- Limiting pints consumed (max 5)
- Maintaining health (minimum 50)
- Getting home on time (before 11 PM)
- Keeping enough money (minimum €40)

### Tips

- Visit the pub and chipper to buy items
- Talk to NPCs for clues and interactions
- Watch your health and money
- Save your game regularly
- Avoid the dark alleyway unless prepared!
- Win money playing dice with Bob

## Game Locations

- **Outside** - The starting point between pub and chipper
- **Corrigan's Bar** - The local pub with Bob the bartender
- **Mario's Chipper** - Get food here
- **Cab Stand** - Take a taxi home
- **Alleyway** - A dangerous shortcut (beware of Billy!)
- **Your House** - The final destination

## Technical Features

This game demonstrates key Java programming concepts:

- **Object-Oriented Design**: Classes, inheritance, interfaces, polymorphism
- **JavaFX GUI**: Rich graphical interface with images and status displays
- **MVC Pattern**: Model-View-Controller architecture separating concerns
- **Collections Framework**: List, Map, ArrayList, HashMap for data management
- **Enums**: CommandType and Direction enums with string conversion
- **Generics**: Box<T extends Item> generic container class
- **Exception Handling**: Try-catch blocks for robust error management
- **Serialization**: Save/load game state using Java I/O
- **Input Parsing**: Command parser with tokenization
- **Annotations**: @Override and @FXML annotations throughout

## Project Structure

```
src/main/java/com/zork/
├── Character.java          - Base character class
├── Player.java             - Player character with inventory
├── NPC.java                - Non-player characters
├── Room.java               - Game locations
├── Item.java               - Item interface
├── Food.java               - Consumable food items
├── Drink.java              - Beverages
├── Weapon.java             - Combat items
├── Box.java                - Generic container (demonstrates generics)
├── GameController.java     - Game logic and command processing
├── GameView.java           - View interface
├── ConsoleView.java        - Text-based view
├── GuiView.java            - JavaFX graphical view
├── Parser.java             - Command parsing
├── Command.java            - Command objects
├── CommandType.java        - Command enum
├── Direction.java          - Direction enum
├── WifeRequirements.java   - End game scoring system
├── DiceGame.java           - Mini-game logic
├── ZorkULGame.java         - Console launcher
└── GuiMain.java            - GUI launcher

src/main/resources/
├── fxml/GUI.fxml           - JavaFX layout
├── images/                 - Game graphics
│   ├── rooms/
│   ├── maps/
│   ├── items/
│   └── wife/
└── fonts/                  - Custom fonts
```

## Save Files

Game progress is saved to `player.ser` in the project root directory.

## Troubleshooting

**JavaFX Warning**: If you see JavaFX configuration warnings, these are normal and don't affect gameplay.

**Can't load images**: Ensure images are in `src/main/resources/images/` folders.

**Save file not found**: Use "new" when starting for the first time.

## Credits

Developed for Java Programming Assignment - Block 2 Project
Demonstrates advanced Java concepts including OOP, JavaFX, generics, and design patterns.

## License

Educational project - created for academic purposes.


