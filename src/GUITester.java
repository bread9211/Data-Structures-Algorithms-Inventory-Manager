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
        
        // Warehouse A - Produce/Fruits
        ItemID banana = new ItemID("Banana", 1001, 150, 100, new String[]{"fruit", "produce"}, 2, 14);
        ItemID apple = new ItemID("Apple", 1002, 200, 120, new String[]{"fruit", "produce"}, 2, 21);
        ItemID orange = new ItemID("Orange", 1003, 175, 110, new String[]{"fruit", "produce"}, 2, 21);
        ItemID lettuce = new ItemID("Lettuce", 1004, 100, 50, new String[]{"vegetable", "produce"}, 1, 7);
        ItemID tomato = new ItemID("Tomato", 1005, 120, 70, new String[]{"vegetable", "produce"}, 1, 10);
        
        warehouseA.addItem(new PerishableItem(1001, 150, null, java.sql.Date.valueOf("2026-02-14")), banana);
        warehouseA.addItem(new PerishableItem(1002, 200, null, java.sql.Date.valueOf("2026-02-20")), apple);
        warehouseA.addItem(new PerishableItem(1003, 75, null, java.sql.Date.valueOf("2026-02-18")), orange);
        warehouseA.addItem(new PerishableItem(1004, 100, null, java.sql.Date.valueOf("2026-02-08")), lettuce);
        warehouseA.addItem(new PerishableItem(1005, 50, null, java.sql.Date.valueOf("2026-02-15")), tomato);
        
        // Warehouse B - Hardware
        ItemID m3Screw = new ItemID("M3 Screw", 2001, 50, 30, new String[]{"hardware", "fastener"}, 5, 0);
        ItemID bolt = new ItemID("Bolt M5", 2002, 30, 20, new String[]{"hardware", "fastener"}, 5, 0);
        ItemID washer = new ItemID("Washer", 2003, 80, 40, new String[]{"hardware", "fastener"}, 5, 0);
        ItemID nail = new ItemID("Nail 2in", 2004, 120, 60, new String[]{"hardware", "fastener"}, 5, 0);
        
        warehouseB.addItem(new Item(2001, 500, null), m3Screw);
        warehouseB.addItem(new Item(2002, 300, null), bolt);
        warehouseB.addItem(new Item(2003, 800, null), washer);
        warehouseB.addItem(new Item(2004, 1200, null), nail);
        
        // Warehouse C - Office Supplies & Mixed
        ItemID notebook = new ItemID("Notebook", 3001, 100, 60, new String[]{"office", "stationery"}, 3, 0);
        ItemID pen = new ItemID("Pen (ballpoint)", 3002, 60, 30, new String[]{"office", "stationery"}, 3, 0);
        ItemID pencil = new ItemID("Pencil HB", 3003, 45, 20, new String[]{"office", "stationery"}, 3, 0);
        ItemID paperReam = new ItemID("Paper Ream A4", 3004, 200, 100, new String[]{"office", "paper"}, 2, 0);
        ItemID coffee = new ItemID("Coffee Ground", 3005, 300, 150, new String[]{"beverage", "coffee"}, 2, 90);
        
        warehouseC.addItem(new Item(3001, 100, null), notebook);
        warehouseC.addItem(new Item(3002, 60, null), pen);
        warehouseC.addItem(new Item(3003, 200, null), pencil);
        warehouseC.addItem(new Item(3004, 50, null), paperReam);
        warehouseC.addItem(new PerishableItem(3005, 75, null, java.sql.Date.valueOf("2026-08-04")), coffee);
        
        // Refresh the inventory display to show all items
        gui.refreshInventory();
    }
}
