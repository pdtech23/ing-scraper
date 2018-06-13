package scrapper.user.experience;

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


class ConsoleUserInterfaceTest {

  @Mocked
  private BufferedReader inputReaderMock;

  private ConsoleUserInterface testedUserInterface = new ConsoleUserInterface();

  @Test
  void shouldBeAbleToReadUsersLogin() throws IOException {
    // given
    new Expectations() {{
      inputReaderMock.readLine();
      result = "janusz";
    }};
    // when
    String result = testedUserInterface.askUserForLogin();
    // then
    assertEquals("janusz", result);
  }

  @Test
  void shouldBeAbleToReadUsersPassword() throws IOException {
    // given
    ArrayList<Integer> positionsOfRevealedCharacters = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    new Expectations() {{
      inputReaderMock.readLine();
      result = "j\n";
      times = 5;
    }};
    // when
    char[] result = testedUserInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);
    // then
    assertTrue(Arrays.equals("jjjjj".toCharArray(), result));
  }

  @Test
  void shouldBreakOnIncompletePassword() throws IOException {
    // given
    ArrayList<Integer> positionsOfRevealedCharacters = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    new Expectations() {{
      inputReaderMock.readLine();
      result = "j";
      times = 4;
      inputReaderMock.readLine();
      result = "";
    }};
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedUserInterface.askUserForNeededPasswordCharacters
        (positionsOfRevealedCharacters));
  }
}
