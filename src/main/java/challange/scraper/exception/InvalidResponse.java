package challange.scraper.exception;

import org.json.JSONException;

public class InvalidResponse extends RuntimeException {

  public InvalidResponse(JSONException e) {
    super("Unexpected response structure. Details: ", e);
  }
}
