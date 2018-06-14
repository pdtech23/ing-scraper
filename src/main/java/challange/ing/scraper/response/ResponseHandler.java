package challange.ing.scraper.response;

import challange.account.Account;
import challange.account.Money;
import challange.ing.scraper.response.exception.InvalidResponseException;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;
import challange.ing.security.session.UnauthenticatedSessionBuilder;
import challange.ing.security.session.exception.InvalidCredentialsException;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseHandler {

  private static final String DATA_FIELD_KEY = "data";

  public UnauthenticatedSession extractUnauthenticatedSession(Response response) {
    try {
      JSONObject data = response.jsonBody.getJSONObject(DATA_FIELD_KEY);
      return new UnauthenticatedSessionBuilder()
          .withSalt(data.getString("salt"))
          .withMask(data.getString("mask"))
          .withKey(data.getString("key"))
          .withUnauthenticatedSessionId(extractSessionId(response))
          .create();
    } catch (JSONException e) {
      throw new InvalidResponseException(e);
    }
  }

  public AuthenticatedSession extractAuthenticatedSession(Response authenticationResponse) {
    String token = extractSessionToken(authenticationResponse);
    String sessionId = extractSessionId(authenticationResponse);
    return new AuthenticatedSession(token, sessionId);
  }

  private String extractSessionToken(Response response) {
    try {
      JSONObject jsonBody = response.jsonBody;
      return jsonBody.getJSONObject(DATA_FIELD_KEY).getString("token");
    } catch (JSONException e) {
      throw new InvalidCredentialsException(e);
    }
  }

  private String extractSessionId(Response response) {
    Header sessionHeader = Arrays.stream(response.headers)
        .filter(header -> header.getName().equals("Set-Cookie"))
        .filter(cookieHeader -> cookieHeader.getValue().contains("JSESSIONID"))
        .findFirst()
        .orElseThrow(InvalidCredentialsException::new);
    String header = sessionHeader.getValue();
    return header.substring(header.indexOf('=') + 1, header.indexOf(';'));
  }

  public List<Account> extractAccounts(Response response) {
    try {
      JSONObject accounts = response.jsonBody.getJSONObject(DATA_FIELD_KEY);
      List<Account> result = new ArrayList<>();
      JSONArray savingAccounts = accounts.getJSONArray("sav");
      addAccounts(result, savingAccounts);
      JSONArray currentAccounts = accounts.getJSONArray("cur");
      addAccounts(result, currentAccounts);
      return result;
    } catch (JSONException e) {
      throw new InvalidResponseException(e);
    }
  }

  private void addAccounts(List<Account> aggregator, JSONArray savingAccounts) throws JSONException {
    for (int i = 0; i < savingAccounts.length(); i++) {
      JSONObject current = savingAccounts.getJSONObject(i);
      Account account = new Account(current.getString("acct"), new Money(current.getString("avbal")
          , current.getString("curr")), current.getString("name"));
      aggregator.add(account);

    }
  }
}
