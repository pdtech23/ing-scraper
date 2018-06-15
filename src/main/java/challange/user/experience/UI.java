package challange.user.experience;

import challange.model.Account;

import java.util.List;

public interface UI {
  void displayWelcomeMessage();

  String askUserForLogin();

  char[] askUserForNeededPasswordChars(List<Integer> positionsOfRevealedCharacters);

  void displayAccounts(List<Account> accounts);
}
