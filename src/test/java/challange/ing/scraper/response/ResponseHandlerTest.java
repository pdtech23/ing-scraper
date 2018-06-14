package challange.ing.scraper.response;

import challange.account.Account;
import challange.ing.TestHelper;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {

  private ResponseHandler testedService = new ResponseHandler();

  @Test
  void shouldReturnFailIfIncorrectResponseData() throws JSONException {
    // given
    Response response = new Response(new JSONObject("{}"), new Header[]{});
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.extractAccounts(response));
    Assertions.assertThrows(RuntimeException.class, () -> testedService.extractAuthenticatedSession(response));
    Assertions.assertThrows(RuntimeException.class, () -> testedService.extractUnauthenticatedSession(response));
  }

  // Tests below would be way better if they could work on actual responses not mocks
  @Test
  void shouldReturnEmptyInfoWhenUserHasNoAccounts() throws JSONException {
    // given
    String jsonAsString = "{" +
        "\"data\":{" +
            "\"sav\":[]," +
            "\"cur\":[]" +
        "}}";
    Response response = new Response(new JSONObject(jsonAsString), new Header[]{});
    // when
    List<Account> result = testedService.extractAccounts(response);
    // then
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnCorrespondingAccountsInfo() throws JSONException {
    // given
    String jsonAsString = "{" +
        "\"data\": {" +
            "\"sav\":[{\"acct\":\"1337\",\"avbal\":\"12.34\",\"name\":\"super acc\",\"curr\":\"PLN\"}]," +
            "\"cur\":[]" +
        "}}";
    Response response = new Response(new JSONObject(jsonAsString), new Header[]{});
    Account expected = TestHelper.SAMPLE_ACCOUNT_INFO;
    // when
    List<Account> result = testedService.extractAccounts(response);
    // then
    assertFalse(result.contains(expected));
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
    UnauthenticatedSession result = testedService.extractUnauthenticatedSession(authenticationResponse);
    // then
    assertEquals(sessionId, result.unauthenticatedSessionId);
    assertEquals(key, result.key);
    assertEquals(mask, result.mask);
    assertEquals(salt, result.salt);
  }

  @Test
  void shouldReturnAuthenticatedSessionOnCorrectResponse() throws JSONException {
    // given
    String sessionId = "authenticatedSessionId";
    String token = "token";
    String jsonAsString = "{" +
        "\"data\":{" +
            "\"token\":" + token +
        "}}";
    BasicHeader[] headers = {new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId + ";")};
    Response authenticationResponse = new Response(new JSONObject(jsonAsString), headers);
    // when
    AuthenticatedSession result = testedService.extractAuthenticatedSession(authenticationResponse);
    // then
    assertEquals(sessionId, result.authenticatedSessionId);
    assertEquals(token, result.token);
  }
}
