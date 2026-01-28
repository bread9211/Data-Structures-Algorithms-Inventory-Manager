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
        
        // Add sample inventory items using the GUI directly
        // Users can select a warehouse and the inventory will be displayed
        gui.addInventoryRow("ID001", "Apples", 150, 2.50, "2026-02-15", "Good");
        gui.addInventoryRow("ID002", "Oranges", 200, 3.00, "2026-02-20", "Good");
        gui.addInventoryRow("ID003", "Bananas", 75, 1.50, "2026-01-28", "Expiring Soon");
    }
}
