package challange.ing.scraper.request;

import challange.ing.security.PasswordBehaviorHandler;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class RequestCreator {

  private static final String ING_REST_ENDPOINT_URI = "https://login.ingbank.pl/mojeing/rest";
  private static final String GET_ALL_ACCOUNTS_URI = ING_REST_ENDPOINT_URI + "/rengetallaccounts";
  private static final String LOGIN_URI = ING_REST_ENDPOINT_URI + "/renlogin";
  private static final String CHECK_LOGIN_URI = ING_REST_ENDPOINT_URI + "/renchecklogin";
  private static final String EMPTY_TRACE = "\"trace\":\"\"";
  private static final String EMPTY_TOKEN = "\"token\":\"\"";
  private static final String LOCALE_PL = "\"locale\":\"PL\"";


  public HttpPost createRequestForLoginCheck(String login) {
    HttpPost request = new HttpPost(CHECK_LOGIN_URI);
    String json = "{" +
        EMPTY_TOKEN + "," +
        EMPTY_TRACE + "," +
        "\"data\":{" +
            "\"login\":\"" + login + "\"" +
            "}," +
        LOCALE_PL +
        "}";
    addJsonBody(request, json);
    return request;
  }

  public HttpPost createRequestForLogin(String login, char[] password, UnauthenticatedSession unauthenticatedSession) {
    HttpPost request = new HttpPost(LOGIN_URI);
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
    addJsonBody(request, json);
    setSessionId(request, unauthenticatedSession.unauthenticatedSessionId);
    return request;
  }

  public HttpPost createRequestForFetchingAccounts(AuthenticatedSession authenticatedSession) {
    HttpPost request = new HttpPost(GET_ALL_ACCOUNTS_URI);
    String json = "{" +
        EMPTY_TRACE + "," +
        "\"token\":\"" + authenticatedSession.token + "\"," +
        LOCALE_PL +
        "}";
    addJsonBody(request, json);
    setSessionId(request, authenticatedSession.authenticatedSessionId);
    setHeadersNecessaryToPretendBrowser(request);
    return request;
  }

  private void addJsonBody(HttpPost request, String json) {
    request.setHeader("Content-type", "application/json");
    try {
      request.setEntity(new StringEntity(json));
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private void setSessionId(HttpPost httpPost, String unauthenticatedSessionId) {
    httpPost.setHeader("Cookie", "JSESSIONID=" + unauthenticatedSessionId);
  }

  private void setHeadersNecessaryToPretendBrowser(HttpPost httpPost) {
    httpPost.setHeader("User-Agent", "");
    httpPost.setHeader("X-Wolf-Protection", String.valueOf(Math.random()));
  }
}
