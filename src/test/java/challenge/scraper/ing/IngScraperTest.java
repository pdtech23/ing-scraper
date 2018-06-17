package challenge.scraper.ing;

import challenge.scraper.Scraper;
import challenge.TestHelper;
import challenge.scraper.ing.session.authenticated.AuthenticatedSession;
import challenge.scraper.ing.session.unauthenticated.UnauthenticatedSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IngScraperTest {

  private final Scraper testedService = new IngScraper();

  @Test
  void shouldProceedOnAnyNonEmptyLogin() {
    // given
    String login = "test";
    // when
    testedService.fetchUnauthenticatedSession(login);
    // then
    // no exception is thrown
  }

  @Test
  void shouldFailOnEmptyLogin() {
    // given
    String login = "";
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.fetchUnauthenticatedSession(login));
  }

  @Test
  void shouldFailOnIncorrectLoginPasswordPair() {
    // given
    String login = "test";
    char[] password = "abcde".toCharArray();
    UnauthenticatedSession unauthenticatedSession = TestHelper.SAMPLE_UNAUTHENTICATED_SESSION;
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.fetchAuthenticatedSession(login, password,
                                                                                                  unauthenticatedSession));
  }

  // due to lack of test credentials can't implement it in a meaningful way
  // shouldProceedOnProperLoginPasswordPair()


  @Test
  void shouldFailWithoutAuthentication() {
    // given
    AuthenticatedSession authenticatedSession = TestHelper.SAMPLE_AUTHENTICATED_SESSION;
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.fetchAccounts(authenticatedSession));
  }
}
