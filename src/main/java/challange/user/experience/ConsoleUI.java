package challange.user.experience;

import challange.account.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;

public class ConsoleUI implements IConsoleUI {

  private static final String PASSPHRASE_READING_FAILURE_MESSAGE = "Failed user password reading.";
  private final BufferedReader userInputReader;

  public ConsoleUI() {
    userInputReader = new BufferedReader(new InputStreamReader(System.in));
  }

  @Override
  public void displayWelcomeMessage() {
    System.out.println("Hello, we will try to get your account data here!\n(ps. we only support ING Bank here)");
  }

  @Override
  public String askUserForLogin() {
    System.out.println("Type in your login please:");
    try {
      String login = userInputReader.readLine();
      if (login.isEmpty())
        throw new IllegalArgumentException("User login cannot be empty!");
      return login;
    } catch (IOException e) {
      throw new UncheckedIOException("Failed user login reading. Details:", e);
    }
  }

  @Override
  public char[] askUserForNeededPasswordChars(List<Integer> positionsOfRevealedCharacters) {
    try {
      char[] passphrase = new char[positionsOfRevealedCharacters.size()];
      for (int i = 0; i < passphrase.length; ++i) {
        System.out.println("Give me character no. " + positionsOfRevealedCharacters.get(i) + " of your password:");
        String line = userInputReader.readLine();
        if (line.isEmpty())
          throw new IllegalArgumentException("Password character cannot be empty!");
        passphrase[i] = line.charAt(0);
      }
      return passphrase;
    } catch (IOException e) {
      throw new UncheckedIOException(PASSPHRASE_READING_FAILURE_MESSAGE, e);
    }
  }

  @Override
  public void printAccounts(List<Account> accounts) {
    System.out.println("Your accounts:");
    accounts.forEach(System.out::println);
  }
}
