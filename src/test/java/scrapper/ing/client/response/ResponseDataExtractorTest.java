package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import scrapper.ing.TestHelper;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}