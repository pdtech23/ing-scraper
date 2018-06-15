package challange.scraper.ing.response;

import challange.scraper.exception.InvalidCredentials;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ResponseHelper {

  public static final String DATA_FIELD_KEY = "data";

  private ResponseHelper() {
  }

  public static String extractSessionToken(Response response) {
    try {
      JSONObject jsonBody = response.jsonBody;
      return jsonBody.getJSONObject(ResponseHelper.DATA_FIELD_KEY).getString("token");
    } catch (JSONException e) {
      throw new InvalidCredentials(e);
    }
  }

  public static String extractSessionId(Response response) {
    Header sessionHeader = Arrays.stream(response.headers)
        .filter(header -> header.getName().equals("Set-Cookie"))
        .filter(cookieHeader -> cookieHeader.getValue().contains("JSESSIONID"))
        .findFirst()
        .orElseThrow(InvalidCredentials::new);
    String header = sessionHeader.getValue();
    return header.substring(header.indexOf('=') + 1, header.indexOf(';'));
  }
}
