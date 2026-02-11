import java.util.*;
import java.sql.Date;
import java.time.LocalDate;

import javax.swing.event.InternalFrameEvent;

public class Warehouse {

    private String name;
    private Map<Integer, Set<Integer>> itemsBySKU;
    private Map<Integer,LocalItemID> stockedIDs;
    private Map<LocalDate, Set<Integer>> itemsByExpiration;
    private List<Item> itemsByChrono;
    private List<Transaction> transactions;
    private int nextInstanceID;

    public Warehouse(String name) {
        this.name = name;
        itemsBySKU = new HashMap<>();
        stockedIDs = new HashMap<>();
        itemsByExpiration = new TreeMap<>();
        itemsByChrono = new ArrayList<>();
        transactions = new ArrayList<>();
        nextInstanceID = 0;
    }
    public String getName() {
        return name;
    }

    /* =========================
       ADD / REMOVE
       ========================= */

    public int addItem(Item item,int sku){
        itemsByChrono.add(item);
        item.setID(nextInstanceID);

        itemsBySKU.putIfAbsent(sku, new HashSet<>());
        itemsBySKU.get(sku).add(nextInstanceID);

        // Ensure stockedIDs is updated even when using the int sku version
        if (!stockedIDs.containsKey(sku)) {
            // Create a default ItemID reference for tracking
            ItemID defaultItem = new ItemID("Item " + sku, sku, 0, 0, new String[]{});
            stockedIDs.put(sku, new LocalItemID(defaultItem, item.getStock()));
        } else {
            // Update the stock count in the existing entry
            stockedIDs.get(sku).addStock(item.getStock());
        }

        if (item.isPerishable()){
            if(!itemsByExpiration.containsKey(item.getExpr()))
                itemsByExpiration.put(item.getExpr(), new HashSet<Integer>());
            itemsByExpiration.get(item.getExpr()).add(nextInstanceID);
        }
        nextInstanceID++;
        return nextInstanceID-1;
    }

    public Item getItem(int instanceID){
        return itemsByChrono.get(instanceID);
    }

    public int addItem(Item item,ItemID itemID) {
        int sku = item.getSKU();
        if(!itemsBySKU.containsKey(sku))
            stockedIDs.put(sku,new LocalItemID(itemID,0));
        return addItem(item,sku);
    }

    public Item removeItemInstance(int instanceID) {
        Item item = itemsByChrono.get(instanceID);
        if (item == null) return null;

        int sku = item.getSKU();
        itemsBySKU.get(sku).remove(instanceID);
        stockedIDs.get(sku).addStock(-1*item.getStock());
        if (itemsBySKU.get(sku).isEmpty()) {
            stockedIDs.remove(sku);
            itemsBySKU.remove(sku);
        }

        if(item.isPerishable())
            itemsByExpiration.get(item.getExpr()).remove(instanceID);
            if(itemsByExpiration.get(item.getExpr()).isEmpty())
                itemsByExpiration.remove(item.getExpr());
        itemsByChrono.set(instanceID,null);
        return item;
    }

    public Item removeItemQuantity(int instanceID, int amount) {
        Item item = itemsByChrono.get(instanceID);
        if(amount > item.getStock())
            return null;
        else if(amount == item.getStock())
            return removeItemInstance(instanceID);
        Item copy = new Item(item.getSKU(),amount);
        item.removeItem(amount);
        stockedIDs.get(item.getSKU()).addStock(-1*amount);
        return copy;
    }

    public List<Item> removeSKUQuantity(int SKU, int amount) {
        LocalItemID itemID = stockedIDs.get(SKU);
        if(amount > itemID.getStock())
            return null;

        List<Item> allItems = searchBySKU(SKU);
        List<Item> returnItems = new ArrayList<Item>();
        Iterator<Item> it = allItems.iterator();
        Item item;
        int stock;
        while(amount > 0){
            item = it.next();
            stock = item.getStock();
            if(stock > amount){
                returnItems.add(removeItemQuantity(item.getID(),amount));
                amount = 0;
            }else{
                amount -= stock;
                returnItems.add(removeItemInstance(item.getID()));
            }
        }
        itemID.addStock(-1*amount);
        return returnItems;
    }

    public boolean splitInstance(int instanceID, int amount){
        Item item = itemsByChrono.get(instanceID);
        if(item.getStock() > amount){
            Item copy = removeItemQuantity(instanceID, amount);
            if(copy != null)
                addItem(copy, copy.getSKU());
            return true;
        }
        return false;
    }

    public List<Item> clearExpr(){
        LocalDate date;
        List<Item> result = new ArrayList<>();
        Set<LocalDate> dates = itemsByExpiration.keySet();
        Iterator<LocalDate> it = dates.iterator();
        boolean passedDate = false;
        while(it.hasNext() && !passedDate){
            date = it.next();
            if(date.isBefore(LocalDate.now())){
                Set<Integer> ids = itemsByExpiration.get(date);
                for(int id: ids)
                result.add(removeItemInstance(id));
            }else
                passedDate = true;
        }
        return result;
    }

