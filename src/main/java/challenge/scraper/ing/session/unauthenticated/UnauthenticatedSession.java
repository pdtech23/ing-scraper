package challenge.scraper.ing.session.unauthenticated;

import challenge.scraper.ing.session.credentials.PasswordBehaviorHandler;

import java.util.List;

public class UnauthenticatedSession {

  public final String salt;
  public final String mask;
  public final String key;
  public final String unauthenticatedSessionId;
  public final List<Integer> positionsOfRevealedCharacters;

  public UnauthenticatedSession(String salt, String mask, String key, String unauthenticatedSessionId) {
    this.salt = salt;
    this.mask = mask;
    this.key = key;
    this.unauthenticatedSessionId = unauthenticatedSessionId;
    this.positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters(mask);
  }
}
