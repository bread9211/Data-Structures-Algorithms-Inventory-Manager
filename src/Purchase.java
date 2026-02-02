public class Purchase extends Transaction {
    private int cost;
    private String supplier;

    public Purchase(Item item, int cost, String supplier) {
        super(item);
        this.cost = cost;
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "PURCHASE | " + item + " | cost=" + cost + " | supplier=" + supplier;
    }
}
