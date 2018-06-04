package scrapper.ing;

import org.json.JSONException;
import scrapper.ing.userInterface.ConsoleUserInterface;

import java.io.IOException;

public class AppEntryPoint {

    public static void main(String[] args) throws JSONException, IOException {

        ConsoleUserInterface ui = new ConsoleUserInterface();

        while (ui.isNotDone()) {
            ui.interactWithUser();
        }
    }

}
