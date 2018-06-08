package scrapper.ing.user.experience;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.account.Money;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsoleUserInterfaceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Mocked
    private BufferedReader inputReaderMock;

    private ConsoleUserInterface testedUserInterface;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(this.outContent));

        this.testedUserInterface = new ConsoleUserInterface();
    }

    @Test
    public void shouldDisplayWelcomeMessage() {
        // given

        // when
        this.testedUserInterface.displayWelcomeMessage();

        // then
        assertEquals(ConsoleUserInterface.WELCOME_MESSAGE + "\n", this.outContent.toString());
    }

    @Test
    public void shouldDisplayFailureMessage() {
        // given

        // when
        this.testedUserInterface.displayFailureMessage();

        // then
        assertEquals(ConsoleUserInterface.FAILED_LOGIN_ATTEMPT_MESSAGE + "\n", this.outContent.toString());
    }

    @Test
    public void shouldBeAbleToReadUsersLogin() throws IOException {
        // given
        new Expectations() {{
            ConsoleUserInterfaceTest.this.inputReaderMock.readLine();
            this.result = "janusz";
        }};

        // when
        String result = this.testedUserInterface.askUserForLogin();

        // then
        assertEquals(ConsoleUserInterface.ASK_FOR_LOGIN_MESSAGE + "\n", this.outContent.toString());
        assertEquals("janusz", result);
    }

    @Test
    public void shouldBeAbleToReadUsersPassword() throws IOException {
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
        assertTrue(this.outContent.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_PREFIX));
        assertTrue(this.outContent.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_POSTFIX));
        assertTrue(Arrays.equals("jjjjj".toCharArray(), result));
    }

    @Test
    public void shouldShouldReturnEmptyOnIncompletePassword() throws IOException {
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
        assertTrue(this.outContent.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_PREFIX));
        assertTrue(this.outContent.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_POSTFIX));
        assertEquals(0, result.length);
    }

    @Test
    public void shouldDisplayAccounts() {
        // given
        List<IngAccountInfo> accounts = Arrays.asList(new IngAccountInfo("12", new Money(12.12, "$"), "test"), new
                IngAccountInfo("1337", new Money(99.99, "&"), "hehe"));

        // when
        this.testedUserInterface.printAccounts(accounts);

        // then
        assertTrue(this.outContent.toString().contains("test no. 12; available balance: 12.12 $"));
        assertTrue(this.outContent.toString().contains("hehe no. 1337; available balance: 99.99 &"));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setIn(System.in);
    }

}