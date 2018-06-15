package challange.ing.scraper;

import challange.account.Account;
import challange.ing.session.AuthenticatedSession;
import challange.ing.session.UnauthenticatedSession;

import java.util.List;

public interface Scraper {

  UnauthenticatedSession fetchUnauthenticatedSession(String login);

  AuthenticatedSession fetchAuthenticatedSession(String login, char[] password, UnauthenticatedSession
      unauthenticatedSession);

  List<Account> fetchAccounts(AuthenticatedSession authenticatedSession);
}
