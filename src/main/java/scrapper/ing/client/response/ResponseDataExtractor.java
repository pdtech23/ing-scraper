package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.account.Money;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.security.SessionData;

import java.util.*;

public class ResponseDataExtractor {

    public PasswordMetadata extractPasswordMetadata(ResponseData responseData) {

        try {
            JSONObject data = responseData.getJsonBody().getJSONObject("data");
            return new PasswordMetadata(data.getString("salt"), data.getString("mask"), data.getString("key"), this
                    .extractSessionId(responseData));
        } catch (JSONException e) {
            e.printStackTrace();
            return PasswordMetadata.EMPTY;
        }

    }

    private String extractSessionToken(ResponseData responseResult) {
        try {
            return responseResult.getJsonBody().getJSONObject("data").getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String extractSessionId(ResponseData responseData) {
        Optional<Header> sessionHeader = Arrays.stream(responseData.getHeaders()).filter(header -> header.getName()
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

    public SessionData extractAuthenticatedSession(ResponseData authorizationResponse) {
        String token = this.extractSessionToken(authorizationResponse);
        String sessionId = this.extractSessionId(authorizationResponse);
        if (token.isEmpty() || sessionId.isEmpty()) {
            return SessionData.EMPTY;
        }
        return new SessionData(token, sessionId);
    }

    public List<IngAccountInfo> extractAccountsInfo(ResponseData jsonResponse) {
        try {
            JSONObject accounts = jsonResponse.getJsonBody().getJSONObject("data");
            List<IngAccountInfo> result = new ArrayList<>();
            JSONArray savingAccounts = accounts.getJSONArray("sav");
            JSONArray currentAccounts = accounts.getJSONArray("cur");

            this.addAccounts(result, savingAccounts);
            this.addAccounts(result, currentAccounts);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void addAccounts(List<IngAccountInfo> result, JSONArray savingAccounts) throws JSONException {
        for (int i = 0; i < savingAccounts.length(); i++) {
            JSONObject node = savingAccounts.getJSONObject(i);
            IngAccountInfo account = new IngAccountInfo(node.getString("acct"), new Money(node.getDouble("avbal"),
                    node.getString("curr")), node.getString("name"));
            result.add(account);
        }
    }
}

