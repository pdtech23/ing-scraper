package scrapper.ing.account;

public class Money {

    private final int amount;
    private final String currency;

    public Money(double amount, String currency) {
        this.amount = (int) Math.floor(amount * 100);
        this.currency = currency;
    }

    @Override
    public String toString() {
        return ((int) Math.floor(this.amount / 100.0)) + "." + this.amount % 100 + " " + this.currency;
    }
}
