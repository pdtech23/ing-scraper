package challenge.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

  @Test
  void shouldGenerateProperString() {
    // given
    Money availableBalance = new Money("1234.12", "PLN");
    String accountName = "Name";
    String accountNumber = "1337 4568 9292 9212";
    Account info = new Account(accountNumber, availableBalance, accountName);
    // when
    String result = info.toString();
    // then
    assertTrue(result.contains(accountNumber.substring(0, 4)) && result.contains(availableBalance.toString()) && result.contains(accountName));
  }
}
