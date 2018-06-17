package challenge.model;

import java.math.BigDecimal;

public class Money {

  private final BigDecimal amount;
  private final String currency;

  public Money(String amount, String currency) {
    this.amount = new BigDecimal(amount);
    this.currency = currency;
  }

  @Override
  public String toString() {
    return (amount.toPlainString() + " " + currency);
  }
}
