package challange.scraper.ing.accounts;

import challange.model.Account;
import challange.model.Money;
import challange.scraper.ing.SingleRequestFetchingStrategy;
import challange.scraper.ing.request.RequestHelper;
import challange.scraper.ing.response.Response;
import challange.scraper.ing.response.ResponseHelper;
import challange.scraper.exception.InvalidResponse;
import challange.scraper.ing.session.authenticated.AuthenticatedSession;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountsFetchingStrategy extends SingleRequestFetchingStrategy<List<Account>> {

  private AuthenticatedSession authenticatedSession;

  public AccountsFetchingStrategy(AuthenticatedSession authenticatedSession) {
    this.authenticatedSession = authenticatedSession;
  }

  @Override
  protected HttpUriRequest prepareRequest() {
    HttpPost request = new HttpPost(RequestHelper.GET_ALL_ACCOUNTS_URI);
    String json = "{" +
        RequestHelper.EMPTY_TRACE + "," +
        "\"token\":\"" + authenticatedSession.token + "\"," +
        RequestHelper.LOCALE_PL +
        "}";
    RequestHelper.addJsonBodyWithHeader(request, json);
    RequestHelper.setSessionIdCookie(request, authenticatedSession.authenticatedSessionId);
    RequestHelper.setHeadersNecessaryToPretendBrowser(request);
    return request;
  }

  @Override
  protected List<Account> extractResultFrom(Response response) {
    try {
      JSONObject accounts = response.jsonBody.getJSONObject(ResponseHelper.DATA_FIELD_KEY);
      List<Account> result = new ArrayList<>();
      JSONArray savingAccounts = accounts.getJSONArray("sav");
      addAccounts(result, savingAccounts);
      JSONArray currentAccounts = accounts.getJSONArray("cur");
      addAccounts(result, currentAccounts);
      return result;
    } catch (JSONException e) {
      throw new InvalidResponse(e);
    }
  }

  private void addAccounts(List<Account> aggregator, JSONArray savingAccounts) throws JSONException {
    for (int i = 0; i < savingAccounts.length(); i++) {
      JSONObject current = savingAccounts.getJSONObject(i);
      Account account = new Account(current.getString("acct"),
                                    new Money(current.getString("avbal"), current.getString("curr")),
                                    current.getString("name"));
      aggregator.add(account);
    }
  }
}
