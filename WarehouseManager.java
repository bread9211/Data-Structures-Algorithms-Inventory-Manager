import java.util.*;
import java.util.Date;
import java.util.LocalDate;

public class WarehouseManager {
    private Map<String,Warehouse> warehouses;
    private List<ItemID> stockedIDs;
    private Warehouse current;
    private int nextSKU;

    public WarehouseManager() {
        warehouse = new ArrayList<>();
        stockedIDs = new ArrayList<>();
        current = null;
        nextSKU = 0;
    }

    public Warehouse getCurrent(){
        return current;
    }
    public void setCurrent(String name){
        current = warehouses.get(name);
    }
    public Warehouse getWarehouse(String name){
        return warehouses.get(name);
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

    public void tradeItems(Warehouse other, Set<Integer> currentInstances, Set<Integer> otherInstances){
        Set<Item> curItems = new HastSet<>();
        for(int id : currentInstances)
            curItems.add(current.removeItemInstance(id));
        Set<Item> othItems = new HastSet<>();
        for(int id : otherInstances)
            othItems.add(other.removeItemInstance(id));

        Transaction trade = new Trade(current,other,curItems,othItems);
        current.addTransaction(trade);
        other.addTransaction(trade);
    }

    public itemID getItemID(int SKU){
        return stockedIDs.get(SKU);
    }

    public void printAllWarehouses() {
        for (Warehouse w : warehouse) {
            w.printInventory();
            System.out.println();
        }
    }
}
