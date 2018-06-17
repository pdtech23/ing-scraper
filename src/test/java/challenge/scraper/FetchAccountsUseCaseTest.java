package challenge.scraper;

import challenge.FetchAccountsUseCase;
import challenge.TestHelper;
import challenge.model.Account;
import challenge.user.experience.UI;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


class FetchAccountsUseCaseTest {

  private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
  private static final List<Account> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO, TestHelper.SAMPLE_ACCOUNT_INFO);
  private static final String SAMPLE_LOGIN = "janusz";
  private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);

  private FetchAccountsUseCase testedService;

  @Mocked
  private UI userInterface;
  @Mocked
  private Scraper Scraper;

  @BeforeEach
  void setUp() {
    testedService = new FetchAccountsUseCase(userInterface, Scraper);
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
      Scraper.fetchAccounts(TestHelper.SAMPLE_AUTHENTICATED_SESSION);
      result = SAMPLE_ACCOUNTS_LIST;
      userInterface.displayAccounts(SAMPLE_ACCOUNTS_LIST);
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
      Scraper.fetchUnauthenticatedSession(anyString);
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
      Scraper.fetchAuthenticatedSession(SAMPLE_LOGIN, SAMPLE_PASSWORD, TestHelper.SAMPLE_UNAUTHENTICATED_SESSION);
      result = TestHelper.SAMPLE_AUTHENTICATED_SESSION;
    }};
  }
}
