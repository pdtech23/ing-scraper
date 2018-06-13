package scrapper.ing;

import scrapper.account.Account;
import scrapper.account.Money;
import scrapper.ing.security.UnauthenticatedSession;

public class TestHelper {

    public static final Account SAMPLE_ACCOUNT_INFO = new Account("1337", new Money("12.34", "PLN"), "super acc");

    private static final String SAMPLE_UNAUTHENTICATED_SESSION_ID = "unauthenticatedSessionId";
    public static final UnauthenticatedSession SAMPLE_UNAUTHENTICATED_SESSION = new UnauthenticatedSession
            ("ONE_SALTY_BOY", "*****--------", "ONE_KEY_GRILL", SAMPLE_UNAUTHENTICATED_SESSION_ID);
}
