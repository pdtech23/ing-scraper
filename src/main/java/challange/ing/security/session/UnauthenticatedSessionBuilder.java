package challange.ing.security.session;

import challange.ing.security.PasswordBehaviorHandler;

import java.util.List;

public class UnauthenticatedSessionBuilder {
  private String salt;
  private String mask;
  private String key;
  private String unauthenticatedSessionId;
  private List<Integer> positionsOfRevealedCharacters;

  public UnauthenticatedSessionBuilder withSalt(String salt) {
    this.salt = salt;
    return this;
  }

  public UnauthenticatedSessionBuilder withMask(String mask) {
    this.mask = mask;
    this.positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters(mask);
    return this;
  }

  public UnauthenticatedSessionBuilder withKey(String key) {
    this.key = key;
    return this;
  }

  public UnauthenticatedSessionBuilder withUnauthenticatedSessionId(String unauthenticatedSessionId) {
    this.unauthenticatedSessionId = unauthenticatedSessionId;
    return this;
  }

  public UnauthenticatedSession create() {
    return new UnauthenticatedSession(salt, mask, key, unauthenticatedSessionId, positionsOfRevealedCharacters);
  }
}