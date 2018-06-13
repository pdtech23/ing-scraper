package scraper.user.experience;

import scraper.account.Account;
import scraper.ing.security.PasswordBehaviorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public class ConsoleUserInterface {

  private static final PrintStream PRINTER = System.out;
  private final BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

  public void displayWelcomeMessage() {
    PRINTER.println("Hello, we will try to get your account data here!\n(ps. we only " + "support ING Bank here)");
  }

  public String askUserForLogin() {
    PRINTER.println("Type in Your login please:");
    try {
      return userInputReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed user login reading.");
    }
  }

  public char[] askUserForNeededPasswordCharacters(List<Integer> positionsOfRevealedCharacters) {
    char[] passphrase = new char[PasswordBehaviorHandler.NUMBER_OF_REVEALED_CHARACTERS];
    for (int i = 0; i < PasswordBehaviorHandler.NUMBER_OF_REVEALED_CHARACTERS; ++i) {
      PRINTER.println("Give me character no. " + positionsOfRevealedCharacters.get(i) + " of your password:");
      String failureMessage = "Failed user password reading.";
      try {
        String input = userInputReader.readLine();
        if (input.isEmpty()) {
          throw new RuntimeException(failureMessage);
        }
        passphrase[i] = input.charAt(0);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(failureMessage);
      }
    }
    return passphrase;
  }

  public void printAccounts(List<Account> accounts) {
    PRINTER.println("Your accounts:");
    accounts.forEach(PRINTER::println);
  }
}
