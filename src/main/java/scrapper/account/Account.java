package scrapper.account;

public class Account {

  private final String accountNumber;
  private final Money availableBalance;
  private final String name;

  public Account(String accountNumber, Money availableBalance, String name) {
    this.accountNumber = accountNumber;
    this.availableBalance = availableBalance;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Account " + name + " no. " + accountNumber + "; available balance: " + availableBalance;
  }
}
