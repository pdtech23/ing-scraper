package challange.ing.scraper.fetching.session.unauthenticated;

import challange.ing.scraper.fetching.SingleRequestFetchingStrategy;
import challange.ing.scraper.request.RequestHelper;
import challange.ing.scraper.response.Response;
import challange.ing.scraper.response.ResponseHelper;
import challange.ing.scraper.response.exception.InvalidResponseException;
import challange.ing.session.UnauthenticatedSession;
import challange.ing.session.UnauthenticatedSessionBuilder;
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
      return new UnauthenticatedSessionBuilder()
          .withSalt(data.getString("salt"))
          .withMask(data.getString("mask"))
          .withKey(data.getString("key"))
          .withUnauthenticatedSessionId(ResponseHelper.extractSessionId(response))
          .create();
    } catch (JSONException e) {
      throw new InvalidResponseException(e);
    }
  }
}
