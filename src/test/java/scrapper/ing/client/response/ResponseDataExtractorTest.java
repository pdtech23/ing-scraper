package scrapper.ing.client.response;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDataExtractorTest {

    private ResponseDataExtractor testedService = new ResponseDataExtractor();

    @Test
    void shouldReturnEmptyIfIncorrectResponseData() throws JSONException {
        // given
        Response response = new Response(new JSONObject("{}"), new Header[]{});

        // when
        List<IngAccountInfo> accountInfos = this.testedService.extractAccountsInfo(response);
        Optional<AuthenticatedSession> authenticatedSession = this.testedService.extractAuthenticatedSession(response);
        Optional<UnauthenticatedSession> unauthenticatedSession = this.testedService.extractUnauthenticatedSession
                (response);

        // then
        assertTrue(accountInfos.isEmpty());
        assertFalse(authenticatedSession.isPresent());
        assertFalse(unauthenticatedSession.isPresent());
    }

    // Tests below would be way better if they could work on actual responses not mocks
    @Test
    void shouldReturnEmptyInfoWhenUserHasNoAccounts() throws JSONException {
        // given
        String jsonAsString = "{\"data\":{\"sav\":[],\"cur\":[]}}";
        Response response = new Response(new JSONObject(jsonAsString), new Header[]{});

        // when
        List<IngAccountInfo> result = this.testedService.extractAccountsInfo(response);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnCorrespondingAccountsInfo() throws JSONException {
        // given
        String jsonAsString = "{\"data\":{\"sav\":[{\"acct\":\"1337\",\"avbal\":\"12.34\",\"name\":\"super acc\"," +
                "\"curr\":\"PLN\"}],\"cur\":[]}}";
        Response response = new Response(new JSONObject(jsonAsString), new Header[]{});
        IngAccountInfo expected = TestHelper.SAMPLE_ACCOUNT_INFO;

        // when
        List<IngAccountInfo> result = this.testedService.extractAccountsInfo(response);

        // then
        assertFalse(result.contains(expected));
    }

    @Test
    void shouldReturnUnauthenticatedSessionOnCorrectResponse() throws JSONException {
        // given
        String sessionId = "sessionId";
        String key = "1234";
        String mask = "**++";
        String salt = "SALT";
        Response authenticationResponse = new Response(new JSONObject("{\"data\":{\"salt\":" + salt + "," +
                "\"mask\":" + mask + ",\"key\":" + key + "}}"), new BasicHeader[]{new BasicHeader("Set-Cookie",
                "JSESSIONID=" + sessionId + ";")});

        // when
        Optional<UnauthenticatedSession> result = this.testedService.extractUnauthenticatedSession
                (authenticationResponse);

        // then
        assertTrue(result.isPresent());
        UnauthenticatedSession resultValue = result.get();
        assertEquals(sessionId, resultValue.unauthenticatedSessionId);
        assertEquals(key, resultValue.key);
        assertEquals(mask, resultValue.mask);
        assertEquals(salt, resultValue.salt);
    }

    @Test
    void shouldReturnAuthenticatedSessionOnCorrectResponse() throws JSONException {
        // given
        String sessionId = "sessionId";
        String token = "token";
        Response authenticationResponse = new Response(new JSONObject("{\"data\":{\"token\":" + token + "}}"), new
                BasicHeader[]{new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId + ";")});

        // when
        Optional<AuthenticatedSession> result = this.testedService.extractAuthenticatedSession(authenticationResponse);

        // then
        assertTrue(result.isPresent());
        AuthenticatedSession resultValue = result.get();
        assertEquals(sessionId, resultValue.authenticatedSessionId);
        assertEquals(token, resultValue.token);
    }
}