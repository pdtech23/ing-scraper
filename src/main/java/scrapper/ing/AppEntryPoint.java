package scrapper.ing;

import org.json.JSONException;
import scrapper.ing.userInterface.ConsoleUserInterface;

public class AppEntryPoint {

    public static void main(String[] args) throws JSONException {

        ConsoleUserInterface ui = new ConsoleUserInterface();

        while (ui.isNotDone()) {
            ui.interactWithUser();
        }
    }

}
