package scrapper.ing.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.client.response.ResponseHandler;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

class ConnectionTest {

    private final Connection testedService = new Connection(new ResponseHandler());

    @Test
    void shouldProceedOnAnyNonEmptyLogin() {
        // given
        String login = "test";

        // when
        testedService.createUnauthenticatedSession(login);

        // then
        // no exception is thrown
    }

    @Test
    void shouldFailOnEmptyLogin() {
        // given
        String login = "";

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.createUnauthenticatedSession(login));
    }

    @Test
    void shouldFailOnIncorrectLoginPasswordPair() {
        // given
        String login = "test";
        char[] password = "abcde".toCharArray();
        UnauthenticatedSession unauthenticatedSession = TestHelper.SAMPLE_UNAUTHENTICATED_SESSION;

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.createAuthenticatedSession(login,
                password, unauthenticatedSession));
    }

    // due to lack of test credentials can't implement it in a meaningful way
    // shouldProceedOnProperLoginPasswordPair()


    @Test
    void shouldFailWithoutAuthentication() {
        // given
        AuthenticatedSession authenticatedSession = new AuthenticatedSession("123", "abc");

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.getAccountsInfo(authenticatedSession));
    }
}
