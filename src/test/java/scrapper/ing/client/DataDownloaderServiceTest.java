package scrapper.ing.client;

import org.junit.Ignore;
import org.junit.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordMetadata;

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
        PasswordMetadata response = this.testedService.doFirstLogInStep(login);

        // then
        assertNotSame(PasswordMetadata.EMPTY, response);
    }

    @Test
    public void shouldFailOnEmptyLogin() {
        // given
        String login = "";

        // when
        PasswordMetadata response = this.testedService.doFirstLogInStep(login);

        // then
        assertSame(PasswordMetadata.EMPTY, response);
    }

    @Test
    public void shouldFailOnIncorrectLoginPasswordPair() {
        // given
        String login = "test";
        char[] password = "abcde".toCharArray();
        PasswordMetadata passwordMetadata = TestHelper.SAMPLE_PASSWORD_METADATA;

        // when
        AuthenticatedSession response = this.testedService.createAuthenticatedSession(login, password,
                passwordMetadata);

        // then
        assertEquals(AuthenticatedSession.EMPTY, response);
    }

    // ignored due to lack of test credentials ;(
    @Ignore
    @Test
    public void shouldProceedOnProperLoginPasswordPair() {
        // given
        // proper login, password and metadata

        // when
        // call method

        // then
        // assert that result is success
    }

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