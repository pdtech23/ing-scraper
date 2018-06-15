package challange.ing.scraper.response;

import challange.ing.credentials.exception.InvalidCredentialsException;
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
      throw new InvalidCredentialsException(e);
    }
  }

  public static String extractSessionId(Response response) {
    Header sessionHeader = Arrays.stream(response.headers)
        .filter(header -> header.getName().equals("Set-Cookie"))
        .filter(cookieHeader -> cookieHeader.getValue().contains("JSESSIONID"))
        .findFirst()
        .orElseThrow(InvalidCredentialsException::new);
    String header = sessionHeader.getValue();
    return header.substring(header.indexOf('=') + 1, header.indexOf(';'));
  }
}
