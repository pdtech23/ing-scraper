package scrapper.ing.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import scrapper.account.Account;
import scrapper.ing.client.response.Response;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.UnauthenticatedSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataDownloaderService {
    private static final String ING_REST_ENDPOINT_URI = "https://login.ingbank.pl/mojeing/rest";
    private static final String CHECK_LOGIN_URI = ING_REST_ENDPOINT_URI + "/renchecklogin";
    private static final String GET_ALL_ACCOUNTS_URI = ING_REST_ENDPOINT_URI + "/rengetallaccounts";
    private static final String LOGIN_URI = ING_REST_ENDPOINT_URI + "/renlogin";

    private ResponseDataExtractor responseDataExtractor;

    public DataDownloaderService(ResponseDataExtractor responseDataExtractor) {
        this.responseDataExtractor = responseDataExtractor;
    }

    public Optional<UnauthenticatedSession> createUnauthenticatedSession(String login) {

        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login + "\"},\"locale\":\"PL\"}";
        HttpPost httpPost = new HttpPost(CHECK_LOGIN_URI);

        Optional<Response> response = executeJsonRequest(httpPost, json);

        if (response.isPresent()) {
            return responseDataExtractor.extractUnauthenticatedSession(response.get());
        }
        return Optional.empty();
    }

    public Optional<AuthenticatedSession> createAuthenticatedSession(String login, char[] password,
                                                                     UnauthenticatedSession unauthenticatedSession) {

        String json = "{\"token\":\"\",\"trace\":\"\",\"data\":{\"login\":\"" + login + "\",\"pwdhash\":\"" +
                PasswordBehaviorHandler.createPasswordHash(unauthenticatedSession, password) + "\",\"di\":\"T\"}," +
                "\"locale\":\"PL\"}";

        HttpPost httpPost = new HttpPost(LOGIN_URI);
        httpPost.setHeader("Cookie", "JSESSIONID=" + unauthenticatedSession.unauthenticatedSessionId);

        Optional<Response> responseResult = executeJsonRequest(httpPost, json);

        if (responseResult.isPresent()) {
            return responseDataExtractor.extractAuthenticatedSession(responseResult.get());
        }
        return Optional.empty();
    }


    public List<Account> getAccountsInfo(AuthenticatedSession authenticatedSession) {
        HttpPost httpPost = new HttpPost(GET_ALL_ACCOUNTS_URI);

        setHeadersNecessaryToPretendBrowser(httpPost);

        httpPost.setHeader("Cookie", "JSESSIONID=" + authenticatedSession.authenticatedSessionId);
        String json = "{\"token\":\"" + authenticatedSession.token + "\",\"trace\":\"\",\"locale\":\"PL\"}";
        Optional<Response> response = executeJsonRequest(httpPost, json);

        if (response.isPresent()) {
            return responseDataExtractor.extractAccountsInfo(response.get());
        }

        return Collections.emptyList();

    }

    private void setHeadersNecessaryToPretendBrowser(HttpPost httpPost) {
        httpPost.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Ubuntu Chromium/66.0.3359.181 Chrome/66.0.3359.181 Safari/537.36");
        httpPost.setHeader("X-Wolf-Protection", "0.7616067842109708");
    }

    private Optional<Response> executeJsonRequest(HttpPost httpPost, String jsonBody) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody));
            CloseableHttpResponse response = client.execute(httpPost);

            return Optional.of(new Response(extractResponseJson(response), response.getAllHeaders()));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private JSONObject extractResponseJson(HttpResponse response) throws IOException, JSONException {
        String jsonAsString = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines()
                .collect(Collectors.joining("\n"));
        if (response.getStatusLine().getStatusCode() != 200) {
            return new JSONObject();
        }
        return new JSONObject(jsonAsString);
    }
}
