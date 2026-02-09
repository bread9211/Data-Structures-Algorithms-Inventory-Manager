import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ExportUtil {
    
    public static void exportWarehousesToCSV(String filePath, List<String> warehouseNames, WarehouseManager warehouseManager) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Warehouse,SKU,Item Name,Quantity,Buy Price,Sell Price,Expiration Date\n");
            
            // Write data for each selected warehouse
            for (String warehouseName : warehouseNames) {
                Warehouse warehouse = warehouseManager.getWarehouse(warehouseName);
                if (warehouse != null) {
                    exportWarehouseData(writer, warehouse);
                }
            }
        }
    }
    
    private static void exportWarehouseData(FileWriter writer, Warehouse warehouse) throws IOException {
        // Get all items grouped by SKU
        Map<Integer, List<Item>> itemsByID = warehouse.getItemsByID();
        
        for (Map.Entry<Integer, List<Item>> entry : itemsByID.entrySet()) {
            int sku = entry.getKey();
            List<Item> items = entry.getValue();
            
            // Get item metadata (name and prices)
            String itemName = warehouse.getItemName(sku);
            
            // Calculate total quantity for this SKU
            int totalQuantity = 0;
            String expirationDate = "N/A";
            
            for (Item item : items) {
                if (item != null) {
                    totalQuantity += item.getStock();
                    
                    // Get expiration date if perishable
                    if (item.isPerishable()) {
                        LocalDate expr = item.getExpr();
                        if (expr != null) {
                            expirationDate = expr.toString();
                        }
                    }
                }
            }
            
            // Get pricing information
            String buyPrice = "N/A";
            String sellPrice = "N/A";
            
            try {
                // Try to get prices from ItemID metadata
                ItemID itemID = warehouse.getItemIDForSKU(sku);
                if (itemID != null) {
                    // Use reflection to access price fields since they're private
                    buyPrice = String.valueOf(getItemIDPrice(itemID, "buyPrice"));
                    sellPrice = String.valueOf(getItemIDPrice(itemID, "sellPrice"));
                }
            } catch (Exception e) {
                // Keep default "N/A" values
            }
            
            // Write row
            writer.append(warehouse.getName()).append(",");
            writer.append(String.valueOf(sku)).append(",");
            writer.append(escapeCsvValue(itemName)).append(",");
            writer.append(String.valueOf(totalQuantity)).append(",");
            writer.append(buyPrice).append(",");
            writer.append(sellPrice).append(",");
            writer.append(expirationDate).append("\n");
        }
    }
    
    private static int getItemIDPrice(ItemID itemID, String fieldName) {
        try {
            java.lang.reflect.Field field = ItemID.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(itemID);
            if (value instanceof Integer) {
                return (Integer) value;
            }
        } catch (Exception e) {
            // Return 0 if field not found or error occurs
        }
        return 0;
    }
    
    private static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        
        // If the value contains comma, newline, or quotes, wrap it in quotes and escape internal quotes
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
