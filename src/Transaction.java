import java.time.LocalDate;

public abstract class Transaction {
    protected LocalDate date;
    protected Item item;

    public Transaction(Item item) {
        this.item = item;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public Item getItem() {
        return item;
    }
}
