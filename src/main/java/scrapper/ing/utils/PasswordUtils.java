package scrapper.ing.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PasswordUtils {

    public static final char MARKER = '*';

    public static List<Integer> extractStarIndexesFromMask(String mask) {
        char[] chars = mask.toCharArray();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == MARKER) {
                result.add(i);
            }
        }
        return result;
    }

    public static String mixSaltAndPasswordAsInJS(String saltWithMask, Iterator<Character> passphrase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < saltWithMask.length(); i++) {
            if (saltWithMask.charAt(i) == MARKER) {
                result.append(passphrase.next());
            } else {
                result.append(saltWithMask.charAt(i));
            }
        }
        return result.toString();
    }

    public static String putMaskOnSalt(String mask, String salt) {
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
