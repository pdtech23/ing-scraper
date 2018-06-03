package scrapper.ing.userInterface;

import org.json.JSONException;
import org.json.JSONObject;
import scrapper.ing.client.IngClientDataExtractorService;
import scrapper.ing.utils.PasswordUtils;

import java.util.List;

public class ConsoleUserInterface {
    private final IngClientDataExtractorService clientDataExtractor;

    private boolean isFinished;

    public ConsoleUserInterface() {
        this.isFinished = false;
        this.clientDataExtractor = new IngClientDataExtractorService();
    }

    private static void displayWelcomeMessage() {
        System.out.println("Hello, we will try to get your account data here!");
        System.out.println("(ps. we only support ING Bank... for now)");
    }

    private static String getUserLogin() {
        System.out.println("Give me Your login please:");
        return System.console().readLine();
    }

    public void interactWithUser() throws JSONException {
        displayWelcomeMessage();
        String login = getUserLogin();
        JSONObject requestForLoginCheckResult = this.clientDataExtractor.getRequestForLoginCheckResult(login);
        JSONObject data = (JSONObject) requestForLoginCheckResult.get("data");
        String salt = (String) data.get("salt");
        String mask = (String) data.get("mask");
        String key = (String) data.get("key");
        List<Integer> revealedPasswordCharactersIndexes = PasswordUtils.extractStarIndexesFromMask(mask);

        // TODO: use API to get mask to ask for proper password characters
        List<Character> password = getNeededPasswordCharacters(revealedPasswordCharactersIndexes);


    }

    private List<Character> getNeededPasswordCharacters(List<Integer> mask) {
        System.out.println("Give me Your login please:");
        return "11111";
    }

    public boolean isNotDone() {
        return !this.isFinished;
    }
}
