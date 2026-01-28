import java.util.*;

public class WarehouseManager {
    private List<Warehouse> warehouse;

    public WarehouseManager() {
        warehouse = new ArrayList<>();
    }

    public void addWarehouse(Warehouse w) {
        warehouse.add(w);
    }

    public void printAllWarehouses() {
        for (Warehouse w : warehouse) {
            w.printInventory();
            System.out.println();
        }
    }
}
