package scrapper.ing.security;

import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordBehaviorHandlerTest {


    @Test
    void shouldReturnCorrespondingIndexes() {
        // given
        List<Integer> positionsCorrespondingToStars = Arrays.asList(1, 3, 6, 10, 15);
        String sampleMask = "*+*++*+++*++++*+++++++++++++++++";

        // when
        List<Integer> result = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters(sampleMask);

        // then
        assertEquals(positionsCorrespondingToStars, result);
    }

    @Test
    void shouldMixPasswordAndSaltAsInJS() {
        // given
        String saltWithMask = "**1**t*gyxKitJlqguphPHqKFH3DEkJ7";
        char[] passphrase = new char[]{'t', 'e', 's', 't', '1'};

        // when
        String result = PasswordBehaviorHandler.mixSaltAndPassword(saltWithMask, passphrase);

        // then
        assertEquals("te1stt1gyxKitJlqguphPHqKFH3DEkJ7", result);
    }

    @Test
    void shouldPutMaskOnSalt() {
        // given
        String sampleSalt = "Gj1Uit0gyxKitJlqguphPHqKFH3DEkJ7";
        String sampleMask = "**+**+*+++++++++++++++++++++++++";
        UnauthenticatedSession metadata = new UnauthenticatedSession(sampleSalt, sampleMask, "", "");

        // when
        String result = PasswordBehaviorHandler.createSaltWithMaskOn(metadata);

        // then
        assertEquals("**1**t*gyxKitJlqguphPHqKFH3DEkJ7", result);
    }

    @Test
    void shouldReturnEmptyWhenMaskLongerThanSalt() {
        // given
        String sampleSalt = "Gj1Uit0gyxKitJlqguph3DEkJ7";
        String sampleMask = "*+*++*+++*++++*+++++++++++++++++";
        UnauthenticatedSession metadata = new UnauthenticatedSession(sampleSalt, sampleMask, "", "");

        // when
        String result = PasswordBehaviorHandler.createSaltWithMaskOn(metadata);

        // then
        assertEquals("", result);
    }

    @Test
    void shouldBeHashAsInJS() {
        // given
        String sampleSalt = "tk0XpsU5dAShJjJ5BS6nOnymXfCBRuSj";
        String sampleMask = "**++*++*+*++++++++++++++++++++++";
        String sampleKey = "75804255617903534713114162762950";
        UnauthenticatedSession metadata = new UnauthenticatedSession(sampleSalt, sampleMask, sampleKey, "");

        // when
        String maskOnSalt = PasswordBehaviorHandler.createSaltWithMaskOn(metadata);
        String mixOfSaltAndPassword = PasswordBehaviorHandler.mixSaltAndPassword(maskOnSalt, new char[]{'A', 'g',
                'c', '#', '7'});
        String pwdHash = HmacUtils.hmacSha1Hex(sampleKey, mixOfSaltAndPassword);

        // then
        assertEquals("54efff0ae5d07baae2e531635a12bb0785fb56c1", pwdHash);

    }
}
