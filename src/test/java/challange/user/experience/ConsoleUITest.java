package challange.user.experience;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ConsoleUITest {

  @Mocked
  private BufferedReader inputReaderMock;

  private UI testedUI = new ConsoleUI();

  @Test
  void shouldBeAbleToReadUsersLogin() throws IOException {
    // given
    new Expectations() {{
      inputReaderMock.readLine();
      result = "janusz";
    }};
    // when
    String result = testedUI.askUserForLogin();
    // then
    assertEquals("janusz", result);
  }

  @Test
  void shouldBeAbleToReadUsersPassword() throws IOException {
    // given
    ArrayList<Integer> positionsOfRevealedCharacters = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    givenPasswordByUser();
    // when
    char[] result = testedUI.askUserForNeededPasswordChars(positionsOfRevealedCharacters);
    // then
    assertTrue(Arrays.equals("jjjjj".toCharArray(), result));
  }

  private void givenPasswordByUser() throws IOException {
    new Expectations() {{
      inputReaderMock.readLine();
      result = "j\n";
      times = 5;
    }};
  }

  @Test
  void shouldBreakOnIncompletePassword() throws IOException {
    // given
    ArrayList<Integer> positionsOfRevealedCharacters = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    givenIncompletePasswordByUser();
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedUI.askUserForNeededPasswordChars
        (positionsOfRevealedCharacters));
  }

  private void givenIncompletePasswordByUser() throws IOException {
    new Expectations() {{
      inputReaderMock.readLine();
      result = "j";
      times = 4;
      inputReaderMock.readLine();
      result = "";
    }};
  }
}
