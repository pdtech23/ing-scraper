package scrapper.ing.client.response;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scrapper.account.Account;
import scrapper.ing.TestHelper;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {

  private ResponseHandler testedService = new ResponseHandler();

  @Test
  void shouldReturnFailIfIncorrectResponseData() throws JSONException {
    // given
    Response response = new Response(new JSONObject("{}"), new Header[]{});
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.extractAccountsInfo(response));
    Assertions.assertThrows(RuntimeException.class, () -> testedService.extractAuthenticatedSession(response));
    Assertions.assertThrows(RuntimeException.class, () -> testedService.extractUnauthenticatedSession(response));
  }

  // Tests below would be way better if they could work on actual responses not mocks
  @Test
  void shouldReturnEmptyInfoWhenUserHasNoAccounts() throws JSONException {
    // given
    String jsonAsString = "{\"data\":{\"sav\":[],\"cur\":[]}}";
    Response response = new Response(new JSONObject(jsonAsString), new Header[]{});
    // when
    List<Account> result = testedService.extractAccountsInfo(response);
    // then
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnCorrespondingAccountsInfo() throws JSONException {
    // given
    String jsonAsString = "{\"data\":{\"sav\":[{\"acct\":\"1337\",\"avbal\":\"12.34\",\"name\":\"super acc\"," +
        "\"curr\":\"PLN\"}],\"cur\":[]}}";
    Response response = new Response(new JSONObject(jsonAsString), new Header[]{});
    Account expected = TestHelper.SAMPLE_ACCOUNT_INFO;
    // when
    List<Account> result = testedService.extractAccountsInfo(response);
    // then
    assertFalse(result.contains(expected));
  }

  @Test
  void shouldReturnUnauthenticatedSessionOnCorrectResponse() throws JSONException {
    // given
    String sessionId = "sessionId";
    String key = "1234";
    String mask = "**++";
    String salt = "SALT";
    Response authenticationResponse = new Response(new JSONObject("{\"data\":{\"salt\":" + salt + "," + "\"mask\":" +
        mask + ",\"key\":" + key + "}}"), new BasicHeader[]{new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId +
        ";")});
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
    String sessionId = "sessionId";
    String token = "token";
    Response authenticationResponse = new Response(new JSONObject("{\"data\":{\"token\":" + token + "}}"), new
        BasicHeader[]{new BasicHeader("Set-Cookie", "JSESSIONID=" + sessionId + ";")});
    // when
    AuthenticatedSession result = testedService.extractAuthenticatedSession(authenticationResponse);
    // then
    assertEquals(sessionId, result.authenticatedSessionId);
    assertEquals(token, result.token);
  }
}
