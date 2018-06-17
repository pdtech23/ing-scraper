package challenge.scraper;

import challenge.model.Account;
import challenge.scraper.ing.session.authenticated.AuthenticatedSession;
import challenge.scraper.ing.session.unauthenticated.UnauthenticatedSession;

import java.util.List;

public interface Scraper {

  UnauthenticatedSession fetchUnauthenticatedSession(String login);

  AuthenticatedSession fetchAuthenticatedSession(String login, char[] password, UnauthenticatedSession unauthenticatedSession);

  List<Account> fetchAccounts(AuthenticatedSession authenticatedSession);
}
