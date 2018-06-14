package challange.ing;

import challange.FetchAccountsUseCase;
import challange.account.Account;
import challange.ing.scraper.IIngScraper;
import challange.ing.security.session.AuthenticatedSession;
import challange.user.experience.IConsoleUI;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


class FetchAccountsUseCaseTest {

  private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
  private static final List<Account> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO, TestHelper
      .SAMPLE_ACCOUNT_INFO);
  private static final String SAMPLE_LOGIN = "janusz";
  private static final AuthenticatedSession SAMPLE_SESSION_DATA = new AuthenticatedSession("token", "authenticatedSessionId");
  private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);

  private FetchAccountsUseCase testedService;

  @Mocked
  private IConsoleUI userInterface;
  @Mocked
  private IIngScraper IIngScraper;

  @BeforeEach
  void setUp() {
    testedService = new FetchAccountsUseCase(userInterface, IIngScraper);
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
    testedService.execute();
    // then
    // no exception is thrown and expectations are met
  }

  private void givenUserHavingAccounts() {
    new Expectations() {{
      IIngScraper.fetchAccounts(SAMPLE_SESSION_DATA);
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
      IIngScraper.createUnauthenticatedSession(anyString);
      result = TestHelper.SAMPLE_UNAUTHENTICATED_SESSION;
    }};
  }

  private void givenSomePassword() {
    new Expectations() {{
      userInterface.askUserForNeededPasswordChars(SAMPLE_CHARACTERS_POSITIONS);
      result = SAMPLE_PASSWORD;
    }};
  }

  private void givenPasswordAndLoginBeingCorrect() {
    new Expectations() {{
      IIngScraper.createAuthenticatedSession(SAMPLE_LOGIN, SAMPLE_PASSWORD, TestHelper.SAMPLE_UNAUTHENTICATED_SESSION);
      result = SAMPLE_SESSION_DATA;
    }};
  }
}
