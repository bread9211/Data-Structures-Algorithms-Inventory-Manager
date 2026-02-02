public class Sell extends Transaction {
    private int price;
    private String customer;

    public Sell(Item item, int price, String customer) {
        super(item);
        this.price = price;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "SELL | " + item + " | price=" + price + " | customer=" + customer;
    }
}
