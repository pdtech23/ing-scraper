package scrapper.ing;

import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.account.Money;
import scrapper.ing.security.PasswordMetadata;

public class TestHelper {

    public static final IngAccountInfo SAMPLE_ACCOUNT_INFO = new IngAccountInfo("1337", new Money(12.34, "PLN"),
            "super acc");

    private static final String SAMPLE_UNAUTHENTICATED_SESSION_ID = "unauthenticatedSessionId";
    public static final PasswordMetadata SAMPLE_PASSWORD_METADATA = new PasswordMetadata("ONE_SALTY_BOY", "*****--------", "ONE_KEY_GRILL", SAMPLE_UNAUTHENTICATED_SESSION_ID);
}
