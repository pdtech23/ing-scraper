package challenge.scraper.ing.session.unauthenticated;

import challenge.scraper.exception.InvalidResponse;
import challenge.scraper.ing.SingleRequestFetchingStrategy;
import challenge.scraper.ing.request.RequestHelper;
import challenge.scraper.ing.response.Response;
import challenge.scraper.ing.response.ResponseHelper;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

public class UnauthenticatedSessionFetchingStrategy extends SingleRequestFetchingStrategy<UnauthenticatedSession> {

  private String login;

  public UnauthenticatedSessionFetchingStrategy(String login) {
    this.login = login;
  }

  @Override
  protected HttpPost prepareRequest() {
    HttpPost request = new HttpPost(RequestHelper.CHECK_LOGIN_URI);
    String json = "{" +
        RequestHelper.EMPTY_TOKEN + "," +
        RequestHelper.EMPTY_TRACE + "," +
        "\"data\":{" +
            "\"login\":\"" + login + "\"" +
            "}," +
        RequestHelper.LOCALE_PL +
        "}";
    RequestHelper.addJsonBodyWithHeader(request, json);
    return request;
  }

  @Override
  protected UnauthenticatedSession extractResultFrom(Response response) {
    try {
      JSONObject data = response.jsonBody.getJSONObject(ResponseHelper.DATA_FIELD_KEY);
      return new UnauthenticatedSession(data.getString("salt"), data.getString("mask"),
                                        data.getString("key"), ResponseHelper.extractSessionId(response));
    } catch (JSONException e) {
      throw new InvalidResponse(e);
    }
  }
}
