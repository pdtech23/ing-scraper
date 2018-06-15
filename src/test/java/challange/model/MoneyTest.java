package challange.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

  @Test
  void shouldGenerateProperString() {
    // given
    String amountMajor = "12";
    String amountMinor = "345";
    String currency = "ZIMBABWE DOLLARS $$$";
    Money money = new Money(amountMajor + "." + amountMinor, currency);
    // when
    String result = money.toString();
    // then
    assertTrue(result.contains(amountMajor) && result.contains(amountMinor) && result.contains(currency));
  }
}
