package scrapper.ing.security;

public class AuthenticatedSession {

  public final String token;
  public final String authenticatedSessionId;

  public AuthenticatedSession(String token, String sessionId) {
    this.token = token;
    authenticatedSessionId = sessionId;
  }
}
