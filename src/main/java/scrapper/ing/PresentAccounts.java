package scrapper.ing;

import scrapper.ing.client.Connection;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.UnauthenticatedSession;
import scrapper.user.experience.ConsoleUserInterface;

import java.util.List;

public class PresentAccounts {

  private final ConsoleUserInterface userInterface;
  private final Connection connection;

  public PresentAccounts(ConsoleUserInterface userInterface, Connection connection) {
    this.userInterface = userInterface;
    this.connection = connection;
  }

  public void displayAccountsWithUserInteraction() {
    userInterface.displayWelcomeMessage();
    String login = userInterface.askUserForLogin();
    UnauthenticatedSession unauthenticatedSession = connection.createUnauthenticatedSession(login);
    List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
        (unauthenticatedSession.mask);
    char[] password = userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);
    AuthenticatedSession authenticatedSession = connection.createAuthenticatedSession(login, password,
        unauthenticatedSession);
    userInterface.printAccounts(connection.getAccountsInfo(authenticatedSession));
  }
}
