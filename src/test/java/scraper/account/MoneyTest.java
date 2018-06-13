package scraper.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTest {

  @Test
  void shouldGenerateProperString() {
    // given
    Money money = new Money("12.3456", "ZIMBABWE DOLLARS $$$");
    // when
    String result = money.toString();
    // then
    assertEquals("12.3456 ZIMBABWE DOLLARS $$$", result);
  }
}
