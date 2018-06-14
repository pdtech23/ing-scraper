package challange;

import challange.account.Account;
import challange.ing.scraper.IIngScraper;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;
import challange.user.experience.IConsoleUI;

import java.util.List;

public class FetchAccountsUseCase {

  private final IConsoleUI ui;
  private final IIngScraper scraper;

  private List<Account> accounts;
  private AuthenticatedSession authenticatedSession;

  public FetchAccountsUseCase(IConsoleUI ui, IIngScraper scraper) {
    this.ui = ui;
    this.scraper = scraper;
  }

  public void execute() {
    ui.displayWelcomeMessage();
    login();
    scrapAccounts();
    ui.printAccounts(accounts);
  }

  private void login() {
    String login = ui.askUserForLogin();
    UnauthenticatedSession unauthenticatedSession = scraper.fetchUnauthenticatedSession(login);
    char[] password = ui.askUserForNeededPasswordChars(unauthenticatedSession.positionsOfRevealedCharacters);
    authenticatedSession = scraper.fetchAuthenticatedSession(login, password, unauthenticatedSession);
  }

  private void scrapAccounts() {
    accounts = scraper.fetchAccounts(authenticatedSession);
  }
}
