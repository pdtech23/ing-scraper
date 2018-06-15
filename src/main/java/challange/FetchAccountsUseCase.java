package challange;

import challange.account.Account;
import challange.ing.scraper.Scraper;
import challange.ing.session.AuthenticatedSession;
import challange.ing.session.UnauthenticatedSession;
import challange.user.experience.UI;

import java.util.List;

public class FetchAccountsUseCase {

  private final UI ui;
  private final Scraper scraper;

  private List<Account> accounts;
  private AuthenticatedSession authenticatedSession;

  public FetchAccountsUseCase(UI ui, Scraper scraper) {
    this.ui = ui;
    this.scraper = scraper;
  }

  public void execute() {
    ui.displayWelcomeMessage();
    login();
    scrapAccounts();
    ui.displayAccounts(accounts);
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