    /* =========================
       SEARCH
       ========================= */

    public List<Item> searchBySKU(int sku) {
        List<Item> result = new ArrayList<>();
        if (!itemsBySKU.containsKey(sku)) return result;

        for (int id : itemsBySKU.get(sku)) {
            result.add(itemsByChrono.get(id));
        }
        return result;
    }
    
    public List<Item> searchByID(int sku) {
        return searchBySKU(sku);
    }
    
    public Map<Integer, List<Item>> getItemsByID() {
        Map<Integer, List<Item>> result = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> entry : itemsBySKU.entrySet()) {
            List<Item> items = new ArrayList<>();
            for (int instanceID : entry.getValue()) {
                items.add(itemsByChrono.get(instanceID));
            }
            result.put(entry.getKey(), items);
        }
        return result;
    }

    public List<Item> searchByName(String name) {
        List<Item> result = new ArrayList<>();
        for (int sku : stockedIDs.keySet()){
            if (stockedIDs.get(sku).getReference().getName().equals(name)) {
                for(int id : itemsBySKU.get(sku))
                    result.add(itemsByChrono.get(id));
            }
        }
        return result;
    }

    public List<Item> searchByKeyword(String keyword) {
        List<Item> result = new ArrayList<>();
        for (int sku : stockedIDs.keySet()){
            if (stockedIDs.get(sku).getReference().checkKeyword(keyword)) {
                for(int id : itemsBySKU.get(sku))
                    result.add(itemsByChrono.get(id));
            }
        }
        return result;
    }

    public String getItemName(int sku) {
        if (stockedIDs.containsKey(sku)) {
            return stockedIDs.get(sku).getReference().getName();
        }
        return "Item " + sku;  // Fallback if name not found
    }

    public ItemID getItemIDForSKU(int sku) {
        if (stockedIDs.containsKey(sku)) {
            return stockedIDs.get(sku).getReference();
        }
        return null;
    }

    public String[] getItemKeywords(int sku) {
        if (stockedIDs.containsKey(sku)) {
            ItemID itemID = stockedIDs.get(sku).getReference();
            // Use reflection to access keywords since they're private
            try {
                java.lang.reflect.Field field = ItemID.class.getDeclaredField("keywords");
                field.setAccessible(true);
                return (String[]) field.get(itemID);
            } catch (Exception e) {
                return new String[]{};
            }
        }
        return new String[]{};
    }

    /* =========================
       SORTING
       ========================= */

    public List<Item> sortByName() {
        List<Item> list = new ArrayList<>(itemsByChrono);
        list.sort((a, b) ->
            stockedIDs.get(a.getSKU()).getReference().getName().compareToIgnoreCase(
            stockedIDs.get(b.getSKU()).getReference().getName()));
        return list;
    }

    public List<Item> sortBySKU() {
        List<Item> list = new ArrayList<>(itemsByChrono);
        list.sort((a, b) ->
            Integer.compare(a.getSKU(), b.getSKU()));
        return list;
    }

    public List<Item> sortByExpiration() {
        List<Item> result = new ArrayList<>();
        Set<LocalDate> dates = itemsByExpiration.keySet();
        for (LocalDate date : dates){
            Set<Integer> ids = itemsByExpiration.get(date);
            for(int id: ids)
                result.add(itemsByChrono.get(id));
        }
        return result;
    }

    /* =========================
       TRANSACTIONS
       ========================= */

    public void addTransaction(Trade transaction){
        transactions.add(transaction);
    }
    public void addTransaction(Purchase transaction){
        transactions.add(transaction);
    }
    public void addTransaction(Sell transaction){
        transactions.add(transaction);
        Item item = transaction.getItem();
        stockedIDs.get(item.getSKU()).sell(item.getStock());
    }
    public List<Transaction> getTransactions(){
        return transactions;
    }


    /* =========================
       DISPLAY
       ========================= */

    public void printInventory() {
        System.out.println("Warehouse: " + name);
        for (Item item : itemsByChrono) {
            if(item != null && item.getStock() != 0)
                System.out.println("  " + item);
        }
    }
}

