package scrapper.ing;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit test for simple AppEntryPoint.
 */
public class LearningTests {

    @Test
    public void shouldFindTarget() throws IOException {
        // given
        URL url = new URL("https://login.ingbank.pl/mojeing/");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // when
        int responseCode = con.getResponseCode();

        // then
        assertEquals(200, responseCode);
    }

    @Test
    public void shouldProceedOnAnyLogin() throws JSONException {
        JSONObject jsonResult = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://login.ingbank.pl/mojeing/rest/renchecklogin");

            String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"test\"},\"locale\":\"PL\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            String resultJson = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
            assertEquals(200, response.getStatusLine().getStatusCode());
            jsonResult = new JSONObject(resultJson);
        } catch (IOException | UnsupportedOperationException | JSONException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals("OK", jsonResult.get("status"));
    }

    @Ignore
    @Test
    public void shouldLoginWithProperCredentials() throws JSONException {
        JSONObject jsonResult = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://login.ingbank.pl/mojeing/rest/renchecklogin");

            String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"pwdhash\":\"" + HmacUtils.hmacSha1Hex("key", "message") + "\"\"login\":\"test\"},\"locale\":\"PL\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            String resultJson = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
            assertEquals(200, response.getStatusLine().getStatusCode());
            jsonResult = new JSONObject(resultJson);
        } catch (IOException | UnsupportedOperationException | JSONException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals("OK", jsonResult.get("status"));
    }
}
