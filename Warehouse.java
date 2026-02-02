import java.util.*;
import java.time.LocalDate;

public class Warehouse {
    private String name;
    //warehousemanager the sku
    private Map<Integer, Integer> itemsByID; //instanceID, SKU
    private Map<Integer,LocalItemID> stockedIDs;
    private Map<Integer, Date> itemsByExpr;
    private List<Transaction> transactions;
    private List<Item> itemsByChrono;
    private int lastInstance;
    

    public Warehouse(String name) {
        this.name = name;
        itemsByID = new HashMap<>();
        stockedIDs = new HashSet<>();
        transactions = new ArrayList<>();
        itemsByChrono = new ArrayList<>();
        lastInstance = 0;
    }

    public String getName() {
        return name;
    }

//add and delete

    public void addItem(Item item, ItemID itemID) {
        int SKU = item.getSKU();
        addItem(item,SKU);
        if (!itemsByID.containsValue(SKU)) {
            stockedIDs.add(SKU,new LocalItemID(itemID, item.getStock()));
        }
    }
    public void addItem(Item item, int SKU){
        itemsByChrono.add(item);
        itemsByID.put(lastInstance,SKU);
        lastInstance++;
        if(item.isPerishable())
            itemsByExpr.put(item,item.getExpr());
    }

    public void purchase(int SKU, int amount) {
        if(!itemsByID.containsKey(SKU)) return;
        
        List<Integer> items = new ArrayList<>();
        items = itemsByID.containsValue(SKU);
        if(stockedIDs.get(SKU).getExpr() != 0)
            while(amount > 0 && itemsByID.containsValue(SKU))
                if(!itemsByExpr.get(i).before(LocalDate))
                    amount = itemsByChrono.get(i).removeItem(amount);
        Item item = itemsByID.get(id);
        item.removeStock(amount);

        if (item.getStock() <= 0) {
            itemsByID.remove(id);
            stockedIDs.remove(id);
        }
    }

    public void splitInstance(Item item, int amount){
        
    }

//search by id, name or keywords

    public List<Item> searchByID(int ID){
        return itemsByID.get(ID);
    }

    public List<Item> searchByName(String name) {
        List<Item> result = new ArrayList<>();
        for (Item item : itemsByID.values()) {
            if (item.getItemID().getName().equalsIgnoreCase(name)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<Item> searchByKeyword(String keyword) {
        List<Item> result = new ArrayList<>();
        for (Item item : itemsByID.values()) {
            if (item.getItemID().hasKeyword(keyword)) {
                result.add(item);
            }
        }
        return result;
    }

//sort by name, id or expiration date

    public List<Item> sortByName() {
        List<Item> list = new ArrayList<>(itemsByID.values());
        list.sort((a, b) ->
            a.getItemID().getName().compareToIgnoreCase(b.getItemID().getName()));
        return list;
    }

    public List<Item> sortByID() {
        List<Item> list = new ArrayList<>(itemsByID.values());
        list.sort((a, b) ->
            a.getItemID().getID().compareTo(b.getItemID().getID()));
        return list;
    }

    public List<Item> sortByExpiration() {
        List<Item> list = new ArrayList<>(itemsByID.values());
        list.sort((a, b) -> {
            if (a.getExpiration() == null) return 1;
            if (b.getExpiration() == null) return -1;
            return a.getExpiration().compareTo(b.getExpiration());
        });
        return list;
    }

//move to diff warehouse

    public void transferTo(Warehouse other, String id, int amount) {
        if (!itemsByID.containsKey(id)) return;

        Item item = itemsByID.get(id);
        if (item.getStock() < amount) return;

        item.removeStock(amount);
        other.addItem(new Item(item.getItemID(), amount, item.getExpiration()));

        if (item.getStock() == 0) {
            itemsByID.remove(id);
            stockedIDs.remove(id);
        }
    }

//display inventory but prob put this in the gui idk how do that

    public void printInventory() {
        System.out.println("Warehouse: " + name);
        for (Item item : itemsByID.values()) {
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