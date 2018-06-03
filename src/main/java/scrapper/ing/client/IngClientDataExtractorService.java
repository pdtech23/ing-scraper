package scrapper.ing.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class IngClientDataExtractorService {

    public JSONObject getRequestForLoginCheckResult(String login) {
        HttpPost httpPost = new HttpPost("https://login.ingbank.pl/mojeing/rest/renchecklogin");
        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login + "\"},\"locale\":\"PL\"}";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            String resultJson = this.extractResponseJsonAsString(response.getEntity().getContent());

            return new JSONObject(resultJson);

        } catch (IOException | UnsupportedOperationException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private String extractResponseJsonAsString(InputStream response) {
        return new BufferedReader(new InputStreamReader(response)).lines().collect(Collectors.joining("\n"));
    }
}
