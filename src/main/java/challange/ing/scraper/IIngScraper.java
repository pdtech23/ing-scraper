package challange.ing.scraper;

import challange.account.Account;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;

import java.util.List;

public interface IIngScraper {
  UnauthenticatedSession fetchUnauthenticatedSession(String login);

  AuthenticatedSession fetchAuthenticatedSession(String login, char[] password, UnauthenticatedSession
      unauthenticatedSession);

  List<Account> fetchAccounts(AuthenticatedSession authenticatedSession);
}
