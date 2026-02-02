import java.util.*;

public class WarehouseManager {
    private Set<Integer> stockedSKUs;
    private List<Transaction> transactionLog;
    private List<Warehouse> warehouses;

    public WarehouseManager() {
        stockedSKUs = new HashSet<>();
        transactionLog = new ArrayList<>();
        warehouses = new ArrayList<>();
    }

    public void registerSKU(int sku) {
        stockedSKUs.add(sku);
    }

    public void addWarehouse(Warehouse w) {
        warehouses.add(w);
    }

    public void logTransaction(Transaction t) {
        transactionLog.add(t);
    }

    public void printTransactions() {
        for (Transaction t : transactionLog) {
            System.out.println(t);
        }
    }
}
