package challenge.scraper.ing.session.credentials;

import challenge.scraper.ing.session.unauthenticated.UnauthenticatedSession;
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
    for (int i = 0; i < maskChars.length; ++i)
      if (maskChars[i] == MARKER)
        result.add(i + 1);
    return result;
  }

  public static String createPasswordHash(UnauthenticatedSession unauthenticatedSession, char[] password) {
    String saltWithPassword = PasswordBehaviorHandler.mixSaltAndPassword(PasswordBehaviorHandler.createSaltWithMaskOn(unauthenticatedSession), password);
    return HmacUtils.hmacSha1Hex(unauthenticatedSession.key, saltWithPassword);
  }

  private static String mixSaltAndPassword(String saltWithMask, char[] passphrase) {
    String result = "";
    int currCharNum = 0;
    for (int i = 0; i < saltWithMask.length(); ++i)
      if (saltWithMask.charAt(i) == MARKER)
        result += passphrase[currCharNum++];
      else
        result += saltWithMask.charAt(i);
    return result;
  }

  private static String createSaltWithMaskOn(UnauthenticatedSession unauthenticatedSession) {
    String result = "";
    for (int i = 0; i < unauthenticatedSession.mask.length(); i++)
      if (unauthenticatedSession.mask.charAt(i) == MARKER)
        result += MARKER;
      else
        result += unauthenticatedSession.salt.charAt(i);
    return result;
  }
}
