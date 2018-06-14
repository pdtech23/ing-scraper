package challange.account;

import challange.ing.TestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {

  @Test
  void shouldGenerateProperString() {
    // given
    Account info = TestHelper.SAMPLE_ACCOUNT_INFO;
    // when
    String result = info.toString();
    // then
    assertEquals("Account super acc no. 1337; available balance: 12.34 PLN", result);
  }
}
