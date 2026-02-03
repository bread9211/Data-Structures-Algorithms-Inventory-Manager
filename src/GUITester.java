public class GUITester {
    public static void main(String[] args) {
        // Create WarehouseManager and warehouses
        WarehouseManager manager = new WarehouseManager();
        
        Warehouse warehouseA = new Warehouse("Warehouse A");
        Warehouse warehouseB = new Warehouse("Warehouse B");
        Warehouse warehouseC = new Warehouse("Warehouse C");
        
        manager.addWarehouse(warehouseA);
        manager.addWarehouse(warehouseB);
        manager.addWarehouse(warehouseC);
        
        // Create the GUI with WarehouseManager
        UserGUI gui = new UserGUI(manager);
        
        // Add warehouses to the dropdown and link them
        gui.addWarehouse("Warehouse A", warehouseA);
        gui.addWarehouse("Warehouse B", warehouseB);
        gui.addWarehouse("Warehouse C", warehouseC);
        
        // Add sample inventory items
        warehouseA.addItem(new Item(1001, 150, null), 1001);
        warehouseA.addItem(new Item(1002, 200, null), 1002);
        warehouseA.addItem(new Item(1003, 75, null), 1003);
        
        // Add perishable items with expiration dates
        warehouseA.addItem(new PerishableItem(1004, 100, null, java.sql.Date.valueOf("2026-12-31")), 1004);
        warehouseA.addItem(new PerishableItem(1005, 50, null, java.sql.Date.valueOf("2026-03-15")), 1005);
        
        warehouseB.addItem(new Item(2001, 50, null), 2001);
        warehouseB.addItem(new Item(2002, 30, null), 2002);
        
        // Add perishable items to Warehouse B
        warehouseB.addItem(new PerishableItem(2003, 80, null, java.sql.Date.valueOf("2026-02-14")), 2003);
        warehouseB.addItem(new PerishableItem(2004, 120, null, java.sql.Date.valueOf("2026-06-30")), 2004);
        
        warehouseC.addItem(new Item(3001, 100, null), 3001);
        warehouseC.addItem(new Item(3002, 60, null), 3002);
        
        // Add perishable items to Warehouse C
        warehouseC.addItem(new PerishableItem(3003, 45, null, java.sql.Date.valueOf("2026-04-10")), 3003);
        warehouseC.addItem(new PerishableItem(3004, 200, null, java.sql.Date.valueOf("2026-09-20")), 3004);
        
        // Refresh the inventory display to show all items
        gui.refreshInventory();
    }
}
