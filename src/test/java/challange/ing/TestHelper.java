package challange.ing;

import challange.account.Account;
import challange.account.Money;
import challange.ing.security.session.UnauthenticatedSession;
import challange.ing.security.session.UnauthenticatedSessionBuilder;

public class TestHelper {

  public static final Account SAMPLE_ACCOUNT_INFO = new Account("1337", new Money("12.34", "PLN"), "super acc");

  private static final String SAMPLE_UNAUTHENTICATED_SESSION_ID = "unauthenticatedSessionId";
  public static final UnauthenticatedSession SAMPLE_UNAUTHENTICATED_SESSION = new UnauthenticatedSessionBuilder()
      .withSalt("ONE_SALTY_BOY").withMask("*****--------").withKey("ONE_KEY_GRILL").withUnauthenticatedSessionId
          (SAMPLE_UNAUTHENTICATED_SESSION_ID).create();
}
