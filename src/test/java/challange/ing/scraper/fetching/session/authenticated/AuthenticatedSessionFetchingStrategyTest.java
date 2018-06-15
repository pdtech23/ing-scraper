package challange.ing.scraper.fetching.session.authenticated;

import challange.ing.TestHelper;
import challange.ing.scraper.response.Response;
import challange.ing.session.AuthenticatedSession;
import challange.ing.session.UnauthenticatedSession;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticatedSessionFetchingStrategyTest {


  private final AuthenticatedSessionFetchingStrategy testSubject = new AuthenticatedSessionFetchingStrategy("login", new char[]{'1', '2', '3', '4', '5'},
                                                                                                            TestHelper.SAMPLE_UNAUTHENTICATED_SESSION);

  @Test
  void shouldReturnFailIfIncorrectResponseData() throws JSONException {
    // given
    Response response = new Response(new JSONObject("{}"), new Header[]{});
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testSubject.extractResultFrom(response));
  }

  @Test
  void fetch() {
  }

  @Test
  void shouldReturnAuthenticatedSessionOnCorrectResponse() throws JSONException {
    // given
    String sessionId = "authenticatedSessionId";
    String token = "token";
    JSONObject json = new JSONObject("{\"data\":{" +
                                             "\"token\":" + token +
                                         "}}");
    BasicHeader[] headers = {new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId + ";")};
    Response authenticationResponse = new Response(json, headers);
    // when
    AuthenticatedSession result = testSubject.extractResultFrom(authenticationResponse);
    // then
    assertEquals(sessionId, result.authenticatedSessionId);
    assertEquals(token, result.token);
  }
}