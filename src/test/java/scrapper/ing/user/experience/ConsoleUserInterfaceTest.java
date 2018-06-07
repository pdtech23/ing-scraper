package scrapper.ing.user.experience;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scrapper.ing.account.IngAccountInfo;
import scrapper.ing.account.Money;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsoleUserInterfaceTest {

    private static final String SAMPLE_INPUT = "janusz\njanusz\njanusz\njanusz\njanusz\njanusz";
    private final ByteArrayInputStream sampleInput = new ByteArrayInputStream(SAMPLE_INPUT.getBytes());
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ConsoleUserInterface testedUserInterface;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(this.outContent));
        System.setIn(this.sampleInput);

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
    public void shouldBeAbleToReadUsersLogin() {
        // given

        // when
        String result = this.testedUserInterface.askUserForLogin();

        // then
        assertEquals(ConsoleUserInterface.ASK_FOR_LOGIN_MESSAGE + "\n", this.outContent.toString());
        assertEquals("janusz", result);
    }

    @Test
    public void shouldBeAbleToReadUsersPassword() {
        // given

        // when
        char[] result = this.testedUserInterface.askUserForNeededPasswordCharacters(new ArrayList<>(Arrays.asList(1,
                2, 3, 4, 5)));

        // then
        assertTrue(this.outContent.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_PREFIX));
        assertTrue(this.outContent.toString().contains(ConsoleUserInterface.PASSPHRASE_QUESTION_POSTFIX));
        assertTrue(Arrays.equals("jjjjj".toCharArray(), result));
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