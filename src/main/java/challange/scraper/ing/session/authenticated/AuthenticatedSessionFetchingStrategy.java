package challange.scraper.ing.session.authenticated;

import challange.scraper.ing.session.credentials.PasswordBehaviorHandler;
import challange.scraper.ing.SingleRequestFetchingStrategy;
import challange.scraper.ing.request.RequestHelper;
import challange.scraper.ing.response.Response;
import challange.scraper.ing.response.ResponseHelper;
import challange.scraper.ing.session.unauthenticated.UnauthenticatedSession;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

public class AuthenticatedSessionFetchingStrategy extends SingleRequestFetchingStrategy<AuthenticatedSession> {

  private String login;
  private char[] password;
  private UnauthenticatedSession unauthenticatedSession;

  public AuthenticatedSessionFetchingStrategy(String login, char[] password, UnauthenticatedSession unauthenticatedSession) {
    this.login = login;
    this.password = password;
    this.unauthenticatedSession = unauthenticatedSession;
  }

  @Override
  protected HttpUriRequest prepareRequest() {
    HttpPost request = new HttpPost(RequestHelper.LOGIN_URI);
    String json = "{" +
        RequestHelper.EMPTY_TOKEN + "," +
        RequestHelper.EMPTY_TRACE + "," +
        "\"data\":{" +
            "\"login\":\"" + login + "\"," +
            "\"pwdhash\":\"" + PasswordBehaviorHandler.createPasswordHash(unauthenticatedSession, password) + "\"," +
            "\"di\":\"T\"" +
            "}," +
        RequestHelper.LOCALE_PL +
        "}";
    RequestHelper.addJsonBodyWithHeader(request, json);
    RequestHelper.setSessionIdCookie(request, unauthenticatedSession.unauthenticatedSessionId);
    return request;
  }

  @Override
  protected AuthenticatedSession extractResultFrom(Response response) {
    String token = ResponseHelper.extractSessionToken(response);
    String sessionId = ResponseHelper.extractSessionId(response);
    return new AuthenticatedSession(token, sessionId);
  }
}
