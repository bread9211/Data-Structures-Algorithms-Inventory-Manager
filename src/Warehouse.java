import java.util.*;
import java.sql.Date;

public class Warehouse {

    private String name;
    private Map<Integer, Set<Integer>> itemsBySKU;
    private Map<Integer,LocalItemID> stockedIDs;
    private Map<Integer, Date> itemsByExpiration;
    private List<Item> itemsByChrono;
    private List<Transaction> transactions;
    private int nextInstanceID;

    public Warehouse(String name) {
        this.name = name;
        itemsBySKU = new HashMap<>();
        stockedIDs = new HashMap<>();
      itemsByExpiration = new HashMap<>();
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

    public int addItem(Item item,int sku) {
        item.setInstance(nextInstanceID);
        itemsByChrono.add(item);

        itemsBySKU.putIfAbsent(sku, new HashSet<>());
        itemsBySKU.get(sku).add(nextInstanceID);

        if (item.isPerishable())
            itemsByExpiration.put(nextInstanceID, item.getExpr());
        nextInstanceID++;
        return nextInstanceID-1;
    }

    public int addItem(Item item,ItemID itemID) {
        int sku = item.getSKU();
        if(!itemsBySKU.containsKey(sku))
            stockedIDs.put(sku,new LocalItemID(itemID, item.getStock()));
        return addItem(item,sku);
    }

    public Item removeItemInstance(int instanceID) {
        Item item = itemsByChrono.get(instanceID);
        if (item == null) return null;

        int sku = item.getSKU();
        itemsBySKU.get(sku).remove(instanceID);
        if (itemsBySKU.get(sku).isEmpty()) {
            itemsBySKU.remove(sku);
        }
        itemsByExpiration.remove(instanceID);
        itemsByChrono.set(instanceID,null);
        return item;
    }

    public boolean splitInstance(Item item, int amount){
        if(item.getStock() > amount){
            addItem(new Item(item.getSKU(),amount,item.getAcquired()),item.getSKU());
            item.removeItem(amount);
            return true;
        }
        return false;
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

    /* =========================
       SORTING
       ========================= */

    public List<Item> sortByName() {
        List<Item> list = new ArrayList<>(itemsByChrono);
        list.sort((a, b) ->
            stockedIDs.get(a.getSKU()).getReference().getName().compareToIgnoreCase(stockedIDs.get(b.getSKU()).getReference().getName()));
        return list;
    }

    public List<Item> sortBySKU() {
        List<Item> list = new ArrayList<>(itemsByChrono);
        list.sort((a, b) ->
            Integer.compare(a.getSKU(), b.getSKU()));
        return list;
    }

    public List<Item> sortByExpiration() {
        List<Integer> ids = new ArrayList<>(itemsByExpiration.keySet());
        ids.sort((a, b) ->
            itemsByExpiration.get(a).compareTo(itemsByExpiration.get(b)));

        List<Item> result = new ArrayList<>();
        for (int id : ids) {
            result.add(itemsByChrono.get(id));
        }
        return result;
    }

    /* =========================
       TRANSFER
       ========================= */
    //NEEDS TO MAKE A TRANSACTION
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
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