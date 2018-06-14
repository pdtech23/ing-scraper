package challange.ing.security.session.exception;

import org.json.JSONException;

public class InvalidCredentialsException extends RuntimeException {

  private static final String FAILURE_MESSAGE = "Invalid login or password.";

  public InvalidCredentialsException() {
    super(FAILURE_MESSAGE);
  }

  public InvalidCredentialsException(JSONException e) {
    super(FAILURE_MESSAGE, e);
  }
}
