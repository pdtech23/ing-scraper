package challange.user.experience;

import challange.account.Account;

import java.util.List;

public interface IConsoleUI {
  void displayWelcomeMessage();

  String askUserForLogin();

  char[] askUserForNeededPasswordChars(List<Integer> positionsOfRevealedCharacters);

  void printAccounts(List<Account> accounts);
}
