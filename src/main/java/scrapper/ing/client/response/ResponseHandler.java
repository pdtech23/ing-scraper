package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scrapper.account.Account;
import scrapper.account.Money;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseHandler {

  private static final String DATA_FIELD_KEY = "data";

  public UnauthenticatedSession extractUnauthenticatedSession(Response response) {
    String failureMessage = "Could not create session";
    String salt = "salt";
    String mask = "mask";
    String key = "key";
    try {
      JSONObject jsonBody = response.jsonBody;
      if (!jsonBody.has(DATA_FIELD_KEY)) {
        throw new RuntimeException(failureMessage);
      }
      JSONObject data = response.jsonBody.getJSONObject(DATA_FIELD_KEY);
      if (data.has(salt) && data.has(mask) && data.has(key)) {
        return new UnauthenticatedSession(data.getString(salt), data.getString(mask), data.getString(key),
            extractSessionId(response));
      }
      throw new RuntimeException(failureMessage);
    } catch (JSONException e) {
      e.printStackTrace();
      throw new RuntimeException(failureMessage);
    }
  }

  public AuthenticatedSession extractAuthenticatedSession(Response authenticationResponse) {
    String token = extractSessionToken(authenticationResponse);
    String sessionId = extractSessionId(authenticationResponse);

    if (token.isEmpty() || sessionId.isEmpty()) {
      throw new RuntimeException("Missing data for creation of authenticated session.");
    }

    return new AuthenticatedSession(token, sessionId);
  }

  private String extractSessionId(Response response) {
    Header sessionHeader = Arrays.stream(response.headers).filter(header -> header.getName().equals("Set-Cookie"))
        .filter(cookieHeader -> cookieHeader.getValue().contains("JSESSIONID")).findFirst().orElseThrow(() -> new
            RuntimeException("Missing session data in response - incorrect password."));

    String header = sessionHeader.getValue();
    int i = header.indexOf('=') + 1;
    int j = header.indexOf(';');
    return header.substring(i, j);
  }

  private String extractSessionToken(Response response) {
    String token = "token";
    try {
      JSONObject jsonBody = response.jsonBody;
      if (!jsonBody.has(DATA_FIELD_KEY)) {
        return "";
      }
      JSONObject data = jsonBody.getJSONObject(DATA_FIELD_KEY);
      if (data.has(token)) {
        return data.getString(token);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      throw new RuntimeException("Incorrect response content.");
    }
    throw new RuntimeException("Missing session token in response.");
  }

  public List<Account> extractAccountsInfo(Response response) {
    String failureMessage = "Could not extract accounts information";
    String savingAccountsFieldKey = "sav";
    String currentAccountsFieldKey = "cur";
    try {
      JSONObject jsonBody = response.jsonBody;
      if (!jsonBody.has(DATA_FIELD_KEY)) {
        throw new RuntimeException(failureMessage);
      }

      JSONObject accounts = jsonBody.getJSONObject(DATA_FIELD_KEY);
      if (!accounts.has(savingAccountsFieldKey) || !accounts.has(currentAccountsFieldKey)) {
        throw new RuntimeException(failureMessage);
      }

      JSONArray savingAccounts = accounts.getJSONArray(savingAccountsFieldKey);
      JSONArray currentAccounts = accounts.getJSONArray(currentAccountsFieldKey);

      List<Account> result = new ArrayList<>();
      addAccounts(result, savingAccounts);
      addAccounts(result, currentAccounts);

      return result;
    } catch (JSONException e) {
      e.printStackTrace();
      throw new RuntimeException(failureMessage);
    }
  }

  private void addAccounts(List<Account> aggregator, JSONArray savingAccounts) throws JSONException {
    String accountKey = "acct";
    String availableBalanceKey = "avbal";
    String currencyKey = "curr";
    String nameKey = "name";
    for (int i = 0; i < savingAccounts.length(); i++) {
      JSONObject current = savingAccounts.getJSONObject(i);
      if (current.has(accountKey) && current.has(availableBalanceKey) && current.has(currencyKey) && current.has
          (nameKey)) {
        Account account = new Account(current.getString(accountKey), new Money(current.getString(availableBalanceKey)
            , current.getString(currencyKey)), current.getString(nameKey));
        aggregator.add(account);
      }
    }
  }
}
