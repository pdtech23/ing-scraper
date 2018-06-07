package scrapper.ing.account;

import java.util.Objects;

public class IngAccountInfo {

    private String accountNumber;
    private Money availableBalance;
    private String name;

    public IngAccountInfo(String accountNumber, Money availableBalance, String name) {
        this.accountNumber = accountNumber;
        this.availableBalance = availableBalance;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Account " + this.name + " no. " + this.accountNumber + "; available balance: " + this.availableBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        IngAccountInfo that = (IngAccountInfo) o;
        return Objects.equals(this.accountNumber, that.accountNumber) && Objects.equals(this.availableBalance, that
                .availableBalance) && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.accountNumber, this.availableBalance, this.name);
    }
}
