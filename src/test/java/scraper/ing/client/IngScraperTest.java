package scraper.ing.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scraper.ing.TestHelper;
import scraper.ing.security.AuthenticatedSession;
import scraper.ing.security.UnauthenticatedSession;

class IngScraperTest {

  private final IngScraper testedService = new IngScraper();

  @Test
  void shouldProceedOnAnyNonEmptyLogin() {
    // given
    String login = "test";
    // when
    testedService.createUnauthenticatedSession(login);
    // then
    // no exception is thrown
  }

  @Test
  void shouldFailOnEmptyLogin() {
    // given
    String login = "";
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.createUnauthenticatedSession(login));
  }

  @Test
  void shouldFailOnIncorrectLoginPasswordPair() {
    // given
    String login = "test";
    char[] password = "abcde".toCharArray();
    UnauthenticatedSession unauthenticatedSession = TestHelper.SAMPLE_UNAUTHENTICATED_SESSION;
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.createAuthenticatedSession(login, password,
        unauthenticatedSession));
  }

  // due to lack of test credentials can't implement it in a meaningful way
  // shouldProceedOnProperLoginPasswordPair()


  @Test
  void shouldFailWithoutAuthentication() {
    // given
    AuthenticatedSession authenticatedSession = new AuthenticatedSession("123", "abc");
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.getAccountsInfo(authenticatedSession));
  }
}