/*
                                            ..........    ..                                        
                                      .:=*#@@@@@@@@@@@@@@@@@%#+-:..                                 
                                   ..=@@@@@@@@@@@@@@@@@@@@@@@@@@@@#=..                              
                                  .+@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%-..                           
                                 :#@@@@@@@%##################%@@@@@@@@@@=.                          
                                -%@@@@@@%#######################%@@@@@@@@#-                         
                               -%@@@@@%###########################%@@@@@@@@+.                       
                              :#@@@@@%##############################%@@@@@@@*:                      
                             .+@@@@@%############%%%@@@@@@@@@@@@@@@@@@@@@@@@@#:.                    
                             :#@@@@@%#########%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%=.                   
                             =%@@@@%#########@@@@@@@@%#*+=-------------+*%@@@@@@+.                  
                            .+@@@@@%########%@@@@@@+--------:...........::=#@@@@@*:.                
                            -%@@@@@%########@@@@@%*=-------:...............:=%@@@@#-                
                         .-#@@@@@@%%########@@@@@#**--------:..............:--#@@@@#:               
                  .+#%%@@@@@@@@@@@%%########@@@@@****=--------------::--------=%@@@@=.              
                :*@@@@@@@@@@@@@@@@%%########@@@@@******=---------------------=*%@@@@*:              
               :*@@@@@@@@%%@@@@@@@%%########@@@@@#*******+=-----------===++****%@@@@*:              
              .-@@@@@@%#####%@@@@%%%########%@@@@%************++**************#@@@@@-.              
              .-@@@@@%######%@@@@%%%%########@@@@@@#*************************#@@@@@#:               
              .-@@@@@%######@@@@@%%%%########%@@@@@@%********************##%@@@@@@@-.               
              .-@@@@@%######@@@@@%%%%##########@@@@@@@@#*************#%@@@@@@@@@@@=.                
              .-@@@@@%%%%%%%@@@@@%%%%###########@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%-.                 
              .-@@@@@%%%%%%%@@@@@%%%%%############%@@@@@@@@@@@@@@@@@@@@@%%%%@@@@#:                  
              .-@@@@@%%%%%%%@@@@@%%%%%###############%%%@@@@@@@@%%%%%######%@@@@%-                  
              .=@@@@@%%%%%%%@@@@@%%%%%%####################################%@@@@@=.                 
              .=@@@@@%%%%%%%@@@@@%%%%%%####################################%@@@@@+.                 
              .+@@@@@%%%%%%%@@@@@%%%%%%%###################################%@@@@@#:                 
              .+@@@@@%%%%%%%@@@@@%%%%%%%##################################%%@@@@@#:                 
              :*@@@@%%%%%%%%@@@@@%%%%%%%%#################################%%@@@@@#-                 
              :*@@@@%%%%%%%%@@@@@%%%%%%%%################################%%%@@@@@#-                 
              :*@@@@@%%%%%%%@@@@@%%%%%%%%%###############################%%%@@@@@#:                 
              :*@@@@@%%%%%%%@@@@@%%%%%%%%%%%###########################%%%%%@@@@@+.                 
              .*@@@@@%%%%%%%@@@@@%%%%%%%%%%%%%########################%%%%%@@@@@@=.                 
              .=@@@@@%%%%%%%@@@@@%%%%%%%%%%%%%%%%%################%%%%%%%%%@@@@@@-                  
              .:@@@@@%%%%%%%@@@@@%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@@@@#:                  
               :*@@@@@%%%%%%@@@@@%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@@@@+.                  
               .-@@@@@@%%%%%@@@@@%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@@@@@=                   
                .+@@@@@@@@@@@@@@@@%%%%%%%%%%%%%%%%@@@@@@%%%%%%%%%%%%%%%%%%@@@@@#-                   
                 .-#@@@@@@@@@@@@@@%%%%%%%%%%%%%%%@@@@@@@@@@@@@@@@@@%%%%%%@@@@@@+.                   
                   .:-*%@@@@@@@@@@%%%%%%%%%%%%%%@@@@@@@@@@@@@@@@@%%%%%%%%@@@@@%=                    
                        ...:*@@@@@%%%%%%%%%%%%%%@@@@@%--*@@@@@%%%%%%%%%%%@@@@@#-                    
                            =%@@@@%%%%%%%%%%%%%%@@@@@@..-@@@@@%%%%%%%%%%%@@@@@*:                    
                            -#@@@@@%%%%%%%%%%%%%@@@@@@..-@@@@@%%%%%%%%%%%@@@@@+.                    
                            :*@@@@@%%%%%%%%%%%%%@@@@@@. .%@@@@%%%%%%%%%%%@@@@%-                     
                           ..*@@@@@%%%%%%%%%%%%%@@@@@*  .=@@@@@%%%%%%%%%@@@@@+.                     
                             +@@@@@@%%%%%%%%%%%@@@@@@.   .#@@@@@@@@@@@@@@@@@+.                      
                             =%@@@@@@@%%%%%%@@@@@@@@*    ..=@@@@@@@@@@@@@%+..                       
                             .-%@@@@@@@@@@@@@@@@@@@*.      ...:::::::::::.                          
                               .=#@@@@@@@@@@@@@@@*:                                                 
                                 ..-=**####*++=:..                                                  
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
*/