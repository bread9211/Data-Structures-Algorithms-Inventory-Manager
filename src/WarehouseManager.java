import java.util.*;

public class WarehouseManager {
    private Map<String,Warehouse> warehouses;
    private List<ItemID> stockedIDs;
    private Warehouse current;
    private int nextSKU;

    public WarehouseManager() {
        warehouses = new HashMap<>();
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
        warehouses.put(name,w);
        current = w;
    }
    public void addWarehouse(Warehouse w) {
        warehouses.put(w.getName(),w);
        current = w;
    }

    public void addOwned(Item item){
        current.addItem(item,stockedIDs.get(item.getSKU()));
    }
    public void addNew(Item item, ItemID itemID){
        itemID.setSKU(nextSKU);
        item.setSKU(nextSKU);
        nextSKU++;
        stockedIDs.add(itemID);
        current.addItem(item,itemID);
    }

    public void tradeItems(String other, Set<Integer> currentInstances, Set<Integer> otherInstances){
        if(!warehouses.containsKey(other))
            return;
        Set<Item> curItems = new HashSet<>();
        Item item;
        for(int id : currentInstances){
            item = current.removeItemInstance(id);
            curItems.add(item);
            warehouses.get(other).addItem(item, stockedIDs.get(item.getSKU()));
        }
        Set<Item> othItems = new HashSet<>();
        for(int id : otherInstances){
            item = warehouses.get(other).removeItemInstance(id);
            othItems.add(item);
            current.addItem(item, stockedIDs.get(item.getSKU()));
        }
        Transaction trade = new Trade(current,warehouses.get(other),curItems,othItems);
        current.addTransaction(trade);
        warehouses.get(other).addTransaction(trade);
    }

    public ItemID getItemID(int SKU){
        return stockedIDs.get(SKU);
    }

    public void printAllWarehouses() {
        for (Warehouse w : warehouses.values()) {
            w.printInventory();
            System.out.println();
        }
    }
}
