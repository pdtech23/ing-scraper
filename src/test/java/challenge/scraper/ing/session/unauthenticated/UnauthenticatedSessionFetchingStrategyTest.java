package challenge.scraper.ing.session.unauthenticated;

import challenge.scraper.ing.response.Response;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnauthenticatedSessionFetchingStrategyTest {


  private final UnauthenticatedSessionFetchingStrategy testSubject = new UnauthenticatedSessionFetchingStrategy("Login");

  @Test
  void shouldReturnFailIfIncorrectResponseData() throws JSONException {
    // given
    Response response = new Response(new JSONObject("{}"), new Header[]{});
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testSubject.extractResultFrom(response));
  }

  @Test
  void shouldReturnUnauthenticatedSessionOnCorrectResponse() throws JSONException {
    // given
    String sessionId = "AuthenticatedSessionId";
    String salt = "SALT";
    String mask = "**++";
    String key = "1234";
    String jsonAsString = "{" +
        "\"data\":{" +
            "\"salt\":" + salt + "," +
            "\"mask\":" + mask + "," +
            "\"key\":" + key +
        "}}";
    BasicHeader[] headers = {new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId + ";")};
    Response authenticationResponse = new Response(new JSONObject(jsonAsString), headers);
    // when
    UnauthenticatedSession result = testSubject.extractResultFrom(authenticationResponse);
    // then
    assertEquals(sessionId, result.unauthenticatedSessionId);
    assertEquals(key, result.key);
    assertEquals(mask, result.mask);
    assertEquals(salt, result.salt);
  }
}