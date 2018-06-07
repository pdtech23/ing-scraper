package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.account.Money;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;

import java.util.*;

public class ResponseDataExtractor {

    private static final String DATA_FIELD_KEY = "data";
    private static final String SAV_FIELD_KEY = "sav";
    private static final String CUR_FIELD_KEY = "cur";
    private static final String ACCOUNT_KEY = "acct";
    private static final String AVAILABLE_BALANCE_KEY = "avbal";
    private static final String CURRENCY_KEY = "curr";
    private static final String NAME_KEY = "name";
    private static final String SALT = "salt";
    private static final String MASK = "mask";
    private static final String KEY = "key";

    public UnauthenticatedSession extractUnauthenticatedSession(Response response) {
        try {
            JSONObject jsonBody = response.getJsonBody();
            if (!jsonBody.has(DATA_FIELD_KEY)) {
                return UnauthenticatedSession.EMPTY;
            }
            JSONObject data = response.getJsonBody().getJSONObject(DATA_FIELD_KEY);
            if (data.has(SALT) && data.has(MASK) && data.has(KEY)) {
                return new UnauthenticatedSession(data.getString(SALT), data.getString(MASK), data.getString(KEY), this
                        .extractSessionId(response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return UnauthenticatedSession.EMPTY;
    }

    private String extractSessionToken(Response response) {
        try {
            JSONObject jsonBody = response.getJsonBody();
            if (!jsonBody.has(DATA_FIELD_KEY)) {
                return "";
            }
            JSONObject data = jsonBody.getJSONObject(DATA_FIELD_KEY);
            if (data.has("token")) {
                return data.getString("token");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public AuthenticatedSession extractAuthenticatedSession(Response authenticationResponse) {
        String token = this.extractSessionToken(authenticationResponse);
        String sessionId = this.extractSessionId(authenticationResponse);
        if (token.isEmpty() || sessionId.isEmpty()) {
            return AuthenticatedSession.EMPTY;
        }
        return new AuthenticatedSession(token, sessionId);
    }

    private String extractSessionId(Response response) {
        Optional<Header> sessionHeader = Arrays.stream(response.getHeaders()).filter(header -> header.getName()
                .equals("Set-Cookie")).filter(cookieHeader -> cookieHeader.getValue().contains("JSESSIONID"))
                .findFirst();

        if (!sessionHeader.isPresent()) {
            return "";
        }

        String header = sessionHeader.get().getValue();
        int i = header.indexOf('=') + 1;
        int j = header.indexOf(';');
        return header.substring(i, j);
    }

    public List<IngAccountInfo> extractAccountsInfo(Response response) {
        try {
            JSONObject jsonBody = response.getJsonBody();
            if (!jsonBody.has(DATA_FIELD_KEY)) {
                return Collections.emptyList();
            }

            JSONObject accounts = jsonBody.getJSONObject(DATA_FIELD_KEY);
            if (!accounts.has(SAV_FIELD_KEY) || !accounts.has(CUR_FIELD_KEY)) {
                return Collections.emptyList();
            }

            JSONArray savingAccounts = accounts.getJSONArray(SAV_FIELD_KEY);
            JSONArray currentAccounts = accounts.getJSONArray(CUR_FIELD_KEY);

            List<IngAccountInfo> result = new ArrayList<>();
            this.addAccounts(result, savingAccounts);
            this.addAccounts(result, currentAccounts);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void addAccounts(List<IngAccountInfo> aggregator, JSONArray savingAccounts) throws JSONException {
        for (int i = 0; i < savingAccounts.length(); i++) {
            JSONObject current = savingAccounts.getJSONObject(i);

            if (!current.has(ACCOUNT_KEY) || !current.has(AVAILABLE_BALANCE_KEY) || !current.has(CURRENCY_KEY) ||
                    !current.has(NAME_KEY)) {
                return;
            }

            IngAccountInfo account = new IngAccountInfo(current.getString(ACCOUNT_KEY), new Money(current.getDouble
                    (AVAILABLE_BALANCE_KEY), current.getString(CURRENCY_KEY)), current.getString(NAME_KEY));
            aggregator.add(account);
        }
    }
}

