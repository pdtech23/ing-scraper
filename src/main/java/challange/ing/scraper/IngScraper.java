package challange.ing.scraper;

import challange.account.Account;
import challange.ing.scraper.fetching.accounts.AccountsFetchingStrategy;
import challange.ing.scraper.fetching.session.authenticated.AuthenticatedSessionFetchingStrategy;
import challange.ing.scraper.fetching.session.unauthenticated.UnauthenticatedSessionFetchingStrategy;
import challange.ing.session.AuthenticatedSession;
import challange.ing.session.UnauthenticatedSession;

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
