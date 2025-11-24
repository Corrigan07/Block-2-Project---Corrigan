JavaZork — running the JavaFX GUI (Windows)
==========================================

This project uses OpenJFX for the GUI. The easiest way to run the GUI locally is to download the JavaFX SDK and run Java with the SDK `lib` on the module-path.

Quick steps (PowerShell)
-------------------------

1. Download and extract the JavaFX SDK that matches your platform. Example SDK you downloaded:

   C:\Users\seanc\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1

2. Build the project:

   ```powershell
   mvn -U clean package
   ```

3. Run the GUI (adjust the SDK path if you installed somewhere else):

   ```powershell
   java --module-path "C:\Users\seanc\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp target\JavaZork-1.0-SNAPSHOT.jar com.zork.GuiMain
   ```

Convenience scripts
-------------------
Two scripts are included to make repeated runs easier:

- `run_gui.ps1` — PowerShell script (recommended for interactive development)
- `run_gui.bat` — Windows batch script (double-click or run from cmd)

Both scripts will build the project if the artifact isn't present and then run the main class.

Permanent environment variable (optional)
---------------------------------------
If you want shorter commands during a session, set a session variable in PowerShell:

```powershell
$env:FXLIB = "C:\Users\seanc\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib"
java --module-path $env:FXLIB --add-modules javafx.controls,javafx.fxml -cp target\JavaZork-1.0-SNAPSHOT.jar com.zork.GuiMain
```

To make this permanent, add the variable to your Windows System Environment Variables or add the line to your PowerShell profile (`$PROFILE`).

Notes
-----
- The `--module-path` must point to the SDK `lib` folder that contains `javafx-controls.jar` etc.
- If you get `Module javafx.controls not found`, verify the `lib` path and that the JavaFX SDK matches your JVM (you are using Java 24/25, so OpenJFX 25 is appropriate).

If you want, I can also add an `exec-maven-plugin` entry to `pom.xml` so you can run `mvn exec:java` with the correct args — tell me if you'd like that.
