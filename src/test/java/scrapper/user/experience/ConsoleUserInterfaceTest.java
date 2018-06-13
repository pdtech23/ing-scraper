package scrapper.user.experience;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scrapper.account.Account;
import scrapper.account.Money;

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
        String expected = ("Hello, we will try to get your account data here!\n(ps. we only " + "support ING Bank " +
                "here)") + "\n";

        // when
        testedUserInterface.displayWelcomeMessage();

        // then
        assertEquals(expected, OUT_CONTENT.toString());
    }

    @Test
    void shouldDisplayFailureMessage() {
        // given
        String expected = "Failed login attempt." + "\n";

        // when
        testedUserInterface.displayFailureMessage();

        // then
        assertEquals(expected, OUT_CONTENT.toString());
    }

    @Test
    void shouldBeAbleToReadUsersLogin() throws IOException {
        // given
        new Expectations() {{
            inputReaderMock.readLine();
            result = "janusz";
        }};

        // when
        String result = testedUserInterface.askUserForLogin();

        // then
        assertEquals("Type in Your login please:" + "\n", OUT_CONTENT.toString());
        assertEquals("janusz", result);
    }

    @Test
    void shouldBeAbleToReadUsersPassword() throws IOException {
        // given
        new Expectations() {{
            inputReaderMock.readLine();
            result = "j\n";
            times = 5;
        }};

        // when
        char[] result = testedUserInterface.askUserForNeededPasswordCharacters(new ArrayList<>(Arrays.asList(1,
                2, 3, 4, 5)));

        // then
        assertTrue(OUT_CONTENT.toString().contains("Give me character no. "));
        assertTrue(OUT_CONTENT.toString().contains(" of your password:"));
        assertTrue(Arrays.equals("jjjjj".toCharArray(), result));
    }

    @Test
    void shouldShouldReturnEmptyOnIncompletePassword() throws IOException {
        // given
        new Expectations() {{
            inputReaderMock.readLine();
            result = "j";
            times = 4;
            inputReaderMock.readLine();
            result = "";
        }};

        // when
        char[] result = testedUserInterface.askUserForNeededPasswordCharacters(new ArrayList<>(Arrays.asList(1,
                2, 3, 4, 5)));

        // then
        assertTrue(OUT_CONTENT.toString().contains("Give me character no. "));
        assertTrue(OUT_CONTENT.toString().contains(" of your password:"));
        assertEquals(0, result.length);
    }

    @Test
    void shouldDisplayAccounts() {
        // given
        List<Account> accounts = Arrays.asList(new Account("12", new Money(12.12, "$"), "test"), new Account("1337",
                new Money(99.99, "&"), "hehe"));

        // when
        testedUserInterface.printAccounts(accounts);

        // then
        assertTrue(OUT_CONTENT.toString().contains("test no. 12; available balance: 12.12 $"));
        assertTrue(OUT_CONTENT.toString().contains("hehe no. 1337; available balance: 99.99 &"));
    }
}
