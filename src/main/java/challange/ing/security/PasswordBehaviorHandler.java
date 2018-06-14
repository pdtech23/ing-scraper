package challange.ing.security;

import challange.ing.security.session.UnauthenticatedSession;
import org.apache.commons.codec.digest.HmacUtils;

import java.util.ArrayList;
import java.util.List;

public class PasswordBehaviorHandler {

  private static final char MARKER = '*';

  private PasswordBehaviorHandler() {
  }

  public static List<Integer> extractPositionsOfRevealedCharacters(String mask) {
    char[] maskChars = mask.toCharArray();
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < maskChars.length; ++i) {
      if (maskChars[i] == MARKER)
        result.add(i + 1);
    }
    return result;
  }

  public static String createPasswordHash(UnauthenticatedSession unauthenticatedSession, char[] password) {
    String saltWithPassword = PasswordBehaviorHandler.mixSaltAndPassword(PasswordBehaviorHandler.createSaltWithMaskOn
        (unauthenticatedSession), password);
    return HmacUtils.hmacSha1Hex(unauthenticatedSession.key, saltWithPassword);
  }

  static String mixSaltAndPassword(String saltWithMask, char[] passphrase) {
    StringBuilder result = new StringBuilder();
    int currentCharacterIndex = 0;
    for (int i = 0; i < saltWithMask.length(); ++i) {
      if (saltWithMask.charAt(i) == MARKER)
        result.append(passphrase[currentCharacterIndex++]);
      else
        result.append(saltWithMask.charAt(i));
    }
    return result.toString();
  }

  static String createSaltWithMaskOn(UnauthenticatedSession unauthenticatedSession) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < unauthenticatedSession.mask.length(); i++) {
      if (unauthenticatedSession.mask.charAt(i) == MARKER)
        result.append(MARKER);
      else
        result.append(unauthenticatedSession.salt.charAt(i));
    }
    return result.toString();
  }
}
