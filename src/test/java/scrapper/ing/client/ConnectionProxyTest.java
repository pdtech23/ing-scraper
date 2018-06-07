package scrapper.ing.client;

import org.junit.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.security.SessionData;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ConnectionProxyTest {

    private final DataDownloaderService testedService = new DataDownloaderService(new ResponseDataExtractor());

    @Test
    public void shouldProceedOnAnyLogin() {
        // given
        String login = "test";

        // when
        PasswordMetadata response = this.testedService.doFirstLogInStep(login);

        // then
        assertNotSame(PasswordMetadata.EMPTY, response);
    }

    @Test
    public void shouldFailOnIncorrectLoginPasswordPair() {
        // given
        String login = "test";
        char[] password = "abcde".toCharArray();
        PasswordMetadata passwordMetadata = TestHelper.SAMPLE_PASSWORD_METADATA;

        // when
        SessionData response = this.testedService.createAuthenticatedSession(login, password, passwordMetadata);

        // then
        assertEquals(SessionData.EMPTY, response);
    }

    @Test
    public void shouldFailWithoutAuthentication() {
        // given
        SessionData sessionData = new SessionData("123", "abc");

        // when
        List<IngAccountInfo> accountsInfo = this.testedService.getAccountsInfo(sessionData);

        // then
        assertEquals(Collections.emptyList(), accountsInfo);
    }
}