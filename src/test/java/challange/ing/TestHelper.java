package challange.ing;

import challange.account.Account;
import challange.account.Money;
import challange.ing.session.AuthenticatedSession;
import challange.ing.session.UnauthenticatedSession;
import challange.ing.session.UnauthenticatedSessionBuilder;

public class TestHelper {

  public static final Account SAMPLE_ACCOUNT_INFO = new Account("1337 4567", new Money("12.34", "PLN"), "super acc");
  public static final AuthenticatedSession SAMPLE_AUTHENTICATED_SESSION = new AuthenticatedSession("token", "authenticatedSessionID");
  public static final UnauthenticatedSession SAMPLE_UNAUTHENTICATED_SESSION = new UnauthenticatedSessionBuilder()
      .withSalt("ONE_SALTY_BOY").withMask("*****--------").withKey("ONE_KEY_GRILL").withUnauthenticatedSessionId
          ("unauthenticatedSessionId").create();
}
