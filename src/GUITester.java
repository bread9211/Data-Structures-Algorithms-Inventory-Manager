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
        warehouseA.addItem(new Item(1001, 150, null));
        warehouseA.addItem(new Item(1002, 200, null));
        warehouseA.addItem(new Item(1003, 75, null));
        
        warehouseB.addItem(new Item(2001, 50, null));
        warehouseB.addItem(new Item(2002, 30, null));
        
        warehouseC.addItem(new Item(3001, 100, null));
        warehouseC.addItem(new Item(3002, 60, null));
        
        // Refresh the inventory display to show all items
        gui.refreshInventory();
    }
}
