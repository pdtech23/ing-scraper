package scrapper.ing.client;

import org.junit.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class DataDownloaderServiceTest {

    private final DataDownloaderService testedService = new DataDownloaderService(new ResponseDataExtractor());

    @Test
    public void shouldProceedOnAnyLoginExceptEmpty() {
        // given
        String login = "test";

        // when
        UnauthenticatedSession response = this.testedService.createUnauthenticatedSession(login);

        // then
        assertNotSame(UnauthenticatedSession.EMPTY, response);
    }

    @Test
    public void shouldFailOnEmptyLogin() {
        // given
        String login = "";

        // when
        UnauthenticatedSession response = this.testedService.createUnauthenticatedSession(login);

        // then
        assertSame(UnauthenticatedSession.EMPTY, response);
    }

    @Test
    public void shouldFailOnIncorrectLoginPasswordPair() {
        // given
        String login = "test";
        char[] password = "abcde".toCharArray();
        UnauthenticatedSession unauthenticatedSession = TestHelper.SAMPLE_PASSWORD_METADATA;

        // when
        AuthenticatedSession response = this.testedService.createAuthenticatedSession(login, password, unauthenticatedSession);

        // then
        assertEquals(AuthenticatedSession.EMPTY, response);
    }

    // due to lack of test credentials can't implement it in a meaningful way
    // shouldProceedOnProperLoginPasswordPair()


    @Test
    public void shouldFailWithoutAuthentication() {
        // given
        AuthenticatedSession authenticatedSession = new AuthenticatedSession("123", "abc");

        // when
        List<IngAccountInfo> accountsInfo = this.testedService.getAccountsInfo(authenticatedSession);

        // then
        assertEquals(Collections.emptyList(), accountsInfo);
    }
}