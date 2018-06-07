package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordMetadata;

import java.util.List;

public class ResponseExtractorTest {

    private ResponseDataExtractor testedService = new ResponseDataExtractor();

    @Test
    public void shouldReturnEmptyIfIncorrectResponseData() throws JSONException {
        // given
        Response response = new Response(new JSONObject("{}"), new Header[]{});

        // when
        List<IngAccountInfo> accountInfos = this.testedService.extractAccountsInfo(response);
        AuthenticatedSession authenticatedSession = this.testedService.extractAuthenticatedSession(response);
        PasswordMetadata passwordMetadata = this.testedService.extractPasswordMetadata(response);

        // then
        Assert.assertTrue(accountInfos.isEmpty());
        Assert.assertSame(AuthenticatedSession.EMPTY, authenticatedSession);
        Assert.assertSame(PasswordMetadata.EMPTY, passwordMetadata);
    }
}