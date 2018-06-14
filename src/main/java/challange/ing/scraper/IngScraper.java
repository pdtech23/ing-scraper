package challange.ing.scraper;

import challange.account.Account;
import challange.ing.scraper.response.Response;
import challange.ing.scraper.response.ResponseHandler;
import challange.ing.scraper.response.exception.InvalidResponseException;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.PasswordBehaviorHandler;
import challange.ing.security.session.UnauthenticatedSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class IngScraper implements IIngScraper {
  private static final String ING_REST_ENDPOINT_URI = "https://login.ingbank.pl/mojeing/rest";
  private static final String CHECK_LOGIN_URI = ING_REST_ENDPOINT_URI + "/renchecklogin";
  private static final String GET_ALL_ACCOUNTS_URI = ING_REST_ENDPOINT_URI + "/rengetallaccounts";
  private static final String LOGIN_URI = ING_REST_ENDPOINT_URI + "/renlogin";
  private static final String EMPTY_TRACE = "\"trace\":\"\"";
  private static final String EMPTY_TOKEN = "\"token\":\"\"";
  private static final String LOCALE_PL = "\"locale\":\"PL\"";

  private ResponseHandler responseHandler;

  public IngScraper() {
    responseHandler = new ResponseHandler();
  }

  @Override
  public UnauthenticatedSession createUnauthenticatedSession(String login) {
    String json = "{" +
        EMPTY_TOKEN + "," +
        EMPTY_TRACE + "," +
        "\"data\":{" +
            "\"login\":\"" + login + "\"" +
        "}," +
        LOCALE_PL +
        "}";
    HttpPost httpPost = new HttpPost(CHECK_LOGIN_URI);
    Response response = executeJsonRequest(httpPost, json);
    return responseHandler.extractUnauthenticatedSession(response);
  }

  @Override
  public AuthenticatedSession createAuthenticatedSession(String login, char[] password, UnauthenticatedSession
      unauthenticatedSession) {
    String json = "{" +
        EMPTY_TOKEN + "," +
        EMPTY_TRACE + "," +
        "\"data\":{" +
            "\"login\":\"" + login + "\"," +
            "\"pwdhash\":\"" + PasswordBehaviorHandler.createPasswordHash(unauthenticatedSession, password) + "\"," +
            "\"di\":\"T\"" +
        "}," +
        LOCALE_PL +
        "}";
    HttpPost httpPost = new HttpPost(LOGIN_URI);
    setSessionId(httpPost, unauthenticatedSession.unauthenticatedSessionId);
    Response response = executeJsonRequest(httpPost, json);
    return responseHandler.extractAuthenticatedSession(response);
  }


  @Override
  public List<Account> fetchAccounts(AuthenticatedSession authenticatedSession) {
    String json = "{" +
        "\"token\":\"" + authenticatedSession.token + "\"," +
        EMPTY_TRACE + "," +
        LOCALE_PL +
        "}";
    HttpPost httpPost = new HttpPost(GET_ALL_ACCOUNTS_URI);
    setHeadersNecessaryToPretendBrowser(httpPost);
    setSessionId(httpPost, authenticatedSession.authenticatedSessionId);
    Response response = executeJsonRequest(httpPost, json);
    return responseHandler.extractAccounts(response);
  }

  private void setSessionId(HttpPost httpPost, String unauthenticatedSessionId) {
    httpPost.setHeader("Cookie", "JSESSIONID=" + unauthenticatedSessionId);
  }

  private void setHeadersNecessaryToPretendBrowser(HttpPost httpPost) {
    httpPost.setHeader("User-Agent", "");
    httpPost.setHeader("X-Wolf-Protection", String.valueOf(Math.random()));
  }

  private Response executeJsonRequest(HttpPost httpPost, String jsonBody) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      httpPost.setHeader("Content-type", "application/json");
      httpPost.setEntity(new StringEntity(jsonBody));
      CloseableHttpResponse response = client.execute(httpPost);
      return new Response(extractResponseJson(response), response.getAllHeaders());
    } catch (JSONException e) {
      throw new InvalidResponseException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private JSONObject extractResponseJson(HttpResponse response) throws IOException, JSONException {
    String jsonAsString = EntityUtils.toString(response.getEntity());
    return new JSONObject(jsonAsString);
  }
}
