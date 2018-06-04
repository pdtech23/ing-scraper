package scrapper.ing.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class IngClientDataExtractorService {

    public ResponseData getResponseForLoginCheck(String login) {
        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login + "\"},\"locale\":\"PL\"}";
        HttpPost httpPost = new HttpPost("https://login.ingbank.pl/mojeing/rest/renchecklogin");
        return this.getResultOfPostRequest(httpPost, json);
    }

    public ResponseData attemptLogIn(String login, String passwordHash, String sessionId) {
        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login +
                "\",\"pwdhash\":\"" + passwordHash + "\",\"di\":\"T\"},\"locale\":\"PL\"}";
        HttpPost httpPost = new HttpPost("https://login.ingbank.pl/mojeing/rest/renlogin");
        httpPost.setHeader("Cookie", "JSESSIONID=" + sessionId);
        return this.getResultOfPostRequest(httpPost, json);

    }

    private ResponseData getResultOfPostRequest(HttpPost httpPost, String jsonBody) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody));
            CloseableHttpResponse response = client.execute(httpPost);
            return new ResponseData(this.extractResponseJson(response), response.getAllHeaders());

        } catch (IOException | UnsupportedOperationException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject extractResponseJson(HttpResponse response) throws IOException, JSONException {
        String jsonAsString = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
        return new JSONObject(jsonAsString);
    }

    public ResponseData getAccountsInfo(String sessionId, String token) {
        String json = "{\"token\":\"" + token + "\",\"trace\":\"\",\"locale\":\"PL\"}";
        HttpPost httpPost = new HttpPost("https://login.ingbank.pl/mojeing/rest/rengetallaccounts");
        httpPost.setHeader("Cookie", "JSESSIONID=" + sessionId);
        httpPost.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/66.0.3359.181 Chrome/66.0.3359.181 Safari/537.36");
        httpPost.setHeader("X-Wolf-Protection", "0.7616067842109708");
        return this.getResultOfPostRequest(httpPost, json);

    }
}
