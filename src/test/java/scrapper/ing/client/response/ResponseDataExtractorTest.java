package scrapper.ing.client.response;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.List;

import static org.junit.Assert.*;

public class ResponseDataExtractorTest {

    private ResponseDataExtractor testedService = new ResponseDataExtractor();

    @Test
    public void shouldReturnEmptyIfIncorrectResponseData() throws JSONException {
        // given
        Response response = new Response(new JSONObject("{}"), new Header[]{});

        // when
        List<IngAccountInfo> accountInfos = this.testedService.extractAccountsInfo(response);
        AuthenticatedSession authenticatedSession = this.testedService.extractAuthenticatedSession(response);
        UnauthenticatedSession unauthenticatedSession = this.testedService.extractUnauthenticatedSession(response);

        // then
        assertTrue(accountInfos.isEmpty());
        assertTrue(authenticatedSession.isEmpty());
        assertTrue(unauthenticatedSession.isEmpty());
    }

    // Tests below would be way better if they could work on actual responses not mocks
    @Test
    public void shouldReturnEmptyInfoWhenUserHasNoAccounts() throws JSONException {
        // given
        String jsonAsString = "{\"data\":{\"sav\":[],\"cur\":[]}}";
        Response response = new Response(new JSONObject(jsonAsString), new Header[]{});

        // when
        List<IngAccountInfo> result = this.testedService.extractAccountsInfo(response);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnCorrespondingAccountsInfo() throws JSONException {
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
    public void shouldReturnUnauthenticatedSessionOnCorrectResponse() throws JSONException {
        // given
        String sessionId = "sessionId";
        String key = "1234";
        String mask = "**++";
        String salt = "SALT";
        Response authenticationResponse = new Response(new JSONObject("{\"data\":{\"salt\":" + salt + "," +
                "\"mask\":" + mask + ",\"key\":" + key + "}}"), new BasicHeader[]{new BasicHeader("Set-Cookie",
                "JSESSIONID=" + sessionId + ";")});

        // when
        UnauthenticatedSession result = this.testedService.extractUnauthenticatedSession(authenticationResponse);

        // then
        assertEquals(sessionId, result.getUnauthenticatedSessionId());
        assertEquals(key, result.getKey());
        assertEquals(mask, result.getMask());
        assertEquals(salt, result.getSalt());
    }

    @Test
    public void shouldReturnAuthenticatedSessionOnCorrectResponse() throws JSONException {
        // given
        String sessionId = "sessionId";
        String token = "token";
        Response authenticationResponse = new Response(new JSONObject("{\"data\":{\"token\":" + token + "}}"), new
                BasicHeader[]{new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId + ";")});

        // when
        AuthenticatedSession result = this.testedService.extractAuthenticatedSession(authenticationResponse);

        // then
        assertEquals(sessionId, result.getAuthenticatedSessionId());
        assertEquals(token, result.getToken());
    }
}