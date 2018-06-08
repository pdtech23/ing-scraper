package scrapper.ing.client;

import org.junit.jupiter.api.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DataDownloaderServiceTest {

    private final DataDownloaderService testedService = new DataDownloaderService(new ResponseDataExtractor());

    @Test
    void shouldProceedOnAnyLoginExceptEmpty() {
        // given
        String login = "test";

        // when
        Optional<UnauthenticatedSession> response = this.testedService.createUnauthenticatedSession(login);

        // then
        assertTrue(response.isPresent());
    }

    @Test
    void shouldFailOnEmptyLogin() {
        // given
        String login = "";

        // when
        Optional<UnauthenticatedSession> response = this.testedService.createUnauthenticatedSession(login);

        // then
        assertFalse(response.isPresent());
    }

    @Test
    void shouldFailOnIncorrectLoginPasswordPair() {
        // given
        String login = "test";
        char[] password = "abcde".toCharArray();
        UnauthenticatedSession unauthenticatedSession = TestHelper.SAMPLE_PASSWORD_METADATA;

        // when
        Optional<AuthenticatedSession> response = this.testedService.createAuthenticatedSession(login, password,
                unauthenticatedSession);

        // then
        assertFalse(response.isPresent());
    }

    // due to lack of test credentials can't implement it in a meaningful way
    // shouldProceedOnProperLoginPasswordPair()


    @Test
    void shouldFailWithoutAuthentication() {
        // given
        AuthenticatedSession authenticatedSession = new AuthenticatedSession("123", "abc");

        // when
        List<IngAccountInfo> accountsInfo = this.testedService.getAccountsInfo(authenticatedSession);

        // then
        assertEquals(Collections.emptyList(), accountsInfo);
    }
}