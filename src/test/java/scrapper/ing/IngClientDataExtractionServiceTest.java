package scrapper.ing;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrapper.account.Account;
import scrapper.ing.client.DownloadDataClient;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.user.experience.ConsoleUserInterface;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class IngClientDataExtractionServiceTest {

    private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
    private static final List<Account> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO,
            TestHelper.SAMPLE_ACCOUNT_INFO);
    private static final String SAMPLE_LOGIN = "janusz";
    private static final AuthenticatedSession SAMPLE_SESSION_DATA = new AuthenticatedSession("token", "sessionId");
    private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);

    private PresentAccounts testedService;

    @Mocked
    private ConsoleUserInterface userInterface;
    @Mocked
    private DownloadDataClient dataExtractor;

    @BeforeEach
    void setUp() {
        testedService = new PresentAccounts(userInterface, dataExtractor);
    }

    @Test
    void shouldDownloadAccountDataWithUserInteraction() {
        // given
        givenSomeLogin();
        givenSuccessfulConnection();
        givenSomePassword();
        givenPasswordAndLoginBeingCorrect();
        new Expectations() {{
            dataExtractor.getAccountsInfo(SAMPLE_SESSION_DATA);
            result = SAMPLE_ACCOUNTS_LIST;
            userInterface.printAccounts(SAMPLE_ACCOUNTS_LIST);
        }};

        // when
        testedService.displayAccountDataWithUserInteraction();

        // then
        // no exception is thrown and expectations are met
    }

    @Test
    void shouldDisplayFailureMessageWhenUnableToCreateSession() {
        // given
        givenSomeLogin();
        givenSuccessfulConnection();
        givenSomePassword();
        new Expectations() {{
            dataExtractor.createAuthenticatedSession(SAMPLE_LOGIN, SAMPLE_PASSWORD, TestHelper
                    .SAMPLE_PASSWORD_METADATA);
            result = Optional.empty();
        }};

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.displayAccountDataWithUserInteraction());
    }

    @Test
    void shouldStopWorkingOnInvalidPassword() {
        // given
        givenSomeLogin();
        givenSuccessfulConnection();
        new Expectations() {{
            userInterface.askUserForNeededPasswordCharacters(SAMPLE_CHARACTERS_POSITIONS);
            result = new char[0];
        }};

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.displayAccountDataWithUserInteraction());
    }

    @Test
    void shouldStopWhenUnableToConnect() {
        // given
        givenSomeLogin();
        new Expectations() {{
            dataExtractor.createUnauthenticatedSession(anyString);
            result = Optional.empty();
        }};

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.displayAccountDataWithUserInteraction());

    }

    @Test
    void shouldStopWorkingOnInvalidLogin() {
        // given
        new Expectations() {{
            userInterface.displayWelcomeMessage();
            userInterface.askUserForLogin();
            result = "";
        }};

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> testedService.displayAccountDataWithUserInteraction());
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
            dataExtractor.createUnauthenticatedSession(anyString);
            result = Optional.of(TestHelper.SAMPLE_PASSWORD_METADATA);
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
            dataExtractor.createAuthenticatedSession(SAMPLE_LOGIN, SAMPLE_PASSWORD, TestHelper
                    .SAMPLE_PASSWORD_METADATA);
            result = Optional.of(SAMPLE_SESSION_DATA);
        }};
    }
}
