package challange.scraper.exception;

import org.json.JSONException;

public class InvalidCredentials extends RuntimeException {

  private static final String FAILURE_MESSAGE = "Invalid login or password.";

  public InvalidCredentials() {
    super(FAILURE_MESSAGE);
  }

  public InvalidCredentials(JSONException e) {
    super(FAILURE_MESSAGE, e);
  }
}
