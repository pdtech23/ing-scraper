package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.security.SessionData;

import java.util.List;

public class ResponseDataExtractorTest {

    private ResponseDataExtractor testedService = new ResponseDataExtractor();

    @Test
    public void shouldReturnEmptyIfIncorrectResponseData() throws JSONException {
        // given
        ResponseData response = new ResponseData(new JSONObject("{}"), new Header[]{});

        // when
        List<IngAccountInfo> accountInfos = this.testedService.extractAccountsInfo(response);
        SessionData sessionData = this.testedService.extractAuthenticatedSession(response);
        PasswordMetadata passwordMetadata = this.testedService.extractPasswordMetadata(response);

        // then
        Assert.assertTrue(accountInfos.isEmpty());
        Assert.assertSame(SessionData.EMPTY, sessionData);
        Assert.assertSame(PasswordMetadata.EMPTY, passwordMetadata);
    }
}