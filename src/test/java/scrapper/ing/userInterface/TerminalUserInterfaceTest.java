package scrapper.ing.userInterface;

import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalUserInterfaceTest {

    @Test
    public void shouldNotBeDoneJustAfterCreation() {
        // given
        ConsoleUserInterface freshlyCreatedUI = new ConsoleUserInterface();

        // when
        boolean result = freshlyCreatedUI.isNotDone();

        // then
        assertTrue(result);
    }

    @Test
    public void shouldBeDoneAfterInteractingWithUser() throws JSONException {
        // given
        ConsoleUserInterface userInterface = new ConsoleUserInterface();
        userInterface.interactWithUser();

        // when
        boolean result = userInterface.isNotDone();

        // then
        assertTrue(result);
    }

}