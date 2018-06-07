package scrapper.ing.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.response.ResponseData;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.security.SessionData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataDownloaderService {
    private static final String ING_REST_ENDPOINT_URI = "https://login.ingbank.pl/mojeing/rest";
    private static final String GET_ALL_ACCOUNTS_URI = ING_REST_ENDPOINT_URI + "/rengetallaccounts";
    private static final String LOGIN_URI = ING_REST_ENDPOINT_URI + "/renlogin";

    private ResponseDataExtractor responseDataExtractor;

    public DataDownloaderService(ResponseDataExtractor responseDataExtractor) {
        this.responseDataExtractor = responseDataExtractor;
    }

    public PasswordMetadata doFirstLogInStep(String login) {

        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login + "\"},\"locale\":\"PL\"}";
        HttpPost httpPost = new HttpPost(ING_REST_ENDPOINT_URI + "/renchecklogin");

        ResponseData responseData = this.executeJsonRequest(httpPost, json);

        if (responseData == ResponseData.EMPTY_RESPONSE) {
            return PasswordMetadata.EMPTY;
        }

        return this.responseDataExtractor.extractPasswordMetadata(responseData);
    }

    public SessionData createAuthenticatedSession(String login, char[] password, PasswordMetadata passwordMetadata) {

        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login + "\",\"pwdhash\":\"" +
                PasswordBehaviorHandler.createPasswordHash(passwordMetadata, password) + "\",\"di\":\"T\"}," +
                "\"locale\":\"PL\"}";

        HttpPost httpPost = new HttpPost(LOGIN_URI);
        httpPost.setHeader("Cookie", "JSESSIONID=" + passwordMetadata.getUnauthenticatedSessionId());

        ResponseData responseResult = this.executeJsonRequest(httpPost, json);

        if (responseResult == ResponseData.EMPTY_RESPONSE) {
            return SessionData.EMPTY;
        }

        return this.responseDataExtractor.extractAuthenticatedSession(responseResult);
    }


    public List<IngAccountInfo> getAccountsInfo(SessionData sessionData) {
        HttpPost httpPost = new HttpPost(GET_ALL_ACCOUNTS_URI);

        httpPost.setHeader("Cookie", "JSESSIONID=" + sessionData.getSessionId());
        httpPost.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Ubuntu Chromium/66.0.3359.181 Chrome/66.0.3359.181 Safari/537.36");
        httpPost.setHeader("X-Wolf-Protection", "0.7616067842109708");

        String json = "{\"token\":\"" + sessionData.getToken() + "\",\"trace\":\"\",\"locale\":\"PL\"}";
        ResponseData response = this.executeJsonRequest(httpPost, json);

        if (response == ResponseData.EMPTY_RESPONSE) {
            return Collections.emptyList();
        }

        return this.responseDataExtractor.extractAccountsInfo(response);

    }

    private ResponseData executeJsonRequest(HttpPost httpPost, String jsonBody) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody));
            CloseableHttpResponse response = client.execute(httpPost);

            return new ResponseData(this.extractResponseJson(response), response.getAllHeaders());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return ResponseData.EMPTY_RESPONSE;
        }
    }

    private JSONObject extractResponseJson(HttpResponse response) throws IOException, JSONException {
        String jsonAsString = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines()
                .collect(Collectors.joining("\n"));
        return new JSONObject(jsonAsString);
    }
}
