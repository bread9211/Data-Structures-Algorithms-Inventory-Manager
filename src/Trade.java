import java.util.Set;

public class Trade extends Transaction {
    private Set<Item> sent;
    private Set<Item> received;
    private Warehouse from;
    private Warehouse to;

    public Trade(Warehouse from, Warehouse to,
                 Set<Item> sent, Set<Item> received) {
        super(null); // Trade has multiple items
        this.from = from;
        this.to = to;
        this.sent = sent;
        this.received = received;
    }

    @Override
    public String toString() {
        return "TRADE | " + from.getName() + " → " + to.getName() +
               " | sent=" + sent.size() +
               " | received=" + received.size();
    }
}
