package challange.ing.security;

import challange.ing.security.session.UnauthenticatedSession;
import challange.ing.security.session.UnauthenticatedSessionBuilder;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Assertions;
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
    UnauthenticatedSession unauthenticatedSession = new UnauthenticatedSessionBuilder()
        .withSalt("Aj1Uit0gyxKitJl66uphPHqKFH3DEk33")
        .withMask("**+**+*+++++++++++++++++++++++++")
        .withKey("")
        .withUnauthenticatedSessionId("")
        .create();
    // when
    String result = PasswordBehaviorHandler.createSaltWithMaskOn(unauthenticatedSession);
    // then
    assertEquals("**1**t*gyxKitJl66uphPHqKFH3DEk33", result);
  }

  @Test
  void shouldFailWhenMaskLongerThanSalt() {
    // given
    String sampleSalt = "Gj1Uit0gyxKitJlqguph3DEkJ7";
    String sampleMask = "*+*++*+++*++++*+++++++++++++++++";
    UnauthenticatedSession unauthenticatedSession = new UnauthenticatedSessionBuilder().withSalt(sampleSalt).withMask
        (sampleMask).withKey("").withUnauthenticatedSessionId("").create();
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> PasswordBehaviorHandler.createSaltWithMaskOn
        (unauthenticatedSession));
  }

  @Test
  void shouldBeHashAsInJS() {
    // given
    String sampleKey = "75804255617903534713114162762950";
    UnauthenticatedSession unauthenticatedSession = new UnauthenticatedSessionBuilder()
        .withSalt("tk0XpsU5dAShJjJ5BS6nOnymXfCBRuSj")
        .withMask("**++*++*+*++++++++++++++++++++++")
        .withKey(sampleKey)
        .withUnauthenticatedSessionId("")
        .create();
    String maskOnSalt = PasswordBehaviorHandler.createSaltWithMaskOn(unauthenticatedSession);
    String mixOfSaltAndPassword = PasswordBehaviorHandler.mixSaltAndPassword(maskOnSalt, new char[]{'A', 'g', 'c',
        '#', '7'});
    // when
    String pwdHash = HmacUtils.hmacSha1Hex(sampleKey, mixOfSaltAndPassword);
    // then
    assertEquals("54efff0ae5d07baae2e531635a12bb0785fb56c1", pwdHash);

  }
}
