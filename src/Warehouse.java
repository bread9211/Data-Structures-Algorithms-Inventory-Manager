import java.util.*;
import java.time.LocalDate;

public class Warehouse {

    private String name;

    private Map<Integer, Item> itemsByInstance;

    private Map<Integer, Set<Integer>> itemsBySKU;

    private Map<Integer, LocalDate> itemsByExpiration;

    private List<Item> itemsByChrono;

    private int nextInstanceID;

    public Warehouse(String name) {
        this.name = name;
        itemsByInstance = new HashMap<>();
        itemsBySKU = new HashMap<>();
        itemsByExpiration = new HashMap<>();
        itemsByChrono = new ArrayList<>();
        nextInstanceID = 1;
    }

    public String getName() {
        return name;
    }

    /* =========================
       ADD / REMOVE
       ========================= */

    public int addItem(Item item) {
        int instanceID = nextInstanceID++;

        itemsByInstance.put(instanceID, item);
        itemsByChrono.add(item);

        int sku = item.getItemID().getSKU();
        itemsBySKU.putIfAbsent(sku, new HashSet<>());
        itemsBySKU.get(sku).add(instanceID);

        if (item.getExpiration() != null) {
            itemsByExpiration.put(instanceID, item.getExpiration());
        }

        return instanceID;
    }

    public void removeItemInstance(int instanceID) {
        Item item = itemsByInstance.remove(instanceID);
        if (item == null) return;

        int sku = item.getItemID().getSKU();
        itemsBySKU.get(sku).remove(instanceID);
        if (itemsBySKU.get(sku).isEmpty()) {
            itemsBySKU.remove(sku);
        }

        itemsByExpiration.remove(instanceID);
    }

    /* =========================
       SEARCH
       ========================= */

    public List<Item> searchBySKU(int sku) {
        List<Item> result = new ArrayList<>();
        if (!itemsBySKU.containsKey(sku)) return result;

        for (int id : itemsBySKU.get(sku)) {
            result.add(itemsByInstance.get(id));
        }
        return result;
    }

    public List<Item> searchByName(String name) {
        List<Item> result = new ArrayList<>();
        for (Item item : itemsByInstance.values()) {
            if (item.getItemID().getName().equalsIgnoreCase(name)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<Item> searchByKeyword(String keyword) {
        List<Item> result = new ArrayList<>();
        for (Item item : itemsByInstance.values()) {
            if (item.getItemID().hasKeyword(keyword)) {
                result.add(item);
            }
        }
        return result;
    }

    /* =========================
       SORTING
       ========================= */

    public List<Item> sortByName() {
        List<Item> list = new ArrayList<>(itemsByInstance.values());
        list.sort((a, b) ->
            a.getItemID().getName().compareToIgnoreCase(b.getItemID().getName()));
        return list;
    }

    public List<Item> sortBySKU() {
        List<Item> list = new ArrayList<>(itemsByInstance.values());
        list.sort((a, b) ->
            Integer.compare(a.getItemID().getSKU(), b.getItemID().getSKU()));
        return list;
    }

    public List<Item> sortByExpiration() {
        List<Integer> ids = new ArrayList<>(itemsByExpiration.keySet());

        ids.sort((a, b) ->
            itemsByExpiration.get(a).compareTo(itemsByExpiration.get(b)));

        List<Item> result = new ArrayList<>();
        for (int id : ids) {
            result.add(itemsByInstance.get(id));
        }
        return result;
    }

    /* =========================
       TRANSFER
       ========================= */

    public void transferTo(Warehouse other, int instanceID) {
        Item item = itemsByInstance.get(instanceID);
        if (item == null) return;

        other.addItem(item);
        removeItemInstance(instanceID);
    }

    /* =========================
       DISPLAY
       ========================= */

    public void printInventory() {
        System.out.println("Warehouse: " + name);
        for (Item item : itemsByInstance.values()) {
            System.out.println("  " + item);
        }
    }

    public void printChronological() {
        System.out.println("Chronological Log:");
        for (Item item : itemsByChrono) {
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