package scrapper.ing;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.client.ConnectionProxyService;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.security.SessionData;
import scrapper.ing.user.experience.ConsoleUserInterface;

import java.util.Arrays;
import java.util.List;


public class IngClientDataExtractionServiceTest {

    public static final String SAMPLE_UNAUTHENTICATED_SESSION_ID = "unauthenticatedSessionId";
    private static final char[] SAMPLE_PASSWORD = {'a', 'b', 'c', 'd', 'e'};
    private static final List<IngAccountInfo> SAMPLE_ACCOUNTS_LIST = Arrays.asList(TestHelper.SAMPLE_ACCOUNT_INFO,
            TestHelper.SAMPLE_ACCOUNT_INFO);
    private static final String SAMPLE_LOGIN = "janusz";
    private static final SessionData SAMPLE_SESSION_DATA = new SessionData("token", "sessionId");
    private static final List<Integer> SAMPLE_CHARACTERS_POSITIONS = Arrays.asList(1, 2, 3, 4, 5);
    private static final String SAMPLE_PASSWORD_HASH = PasswordBehaviorHandler.createPasswordHash(TestHelper
            .SAMPLE_PASSWORD_METADATA, SAMPLE_PASSWORD);
    private AccountDataExtractionService testedService;

    @Mocked
    private ConsoleUserInterface userInterface;
    @Mocked
    private ConnectionProxyService dataExtractor;

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
        this.testedService.downloadAccountDataWithUserInteraction();

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
            this.result = SessionData.EMPTY;
            IngClientDataExtractionServiceTest.this.userInterface.displayFailureMessage();
        }};

        // when
        this.testedService.downloadAccountDataWithUserInteraction();

        // then
        // no exception is thrown and expectations are met
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

        // when
        this.testedService.downloadAccountDataWithUserInteraction();

        // thenS
        // no exception is thrown and expectations are met
    }

    @Test
    public void shouldStopWhenUnableToConnect() {
        // given
        this.givenSomeLogin();
        new Expectations() {{
            IngClientDataExtractionServiceTest.this.dataExtractor.doFirstLogInStep(this.anyString);
            this.result = PasswordMetadata.EMPTY;
        }};

        // when
        this.testedService.downloadAccountDataWithUserInteraction();

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

        // when
        this.testedService.downloadAccountDataWithUserInteraction();

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
            IngClientDataExtractionServiceTest.this.dataExtractor.doFirstLogInStep(this.anyString);
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