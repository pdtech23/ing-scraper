package challenge.scraper.ing;

import challenge.model.Account;
import challenge.scraper.Scraper;
import challenge.scraper.ing.accounts.AccountsFetchingStrategy;
import challenge.scraper.ing.session.authenticated.AuthenticatedSessionFetchingStrategy;
import challenge.scraper.ing.session.unauthenticated.UnauthenticatedSessionFetchingStrategy;
import challenge.scraper.ing.session.authenticated.AuthenticatedSession;
import challenge.scraper.ing.session.unauthenticated.UnauthenticatedSession;

import java.util.List;

public class IngScraper implements Scraper {

  @Override
  public UnauthenticatedSession fetchUnauthenticatedSession(String login) {
    return new UnauthenticatedSessionFetchingStrategy(login).fetch();
  }

  @Override
  public AuthenticatedSession fetchAuthenticatedSession(String login, char[] password, UnauthenticatedSession unauthenticatedSession) {
    return new AuthenticatedSessionFetchingStrategy(login, password, unauthenticatedSession).fetch();
  }

  @Override
  public List<Account> fetchAccounts(AuthenticatedSession authenticatedSession) {
    return new AccountsFetchingStrategy(authenticatedSession).fetch();
  }
}
