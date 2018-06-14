package challange.ing.security.session;

import java.util.List;

public class UnauthenticatedSession {

  public final String salt;
  public final String mask;
  public final String key;
  public final String unauthenticatedSessionId;
  public final List<Integer> positionsOfRevealedCharacters;

  public UnauthenticatedSession(String salt, String mask, String key, String unauthenticatedSessionId, List<Integer>
      positionsOfRevealedCharacters) {
    this.salt = salt;
    this.mask = mask;
    this.key = key;
    this.unauthenticatedSessionId = unauthenticatedSessionId;
    this.positionsOfRevealedCharacters = positionsOfRevealedCharacters;
  }
}
