package challange.scraper.ing.request;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class RequestHelper {

  private static final String ING_REST_ENDPOINT_URI = "https://login.ingbank.pl/mojeing/rest";
  public static final String GET_ALL_ACCOUNTS_URI = ING_REST_ENDPOINT_URI + "/rengetallaccounts";
  public static final String LOGIN_URI = ING_REST_ENDPOINT_URI + "/renlogin";
  public static final String CHECK_LOGIN_URI = ING_REST_ENDPOINT_URI + "/renchecklogin";
  public static final String EMPTY_TRACE = "\"trace\":\"\"";
  public static final String EMPTY_TOKEN = "\"token\":\"\"";
  public static final String LOCALE_PL = "\"locale\":\"PL\"";

  private RequestHelper() {
  }

  public static void addJsonBodyWithHeader(HttpPost request, String json) {
    request.setHeader("Content-type", "application/json");
    try {
      request.setEntity(new StringEntity(json));
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static void setSessionIdCookie(HttpPost httpPost, String unauthenticatedSessionId) {
    httpPost.setHeader("Cookie", "JSESSIONID=" + unauthenticatedSessionId);
  }

  public static void setHeadersNecessaryToPretendBrowser(HttpPost httpPost) {
    httpPost.setHeader("User-Agent", "");
    httpPost.setHeader("X-Wolf-Protection", String.valueOf(Math.random()));
  }
}
