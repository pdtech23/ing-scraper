package scrapper.ing.user.experience;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.account.Money;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ConsoleUserInterfaceTest {

    private static final ByteArrayOutputStream OUT_CONTENT = new ByteArrayOutputStream();

    @Mocked
    private BufferedReader inputReaderMock;

    private ConsoleUserInterface testedUserInterface = new ConsoleUserInterface();

    @BeforeAll
    static void setUp() {
        System.setOut(new PrintStream(OUT_CONTENT));
    }

    @AfterAll
    static void restoreStreams() {
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @AfterEach
    void clearOutStream() {
        OUT_CONTENT.reset();
    }

    @Test
    void shouldDisplayWelcomeMessage() {
        // given
        String expected = ConsoleUserInterface.WELCOME_MESSAGE + "\n";

        // when
        this.testedUserInterface.displayWelcomeMessage();

        // then
        assertEquals(expected, OUT_CONTENT.toString());
    }

    @Test
    void shouldDisplayFailureMessage() {
        // given
        String expected = ConsoleUserInterface.FAILED_LOGIN_ATTEMPT_MESSAGE + "\n";

        // when
        this.testedUserInterface.displayFailureMessage();

        // then
        assertEquals(expected, OUT_CONTENT.toString());
    }

    @Test
    void shouldBeAbleToReadUsersLogin() throws IOException {
        // given
        new Expectations() {{
            ConsoleUserInterfaceTest.this.inputReaderMock.readLine();
            this.result = "janusz";
        }};

        // when
        String result = this.testedUserInterface.askUserForLogin();

        // then
        assertEquals(ConsoleUserInterface.ASK_FOR_LOGIN_MESSAGE + "\n", OUT_CONTENT.toString());
        assertEquals("janusz", result);
    }

    @Test
    void shouldBeAbleToReadUsersPassword() throws IOException {
        // given
        new Expectations() {{
            ConsoleUserInterfaceTest.this.inputReaderMock.readLine();
            this.result = "j\n";
            this.times = 5;
        }};

        // when
        char[] result = this.testedUserInterface.askUserForNeededPasswordCharacters(new ArrayList<>(Arrays.asList(1,
                2, 3, 4, 5)));

        // then
        assertTrue(OUT_CONTENT.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_PREFIX));
        assertTrue(OUT_CONTENT.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_POSTFIX));
        assertTrue(Arrays.equals("jjjjj".toCharArray(), result));
    }

    @Test
    void shouldShouldReturnEmptyOnIncompletePassword() throws IOException {
        // given
        new Expectations() {{
            ConsoleUserInterfaceTest.this.inputReaderMock.readLine();
            this.result = "j";
            this.times = 4;
            ConsoleUserInterfaceTest.this.inputReaderMock.readLine();
            this.result = "";
        }};

        // when
        char[] result = this.testedUserInterface.askUserForNeededPasswordCharacters(new ArrayList<>(Arrays.asList(1,
                2, 3, 4, 5)));

        // then
        assertTrue(OUT_CONTENT.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_PREFIX));
        assertTrue(OUT_CONTENT.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_POSTFIX));
        assertEquals(0, result.length);
    }

    @Test
    void shouldDisplayAccounts() {
        // given
        List<IngAccountInfo> accounts = Arrays.asList(new IngAccountInfo("12", new Money(12.12, "$"), "test"), new
                IngAccountInfo("1337", new Money(99.99, "&"), "hehe"));

        // when
        this.testedUserInterface.printAccounts(accounts);

        // then
        assertTrue(OUT_CONTENT.toString().contains("test no. 12; available balance: 12.12 $"));
        assertTrue(OUT_CONTENT.toString().contains("hehe no. 1337; available balance: 99.99 &"));
    }

}