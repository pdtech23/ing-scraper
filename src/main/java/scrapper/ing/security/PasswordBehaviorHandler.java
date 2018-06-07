package scrapper.ing.security;

import org.apache.commons.codec.digest.HmacUtils;

import java.util.ArrayList;
import java.util.List;

public class PasswordBehaviorHandler {

    public static final int NUMBER_OF_REVEALED_CHARACTERS = 5;
    private static final char MARKER = '*';

    private PasswordBehaviorHandler() {
    }

    public static List<Integer> extractPositionsOfRevealedCharacters(String mask) {
        char[] chars = mask.toCharArray();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == MARKER) {
                result.add(i + 1);
            }
        }
        return result;
    }

    public static String createPasswordHash(UnauthenticatedSession unauthenticatedSession, char[] password) {
        String saltWithPassword = PasswordBehaviorHandler.mixSaltAndPassword(PasswordBehaviorHandler
                .createSaltWithMaskOn(unauthenticatedSession), password);
        return HmacUtils.hmacSha1Hex(unauthenticatedSession.getKey(), saltWithPassword);
    }

    public static String mixSaltAndPassword(String saltWithMask, char[] passphrase) {
        StringBuilder result = new StringBuilder();
        int currentCharacterIndex = 0;
        for (int i = 0; i < saltWithMask.length(); ++i) {
            if (saltWithMask.charAt(i) == MARKER) {
                result.append(passphrase[currentCharacterIndex++]);
            } else {
                result.append(saltWithMask.charAt(i));
            }
        }
        return result.toString();
    }

    public static String createSaltWithMaskOn(UnauthenticatedSession metadata) {
        String mask = metadata.getMask();
        String salt = metadata.getSalt();

        if (mask.length() > salt.length()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == MARKER) {
                result.append(MARKER);
            } else {
                result.append(salt.charAt(i));
            }
        }
        return result.toString();
    }
}
