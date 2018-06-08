package scrapper.ing.account;

public class IngAccountInfo {

    private final String accountNumber;
    private final Money availableBalance;
    private final String name;

    public IngAccountInfo(String accountNumber, Money availableBalance, String name) {
        this.accountNumber = accountNumber;
        this.availableBalance = availableBalance;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Account " + this.name + " no. " + this.accountNumber + "; available balance: " + this.availableBalance;
    }
}
