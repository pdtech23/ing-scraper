package scrapper.user.experience;

import scrapper.account.Account;
import scrapper.ing.security.PasswordBehaviorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public class ConsoleUserInterface {

    private static final PrintStream printer = System.out;
    private final BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

    public void displayWelcomeMessage() {
        printer.println("Hello, we will try to get your account data here!\n(ps. we only " + "support ING Bank here)");
    }

    public String askUserForLogin() {
        printer.println("Type in Your login please:");

        try {
            return userInputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public char[] askUserForNeededPasswordCharacters(List<Integer> positionsOfRevealedCharacters) {

        char[] passphrase = new char[PasswordBehaviorHandler.NUMBER_OF_REVEALED_CHARACTERS];

        for (int i = 0; i < PasswordBehaviorHandler.NUMBER_OF_REVEALED_CHARACTERS; ++i) {
            printer.println("Give me character no. " + positionsOfRevealedCharacters.get(i) + " of your password:");

            try {
                String input = userInputReader.readLine();
                if (input.isEmpty()) {
                    return new char[0];
                }
                passphrase[i] = input.charAt(0);
            } catch (IOException e) {
                e.printStackTrace();
                return new char[0];
            }
        }
        return passphrase;
    }

    public void displayFailureMessage() {
        printer.println("Failed login attempt.");

    }

    public void printAccounts(List<Account> accounts) {
        printer.println("Your accounts:");
        accounts.forEach(printer::println);
    }
}
