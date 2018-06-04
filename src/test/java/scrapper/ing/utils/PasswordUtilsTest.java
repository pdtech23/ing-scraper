package scrapper.ing.utils;

import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PasswordUtilsTest {


    @Test
    public void shouldReturnCorrespondingIndexes() {
        // given
        List<Integer> indexesCorrespondingToStars = Arrays.asList(0, 2, 5, 9, 14);
        String sampleMask = "*+*++*+++*++++*+++++++++++++++++";

        // when
        List<Integer> result = PasswordUtils.extractStarIndexesFromMask(sampleMask);

        // then
        assertEquals(indexesCorrespondingToStars, result);
    }

    @Test
    public void shouldMixPasswordAndSaltAsInJS() {
        // given
        String saltWithMask = "**1**t*gyxKitJlqguphPHqKFH3DEkJ7";
        List<Character> passphrase = Arrays.asList('t', 'e', 's', 't', '1');

        // when
        String result = PasswordUtils.mixSaltAndPasswordAsInJS(saltWithMask, passphrase.iterator());

        // then
        assertEquals("te1stt1gyxKitJlqguphPHqKFH3DEkJ7", result);
    }

    @Test
    public void shouldPutMaskOnSalt() {
        // given
        String sampleSalt = "Gj1Uit0gyxKitJlqguphPHqKFH3DEkJ7";
        String sampleMask = "**+**+*+++++++++++++++++++++++++";

        // when
        String result = PasswordUtils.putMaskOnSalt(sampleMask, sampleSalt);

        // then
        assertEquals("**1**t*gyxKitJlqguphPHqKFH3DEkJ7", result);
    }

    @Test
    public void shouldReturnEmptyWhenMaskLongerThanSalt() {
        // given
        String sampleSalt = "Gj1Uit0gyxKitJlqguph3DEkJ7";
        String sampleMask = "*+*++*+++*++++*+++++++++++++++++";

        // when
        String result = PasswordUtils.putMaskOnSalt(sampleMask, sampleSalt);

        // then
        assertEquals("", result);
    }

    @Test
    public void shouldBeHashAsInJS() {
        // given
        String sampleSalt = "tk0XpsU5dAShJjJ5BS6nOnymXfCBRuSj";
        String sampleMask = "**++*++*+*++++++++++++++++++++++";
        String sampleKey = "75804255617903534713114162762950";

        // when
        String maskOnSalt = PasswordUtils.putMaskOnSalt(sampleMask, sampleSalt);
        String mixOfSaltAndPassword = PasswordUtils.mixSaltAndPasswordAsInJS(maskOnSalt, Arrays.asList('A', 'g', 'c', '#', '7').iterator());
        String pwdHash = HmacUtils.hmacSha1Hex(sampleKey, mixOfSaltAndPassword);

        // then
        assertEquals("54efff0ae5d07baae2e531635a12bb0785fb56c1", pwdHash);

    }
}