package challenge.scraper.ing.session.authenticated;

public class AuthenticatedSession {

  public final String token;
  public final String authenticatedSessionId;

  public AuthenticatedSession(String token, String authenticatedSessionId) {
    this.token = token;
    this.authenticatedSessionId = authenticatedSessionId;
  }
}
