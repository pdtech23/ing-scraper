package challenge;

import challenge.model.Account;
import challenge.model.Money;
import challenge.scraper.ing.session.authenticated.AuthenticatedSession;
import challenge.scraper.ing.session.unauthenticated.UnauthenticatedSession;

public class TestHelper {

  public static final Account SAMPLE_ACCOUNT_INFO = new Account("1337 4567", new Money("12.34", "PLN"), "super acc");
  public static final AuthenticatedSession SAMPLE_AUTHENTICATED_SESSION = new AuthenticatedSession("token", "authenticatedSessionID");
  public static final UnauthenticatedSession SAMPLE_UNAUTHENTICATED_SESSION = new UnauthenticatedSession("ONE_SALTY_BOY", "*****--------",
                                                                                                         "ONE_KEY_GRILL", "unauthenticatedSessionId");
}
