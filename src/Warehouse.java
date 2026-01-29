import java.util.*;

public class Warehouse {
    private String name;
    private Map<Integer, List<Item>> itemsByID;
    private List<Item> itemsByChrono;

    public Warehouse(String name) {
        this.name = name;
        itemsByID = new HashMap<>();
        itemsByChrono = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addItem(Item item) {
        int SKU = item.getSKU();
        if (!itemsByID.containsKey(SKU)) {
            itemsByID.put(SKU, new ArrayList<>());
        }
        itemsByID.get(SKU).add(item);
        itemsByChrono.add(item);
    }

    public void removeItem(int id, int amount) {
        if (!itemsByID.containsKey(id)) return;

        List<Item> items = itemsByID.get(id);
        int remaining = amount;
        for (int i = items.size() - 1; i >= 0 && remaining > 0; i--) {
            Item item = items.get(i);
            int removed = item.removeItem(remaining);
            if (removed == -1) {
                remaining = 0;
            } else {
                remaining = removed;
            }
            
            if (item.getStock() <= 0) {
                items.remove(i);
                itemsByChrono.remove(item);
            }
        }
        
        if (items.isEmpty()) {
            itemsByID.remove(id);
        }
    }

    public List<Item> searchByID(int ID) {
        return itemsByID.getOrDefault(ID, new ArrayList<>());
    }

    public List<Item> searchByName(String name) {
        List<Item> result = new ArrayList<>();
        for (List<Item> items : itemsByID.values()) {
            for (Item item : items) {
                if (String.valueOf(item.getSKU()).contains(name)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public List<Item> sortByID() {
        List<Item> list = new ArrayList<>(itemsByChrono);
        list.sort((a, b) -> Integer.compare(a.getSKU(), b.getSKU()));
        return list;
    }

    public List<Item> sortByAcquiredDate() {
        List<Item> list = new ArrayList<>(itemsByChrono);
        list.sort((a, b) -> {
            if (a.getAcquired() == null) return 1;
            if (b.getAcquired() == null) return -1;
            return a.getAcquired().compareTo(b.getAcquired());
        });
        return list;
    }

    public void transferTo(Warehouse other, int id, int amount) {
        if (!itemsByID.containsKey(id)) return;

        List<Item> items = itemsByID.get(id);
        int remaining = amount;
        
        for (int i = items.size() - 1; i >= 0 && remaining > 0; i--) {
            Item item = items.get(i);
            if (item.getStock() <= remaining) {
                remaining -= item.getStock();
                other.addItem(item);
                items.remove(i);
                itemsByChrono.remove(item);
            } else {
                Item newItem = new Item(id, remaining, item.getAcquired());
                other.addItem(newItem);
                item.removeItem(remaining);
                remaining = 0;
            }
        }
        
        if (items.isEmpty()) {
            itemsByID.remove(id);
        }
    }

    public void printInventory() {
        System.out.println("Warehouse: " + name);
        for (Map.Entry<Integer, List<Item>> entry : itemsByID.entrySet()) {
            int totalStock = 0;
            for (Item item : entry.getValue()) {
                totalStock += item.getStock();
            }
            System.out.println("  Item ID: " + entry.getKey() + ", Total Stock: " + totalStock);
        }
    }

    public Map<Integer, List<Item>> getItemsByID() {
        return new HashMap<>(itemsByID);
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