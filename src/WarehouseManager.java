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
        
        Warehouse otherWarehouse = warehouses.get(other);
        Set<Item> curItems = new HashSet<>();
        Item item;
        
        // Transfer items from current warehouse to other warehouse
        for(int id : currentInstances){
            item = current.removeItemInstance(id);
            if (item != null) {
                curItems.add(item);
                // Get ItemID from current warehouse's stockedIDs
                ItemID itemID = getItemIDFromWarehouse(current, item.getSKU());
                if (itemID != null) {
                    otherWarehouse.addItem(item, itemID);
                } else {
                    otherWarehouse.addItem(item, item.getSKU());
                }
            }
        }
        
        Set<Item> othItems = new HashSet<>();
        // Transfer items from other warehouse to current warehouse
        for(int id : otherInstances){
            item = otherWarehouse.removeItemInstance(id);
            if (item != null) {
                othItems.add(item);
                // Get ItemID from other warehouse's stockedIDs
                ItemID itemID = getItemIDFromWarehouse(otherWarehouse, item.getSKU());
                if (itemID != null) {
                    current.addItem(item, itemID);
                } else {
                    current.addItem(item, item.getSKU());
                }
            }
        }
        
        Transaction trade = new Trade(current, otherWarehouse, curItems, othItems);
        current.addTransaction(trade);
        otherWarehouse.addTransaction(trade);
    }
    
    private ItemID getItemIDFromWarehouse(Warehouse warehouse, int sku) {
        try {
            java.lang.reflect.Field stockedIDsField = Warehouse.class.getDeclaredField("stockedIDs");
            stockedIDsField.setAccessible(true);
            java.util.Map<Integer, LocalItemID> stockedIDs = (java.util.Map<Integer, LocalItemID>) stockedIDsField.get(warehouse);
            
            if (stockedIDs.containsKey(sku)) {
                return stockedIDs.get(sku).getReference();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemID getItemID(int SKU){
        return stockedIDs.get(SKU);
    }

    public List<String> getAllWarehouseNames() {
        return new ArrayList<>(warehouses.keySet());
    }

    public void printAllWarehouses() {
        for (Warehouse w : warehouses.values()) {
            w.printInventory();
            System.out.println();
        }
    }
}
