package challange.ing.security;

import challange.ing.credentials.PasswordBehaviorHandler;
import challange.ing.session.UnauthenticatedSession;
import challange.ing.session.UnauthenticatedSessionBuilder;
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
  void shouldBeHashAsInJS() {
    // given
    UnauthenticatedSession unauthenticatedSession = new UnauthenticatedSessionBuilder()
        .withSalt("tk0XpsU5dAShJjJ5BS6nOnymXfCBRuSj")
        .withMask("**++*++*+*++++++++++++++++++++++")
        .withKey("75804255617903534713114162762950")
        .withUnauthenticatedSessionId("")
        .create();
    // when
    String pwdHash = PasswordBehaviorHandler.createPasswordHash(unauthenticatedSession, new char[]{'B', '5', '8', '3', 'A'});
    // then
    assertEquals("81a3ba9c32583787d0b3a1e33c36922167036d6b", pwdHash);

  }
}
