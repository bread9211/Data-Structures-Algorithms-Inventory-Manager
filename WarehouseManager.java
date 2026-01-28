import java.util.*;

public class WarehouseManager {
    private List<Warehouse> warehouses;
    private List<ItemID> stockedIDs;
    private Warehouse current;
    private int nextSKU;

    public WarehouseManager() {
        warehouse = new ArrayList<>();
        stockedIDs = new ArrayList<>();
        current = null;
        nextSKU = 0;
    }

    public void addWarehouse(String name) {
        Warehouse w = new Warehouse(name);
        warehouses.add(w);
        current = w;
    }
    public void addOwned(Item item){
        current.add(item,stockedIDs.get(item.getSKU()));
    }
    public void addNew(Item item, ItemID itemID){
        itemID.setSKU(nextSKU);
        item.setSKU(nextSKU);
        nextSKU++;
        stockedIDs.add(itemID);
        current.add(item,itemID);
    }
    public void printAllWarehouses() {
        for (Warehouse w : warehouse) {
            w.printInventory();
            System.out.println();
        }
    }
}
