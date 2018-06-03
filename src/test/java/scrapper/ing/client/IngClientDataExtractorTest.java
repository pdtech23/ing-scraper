package scrapper.ing.client;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IngClientDataExtractorTest {

    // TODO: Probably switch testing framework so there's no need for
    @Test
    public void shouldProceedOnAnyLogin() throws JSONException {
        // given
        IngClientDataExtractorService client = new IngClientDataExtractorService();
        String login = "test";

        // when
        JSONObject response = client.getRequestForLoginCheckResult(login);

        //then
        assertEquals(response.get("status"), "OK");
    }
}