package scrapper.ing.account;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoneyTest {

    @Test
    public void shouldGenerateProperString() {
        // given
        Money money = new Money(12.3456, "ZIMBABWE DOLLARS $$$");

        // when
        String result = money.toString();

        // then
        assertEquals("12.34 ZIMBABWE DOLLARS $$$", result);
    }
}