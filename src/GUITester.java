public class GUITester {
    public static void main(String[] args) {
        // Create the GUI
        UserGUI gui = new UserGUI();
        
        // Add sample warehouses
        gui.addWarehouse("Warehouse A");
        gui.addWarehouse("Warehouse B");
        gui.addWarehouse("Warehouse C");
        
        // Add sample inventory items
        gui.addInventoryRow("ID001", "Apples", 150, 2.50, "2026-02-15", "Good");
        gui.addInventoryRow("ID002", "Oranges", 200, 3.00, "2026-02-20", "Good");
        gui.addInventoryRow("ID003", "Bananas", 75, 1.50, "2026-01-28", "Expiring Soon");
        gui.addInventoryRow("ID004", "Milk", 50, 4.25, "2026-02-05", "Good");
        gui.addInventoryRow("ID005", "Cheese", 30, 8.99, "2026-03-10", "Good");
        gui.addInventoryRow("ID006", "Bread", 100, 3.50, "2026-01-26", "Expiring Soon");
        gui.addInventoryRow("ID007", "Yogurt", 60, 5.99, "2026-02-01", "Good");
        gui.addInventoryRow("ID008", "Eggs", 200, 6.50, "2026-02-10", "Good");
    }
}
