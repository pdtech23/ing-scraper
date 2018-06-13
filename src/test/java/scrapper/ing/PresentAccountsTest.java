package scrapper.ing;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapper.account.Account;
import scrapper.ing.client.Connection;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.user.experience.ConsoleUserInterface;

import java.util.Arrays;
import java.util.List;


class PresentAccountsTest {

  private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
  private static final List<Account> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO, TestHelper
      .SAMPLE_ACCOUNT_INFO);
  private static final String SAMPLE_LOGIN = "janusz";
  private static final AuthenticatedSession SAMPLE_SESSION_DATA = new AuthenticatedSession("token", "sessionId");
  private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);

  private PresentAccounts testedService;

  @Mocked
  private ConsoleUserInterface userInterface;
  @Mocked
  private Connection connection;

  @BeforeEach
  void setUp() {
    testedService = new PresentAccounts(userInterface, connection);
  }

  @Test
  void shouldDownloadAccountDataWithUserInteraction() {
    // given
    givenSomeLogin();
    givenSuccessfulConnection();
    givenSomePassword();
    givenPasswordAndLoginBeingCorrect();
    givenUserHavingAccounts();
    // when
    testedService.displayAccountsWithUserInteraction();
    // then
    // no exception is thrown and expectations are met
  }

  private void givenUserHavingAccounts() {
    new Expectations() {{
      connection.getAccountsInfo(SAMPLE_SESSION_DATA);
      result = SAMPLE_ACCOUNTS_LIST;
      userInterface.printAccounts(SAMPLE_ACCOUNTS_LIST);
    }};
  }

  private void givenSomeLogin() {
    new Expectations() {{
      userInterface.displayWelcomeMessage();
      userInterface.askUserForLogin();
      result = SAMPLE_LOGIN;
    }};
  }

  private void givenSuccessfulConnection() {
    new Expectations() {{
      connection.createUnauthenticatedSession(anyString);
      result = TestHelper.SAMPLE_UNAUTHENTICATED_SESSION;
    }};
  }

  private void givenSomePassword() {
    new Expectations() {{
      userInterface.askUserForNeededPasswordCharacters(SAMPLE_CHARACTERS_POSITIONS);
      result = SAMPLE_PASSWORD;
    }};
  }

  private void givenPasswordAndLoginBeingCorrect() {
    new Expectations() {{
      connection.createAuthenticatedSession(SAMPLE_LOGIN, SAMPLE_PASSWORD, TestHelper.SAMPLE_UNAUTHENTICATED_SESSION);
      result = SAMPLE_SESSION_DATA;
    }};
  }
}
