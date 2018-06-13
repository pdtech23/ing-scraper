package scraper.ing;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scraper.account.Account;
import scraper.ing.client.IngScraper;
import scraper.ing.security.AuthenticatedSession;
import scraper.user.experience.ConsoleUserInterface;

import java.util.Arrays;
import java.util.List;


class DisplayAccountsUseCaseTest {

  private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
  private static final List<Account> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO, TestHelper
      .SAMPLE_ACCOUNT_INFO);
  private static final String SAMPLE_LOGIN = "janusz";
  private static final AuthenticatedSession SAMPLE_SESSION_DATA = new AuthenticatedSession("token", "sessionId");
  private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);

  private DisplayAccountsUseCase testedService;

  @Mocked
  private ConsoleUserInterface userInterface;
  @Mocked
  private IngScraper ingScraper;

  @BeforeEach
  void setUp() {
    testedService = new DisplayAccountsUseCase(userInterface, ingScraper);
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
      ingScraper.getAccountsInfo(SAMPLE_SESSION_DATA);
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
      ingScraper.createUnauthenticatedSession(anyString);
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
      ingScraper.createAuthenticatedSession(SAMPLE_LOGIN, SAMPLE_PASSWORD, TestHelper.SAMPLE_UNAUTHENTICATED_SESSION);
      result = SAMPLE_SESSION_DATA;
    }};
  }
}
