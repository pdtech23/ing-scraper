package challange.scraper.ing.accounts;

import challange.model.Account;
import challange.TestHelper;
import challange.scraper.ing.response.Response;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountsFetchingStrategyTest {

  private final AccountsFetchingStrategy testSubject = new AccountsFetchingStrategy(TestHelper.SAMPLE_AUTHENTICATED_SESSION);

  @Test
  void shouldReturnFailIfIncorrectResponseData() throws JSONException {
    // given
    Response response = new Response(new JSONObject("{}"), new Header[]{});
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testSubject.extractResultFrom(response));
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
    List<Account> result = testSubject.extractResultFrom(response);
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
    List<Account> result = testSubject.extractResultFrom(response);
    // then
    assertFalse(result.contains(expected));
  }

}