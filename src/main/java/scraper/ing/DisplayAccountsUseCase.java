package scraper.ing;

import scraper.ing.client.IngScraper;
import scraper.ing.security.AuthenticatedSession;
import scraper.ing.security.PasswordBehaviorHandler;
import scraper.ing.security.UnauthenticatedSession;
import scraper.user.experience.ConsoleUserInterface;

import java.util.List;

public class DisplayAccountsUseCase {

  private final ConsoleUserInterface userInterface;
  private final IngScraper ingScraper;

  public DisplayAccountsUseCase(ConsoleUserInterface userInterface, IngScraper ingScraper) {
    this.userInterface = userInterface;
    this.ingScraper = ingScraper;
  }

  public void displayAccountsWithUserInteraction() {
    userInterface.displayWelcomeMessage();
    String login = userInterface.askUserForLogin();
    UnauthenticatedSession unauthenticatedSession = ingScraper.createUnauthenticatedSession(login);
    List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
        (unauthenticatedSession.mask);
    char[] password = userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);
    AuthenticatedSession authenticatedSession = ingScraper.createAuthenticatedSession(login, password,
        unauthenticatedSession);
    userInterface.printAccounts(ingScraper.getAccountsInfo(authenticatedSession));
  }
}
