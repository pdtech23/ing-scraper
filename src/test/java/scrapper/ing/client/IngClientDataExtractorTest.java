package scrapper.ing.client;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IngClientDataExtractorTest {

    private final IngClientDataExtractorService testedService = new IngClientDataExtractorService();

    @Test
    public void shouldProceedOnAnyLogin() throws JSONException {
        // given
        String login = "test";

        // when
        JSONObject response = testedService.getResponseForLoginCheck(login).getJsonBody();

        // then
        assertEquals(response.get("status"), "OK");
    }

    @Test
    public void shouldFailOnIncorrectLoginPasswordPair() throws JSONException {
        // given
        String login = "test";
        String sessionId = "11111111";
        String passwordHash = "te1stt1gyxKitJlqguphPHqKFH3DEkJ7";

        // when
        JSONObject response = testedService.attemptLogIn(login, passwordHash, sessionId).getJsonBody();

        // then
        assertEquals("ERROR", response.get("status"));
    }

    @Test
    public void shouldFailWithoutAuthentication() {
        // given
        String sessionId = "123";
        String token = "abc";

        // when
        ResponseData accountsInfo = testedService.getAccountsInfo(sessionId, token);

        // then
        assertNull(accountsInfo);
    }
}