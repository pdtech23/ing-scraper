package scrapper.ing.account;

import org.junit.Test;
import scrapper.ing.TestHelper;

import static org.junit.Assert.assertEquals;

public class IngAccountInfoTest {

    @Test
    public void shouldGenerateProperString() {
        // given
        IngAccountInfo info = TestHelper.SAMPLE_ACCOUNT_INFO;

        // when
        String result = info.toString();

        // then
        assertEquals("Account super acc no. 1234; available balance: 12.34 PLN", result);
    }
}