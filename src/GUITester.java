public class GUITester {
    public static void main(String[] args) {
        // Create WarehouseManager
        WarehouseManager manager = new WarehouseManager();
        
        // Add warehouses by name
        manager.addWarehouse("Warehouse A");
        manager.addWarehouse("Warehouse B");
        manager.addWarehouse("Warehouse C");
        
        // Get warehouse references
        Warehouse warehouseA = manager.getWarehouse("Warehouse A");
        Warehouse warehouseB = manager.getWarehouse("Warehouse B");
        Warehouse warehouseC = manager.getWarehouse("Warehouse C");
        
        // Create the GUI with WarehouseManager
        UserGUI gui = new UserGUI(manager);
        
        // Add warehouses to the dropdown and link them
        gui.addWarehouse("Warehouse A", warehouseA);
        gui.addWarehouse("Warehouse B", warehouseB);
        gui.addWarehouse("Warehouse C", warehouseC);
        
        // Warehouse A - Produce/Fruits
        ItemID banana = new ItemID("Banana", 1001, 150, 100, new String[]{"fruit", "produce"});
        ItemID apple = new ItemID("Apple", 1002, 200, 120, new String[]{"fruit", "produce"});
        ItemID orange = new ItemID("Orange", 1003, 175, 110, new String[]{"fruit", "produce"});
        ItemID lettuce = new ItemID("Lettuce", 1004, 100, 50, new String[]{"vegetable", "produce"});
        ItemID tomato = new ItemID("Tomato", 1005, 120, 70, new String[]{"vegetable", "produce"});
        
        warehouseA.addItem(new PerishableItem(1001, 150, null, java.sql.Date.valueOf("2026-02-14")), banana);
        warehouseA.addItem(new PerishableItem(1002, 200, null, java.sql.Date.valueOf("2026-02-20")), apple);
        warehouseA.addItem(new PerishableItem(1003, 75, null, java.sql.Date.valueOf("2026-02-18")), orange);
        warehouseA.addItem(new PerishableItem(1004, 100, null, java.sql.Date.valueOf("2026-02-08")), lettuce);
        warehouseA.addItem(new PerishableItem(1005, 50, null, java.sql.Date.valueOf("2026-02-15")), tomato);
        
        // Warehouse B - Hardware
        ItemID m3Screw = new ItemID("M3 Screw", 2001, 50, 30, new String[]{"hardware", "fastener"});
        ItemID bolt = new ItemID("Bolt M5", 2002, 30, 20, new String[]{"hardware", "fastener"});
        ItemID washer = new ItemID("Washer", 2003, 80, 40, new String[]{"hardware", "fastener"});
        ItemID nail = new ItemID("Nail 2in", 2004, 120, 60, new String[]{"hardware", "fastener"});
        
        warehouseB.addItem(new Item(2001, 500, null), m3Screw);
        warehouseB.addItem(new Item(2002, 300, null), bolt);
        warehouseB.addItem(new Item(2003, 800, null), washer);
        warehouseB.addItem(new Item(2004, 1200, null), nail);
        
        // Warehouse C - Office Supplies & Mixed
        ItemID notebook = new ItemID("Notebook", 3001, 100, 60, new String[]{"office", "stationery"});
        ItemID pen = new ItemID("Pen (ballpoint)", 3002, 60, 30, new String[]{"office", "stationery"});
        ItemID pencil = new ItemID("Pencil HB", 3003, 45, 20, new String[]{"office", "stationery"});
        ItemID paperReam = new ItemID("Paper Ream A4", 3004, 200, 100, new String[]{"office", "paper"});
        ItemID coffee = new ItemID("Coffee Ground", 3005, 300, 150, new String[]{"beverage", "coffee"});
        
        warehouseC.addItem(new Item(3001, 100, null), notebook);
        warehouseC.addItem(new Item(3002, 60, null), pen);
        warehouseC.addItem(new Item(3003, 200, null), pencil);
        warehouseC.addItem(new Item(3004, 50, null), paperReam);
        warehouseC.addItem(new PerishableItem(3005, 75, null, java.sql.Date.valueOf("2026-08-04")), coffee);
        
        // Refresh the inventory display to show all items
        gui.refreshInventory();
    }
}
