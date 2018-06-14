package challange.ing.scraper;

import challange.ing.TestHelper;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IngScraperTest {

  private final IIngScraper testedService = new IngScraper();

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
    AuthenticatedSession authenticatedSession = new AuthenticatedSession("123", "abc");
    // when & then
    Assertions.assertThrows(RuntimeException.class, () -> testedService.fetchAccounts(authenticatedSession));
  }
}
