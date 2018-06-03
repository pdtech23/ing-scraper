package scrapper.ing.utils;

import java.util.ArrayList;
import java.util.List;

public class PasswordUtils {
    public static List<Integer> extractStarIndexesFromMask(String mask) {
        char[] chars = mask.toCharArray();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == '*') {
                result.add(i);
            }
        }
        return result;
    }
}
