package scrapper.ing;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.DataDownloaderService;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.UnauthenticatedSession;
import scrapper.ing.user.experience.ConsoleUserInterface;

import java.util.Arrays;
import java.util.List;


public class IngClientDataExtractionServiceTest {

    private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
    private static final List<IngAccountInfo> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO,
            TestHelper.SAMPLE_ACCOUNT_INFO);
    private static final String SAMPLE_LOGIN = "janusz";
    private static final AuthenticatedSession SAMPLE_SESSION_DATA = new AuthenticatedSession("token", "sessionId");
    private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);

    private AccountDataExtractionService testedService;

    @Mocked
    private ConsoleUserInterface userInterface;
    @Mocked
    private DataDownloaderService dataExtractor;

    @Before
    public void setUp() {
        this.testedService = new AccountDataExtractionService(this.userInterface, this.dataExtractor);
    }

    @Test
    public void shouldDownloadAccountDataWithUserInteraction() {
        // given
        this.givenSomeLogin();
        this.givenSuccessfulConnection();
        this.givenSomePassword();
        this.givenPasswordAndLoginBeingCorrect();
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.dataExtractor.getAccountsInfo(SAMPLE_SESSION_DATA);
            this.result = SAMPLE_ACCOUNTS_LIST;
            IngClientDataExtractionServiceTest.this.userInterface.printAccounts(SAMPLE_ACCOUNTS_LIST);
        }};

        // when
        this.testedService.displayAccountDataWithUserInteraction();

        // then
        // no exception is thrown and expectations are met
    }

    @Test
    public void shouldDisplayFailureMessageWhenUnableToCreateSession() {
        // given
        this.givenSomeLogin();
        this.givenSuccessfulConnection();
        this.givenSomePassword();
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.dataExtractor.createAuthenticatedSession(SAMPLE_LOGIN,
                    SAMPLE_PASSWORD, TestHelper.SAMPLE_PASSWORD_METADATA);

            this.result = AuthenticatedSession.EMPTY;
        }};
        this.expectationOfFailureMessage();

        // when
        this.testedService.displayAccountDataWithUserInteraction();

        // then
        // no exception is thrown and expectations are met
    }

    private void expectationOfFailureMessage() {
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.userInterface.displayFailureMessage();
        }};
    }

    @Test
    public void shouldStopWorkingOnInvalidPassword() {
        // given
        this.givenSomeLogin();
        this.givenSuccessfulConnection();

        new Expectations() {{
            IngClientDataExtractionServiceTest.this.userInterface.askUserForNeededPasswordCharacters
                    (SAMPLE_CHARACTERS_POSITIONS);
            this.result = new char[0];
        }};
        this.expectationOfFailureMessage();

        // when
        this.testedService.displayAccountDataWithUserInteraction();

        // thenS
        // no exception is thrown and expectations are met
    }

    @Test
    public void shouldStopWhenUnableToConnect() {
        // given
        this.givenSomeLogin();
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.dataExtractor.createUnauthenticatedSession(this.anyString);
            this.result = UnauthenticatedSession.EMPTY;
        }};
        this.expectationOfFailureMessage();

        // when
        this.testedService.displayAccountDataWithUserInteraction();

        // then
        // no exception is thrown and expectations are met
    }

    @Test
    public void shouldStopWorkingOnInvalidLogin() {
        // given
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.userInterface.displayWelcomeMessage();
            IngClientDataExtractionServiceTest.this.userInterface.askUserForLogin();
            this.result = "";
        }};
        this.expectationOfFailureMessage();

        // when
        this.testedService.displayAccountDataWithUserInteraction();

        // then
        // no exception is thrown and expectations are met
    }

    private void givenSomeLogin() {
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.userInterface.displayWelcomeMessage();
            IngClientDataExtractionServiceTest.this.userInterface.askUserForLogin();
            this.result = SAMPLE_LOGIN;
        }};
    }

    private void givenSuccessfulConnection() {
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.dataExtractor.createUnauthenticatedSession(this.anyString);
            this.result = TestHelper.SAMPLE_PASSWORD_METADATA;
        }};
    }

    private void givenSomePassword() {
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.userInterface.askUserForNeededPasswordCharacters
                    (SAMPLE_CHARACTERS_POSITIONS);
            this.result = SAMPLE_PASSWORD;
        }};
    }

    private void givenPasswordAndLoginBeingCorrect() {
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.dataExtractor.createAuthenticatedSession(SAMPLE_LOGIN,
                    SAMPLE_PASSWORD, TestHelper.SAMPLE_PASSWORD_METADATA);
            this.result = SAMPLE_SESSION_DATA;
        }};
    }
}