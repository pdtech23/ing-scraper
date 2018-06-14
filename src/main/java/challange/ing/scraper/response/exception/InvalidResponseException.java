package challange.ing.scraper.response.exception;

import org.json.JSONException;

public class InvalidResponseException extends RuntimeException {

  public InvalidResponseException(JSONException e) {
    super("Unexpected response structure. Details: ", e);
  }
}
