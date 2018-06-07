package scrapper.ing.user.experience;

import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.security.PasswordBehaviorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleUserInterface {

    static final String PASSPHRASE_QUESTION_PREFIX = "Give me character no. ";
    static final String PASSPHRASE_QUESTION_POSTFIX = " of your password:";
    static final String FAILED_LOGIN_ATTEMPT_MESSAGE = "Failed login attempt.";
    static final String WELCOME_MESSAGE = "Hello, we will try to get your account data here!\n(ps. we only " +
            "support ING Bank here)";
    static final String ASK_FOR_LOGIN_MESSAGE = "Type in Your login please:";

    private final BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

    public void displayWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public String askUserForLogin() {
        System.out.println(ASK_FOR_LOGIN_MESSAGE);

        try {
            return this.userInputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public char[] askUserForNeededPasswordCharacters(List<Integer> positionsOfRevealedCharacters) {

        char[] passphrase = new char[PasswordBehaviorHandler.NUMBER_OF_REVEALED_CHARACTERS];

        for (int i = 0; i < PasswordBehaviorHandler.NUMBER_OF_REVEALED_CHARACTERS; ++i) {
            System.out.println(PASSPHRASE_QUESTION_PREFIX + positionsOfRevealedCharacters.get(i) +
                    PASSPHRASE_QUESTION_POSTFIX);
            try {
                String input = this.userInputReader.readLine();
                if (input.isEmpty()) {
                    return new char[0];
                }
                passphrase[i] = input.charAt(0);
            } catch (IOException | StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                return new char[0];
            }
        }
        return passphrase;
    }

    public void displayFailureMessage() {
        System.out.println(FAILED_LOGIN_ATTEMPT_MESSAGE);

    }

    public void printAccounts(List<IngAccountInfo> accounts) {
        System.out.println("Your accounts:");
        accounts.forEach(System.out::println);
    }
}
