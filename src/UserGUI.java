import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.*;
import java.time.LocalDate;
import java.util.*;
public class UserGUI {
    private JFrame frame;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel tablePanel;
    private JPanel actionPanel;
    
    // Header components
    private JLabel titleLabel;
    private JComboBox<String> warehouseSelector;
    
    // WarehouseManager reference
    private WarehouseManager warehouseManager;
    private Map<String, Warehouse> warehouseMap;
    
    // Action buttons
    private JButton addItemButton;
    private JButton sellItemButton;
    private JButton transferItemButton;
    private JButton sortButton;
    private JButton themeSwitchButton;
    
    // Search/filter components
    private JTextField searchField;
    private javax.swing.Timer searchTimer;  // Timer for debouncing search

    // State variables
    private boolean isDarkMode = false;
    private String currentSortMode = "Warehouse";
    
    public UserGUI(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
        this.warehouseMap = new HashMap<>();
        FlatLightLaf.setup();

        initializeFrame();
        createHeaderPanel();
        createTablePanel();
        createActionPanel();
        setupLayout();
    }
    
    private void initializeFrame() {
        frame = new JFrame("WarehouseManager - Inventory Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        frame.add(mainPanel);
    }
    
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Warehouse Selection"));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);
        titleLabel = new JLabel("Current Warehouse:");
        warehouseSelector = new JComboBox<>();
        warehouseSelector.addItem("Select a Warehouse...");
        leftPanel.add(titleLabel);
        leftPanel.add(warehouseSelector);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        rightPanel.setOpaque(false);
        themeSwitchButton = new JButton("Dark Mode");
        themeSwitchButton.setFocusPainted(false);
        themeSwitchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        rightPanel.add(themeSwitchButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        themeSwitchButton.addActionListener(e -> toggleTheme());
        warehouseSelector.addActionListener(e -> onWarehouseSelected());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void onWarehouseSelected() {
        String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
        if (selectedWarehouse != null && !selectedWarehouse.equals("Select a Warehouse...")) {
            warehouseManager.setCurrent(selectedWarehouse);
            loadWarehouseInventory(selectedWarehouse);
        } else {
            loadAllWarehousesInventory();
        }
    }
    
    private void loadWarehouseInventory(String warehouseName) {
        // Recreate table without warehouse column
        String[] columnNames = {"Item ID", "Item Name", "Quantity", "Unit Price", "Expiration Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable.setModel(tableModel);
        
        Warehouse warehouse = warehouseMap.get(warehouseName);
        if (warehouse != null) {
            try {
                Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
                
                // Collect items for sorting
                java.util.List<Object[]> itemsToDisplay = new ArrayList<>();
                
                for (Map.Entry<Integer, java.util.List<Item>> entry : itemsByID.entrySet()) {
                    int totalStock = 0;
                    LocalDate lastAcquired = null;
                    LocalDate earliestExpiration = null;
                    boolean hasPerishable = false;
                    
                    for (Item item : entry.getValue()) {
                        totalStock += item.getStock();
                        if (lastAcquired == null || item.getAcquired().isAfter(lastAcquired)) {
                            lastAcquired = item.getAcquired();
                        }
                        
                        if (item.isPerishable()) {
                            PerishableItem perishable = (PerishableItem) item;
                            LocalDate exprDate = perishable.getExpr();
                            if (exprDate != null) {
                                if (earliestExpiration == null || exprDate.isBefore(earliestExpiration)) {
                                    earliestExpiration = exprDate;
                                }
                                hasPerishable = true;
                            }
                        }
                    }
                    
                    String exprDateStr = (earliestExpiration != null) ? earliestExpiration.toString() : "N/A";
                    String itemName = warehouse.getItemName(entry.getKey());
                    
                    itemsToDisplay.add(new Object[]{
                        String.valueOf(entry.getKey()),
                        itemName,
                        totalStock,
                        0.0,
                        exprDateStr,
                        "Active",
                        entry.getKey(),  // For sorting by ID
                        earliestExpiration  // For sorting by expiration date
                    });
                }
                
                // Sort based on current sort mode
                if (currentSortMode.equals("ID")) {
                    itemsToDisplay.sort((a, b) -> Integer.compare((Integer) a[6], (Integer) b[6]));
                } else if (currentSortMode.equals("Name")) {
                    itemsToDisplay.sort((a, b) -> ((String) a[1]).compareToIgnoreCase((String) b[1]));
                } else if (currentSortMode.equals("Expiration Date")) {
                    itemsToDisplay.sort((a, b) -> {
                        LocalDate dateA = (LocalDate) a[7];
                        LocalDate dateB = (LocalDate) b[7];
                        // Items with no expiration date go to the end
                        if (dateA == null && dateB == null) return 0;
                        if (dateA == null) return 1;
                        if (dateB == null) return -1;
                        return dateA.compareTo(dateB);
                    });
                }
                // "Warehouse" sort mode doesn't apply for single warehouse, so keep original order
                
                // Add sorted items to table (without the extra SKU column)
                for (Object[] itemData : itemsToDisplay) {
                    addInventoryRow(
                        (String) itemData[0],
                        (String) itemData[1],
                        (Integer) itemData[2],
                        (Double) itemData[3],
                        (String) itemData[4],
                        (String) itemData[5]
                    );
                }
            } catch (Exception ex) {
                System.err.println("Error loading warehouse inventory: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private void loadAllWarehousesInventory() {
        // Recreate table with warehouse column
        String[] columnNames = {"Warehouse", "Item ID", "Item Name", "Quantity", "Unit Price", "Expiration Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable.setModel(tableModel);
        
        // Collect all items with warehouse info
        java.util.List<Object[]> allItems = new ArrayList<>();
        
        // Iterate through all warehouses
        for (Map.Entry<String, Warehouse> warehouseEntry : warehouseMap.entrySet()) {
            String warehouseName = warehouseEntry.getKey();
            Warehouse warehouse = warehouseEntry.getValue();
            
            try {
                Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
                
                // Show each item instance separately
                for (Map.Entry<Integer, java.util.List<Item>> entry : itemsByID.entrySet()) {
                    for (Item item : entry.getValue()) {
                        LocalDate exprDate = null;
                        String exprDateStr = "N/A";
                        if (item.isPerishable()) {
                            PerishableItem perishable = (PerishableItem) item;
                            exprDate = perishable.getExpr();
                            if (exprDate != null) {
                                exprDateStr = exprDate.toString();
                            }
                        }
                        
                        String itemName = warehouse.getItemName(item.getSKU());
                        allItems.add(new Object[]{
                            warehouseName,
                            String.valueOf(item.getSKU()),
                            itemName,
                            item.getStock(),
                            0.0,
                            exprDateStr,
                            "Active",
                            item.getSKU(),  // For sorting by ID
                            exprDate  // For sorting by expiration date
                        });
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error loading items from " + warehouseName + ": " + ex.getMessage());
            }
        }
        
        // Sort based on current sort mode
        if (currentSortMode.equals("ID")) {
            allItems.sort((a, b) -> Integer.compare((Integer) a[7], (Integer) b[7]));
        } else if (currentSortMode.equals("Name")) {
            allItems.sort((a, b) -> ((String) a[2]).compareToIgnoreCase((String) b[2]));
        } else if (currentSortMode.equals("Expiration Date")) {
            allItems.sort((a, b) -> {
                LocalDate dateA = (LocalDate) a[8];
                LocalDate dateB = (LocalDate) b[8];
                // Items with no expiration date go to the end
                if (dateA == null && dateB == null) return 0;
                if (dateA == null) return 1;
                if (dateB == null) return -1;
                return dateA.compareTo(dateB);
            });
        }
        // "Warehouse" is default, which is the original order
        
        // Add sorted items to table (without the extra SKU column)
        for (Object[] itemData : allItems) {
            tableModel.addRow(new Object[]{
                itemData[0], // Warehouse
                itemData[1], // Item ID
                itemData[2], // Item Name
                itemData[3], // Quantity
                itemData[4], // Unit Price
                itemData[5], // Expiration Date
                itemData[6]  // Status
            });
        }
    }

    private void toggleTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                themeSwitchButton.setText("Dark Mode");
            } else {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                themeSwitchButton.setText("Light Mode");
            }
            isDarkMode = !isDarkMode;
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Inventory List"));
        
        // Create table with columns: Item ID, Item Name, Quantity, Unit Price, Expiration Date
        String[] columnNames = {"Item ID", "Item Name", "Quantity", "Unit Price", "Expiration Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable = new JTable(tableModel);
        
        // Make table read only
        inventoryTable.setDefaultEditor(Object.class, null);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.getTableHeader().setReorderingAllowed(false);
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }
    
    private void createActionPanel() {
        actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout(10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions & Search"));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        // Add document listener for real-time search as user types
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                triggerSearch();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                triggerSearch();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                triggerSearch();
            }
            
            private void triggerSearch() {
                // Cancel existing timer if any
                if (searchTimer != null) {
                    searchTimer.stop();
                }
                
                // Create new timer with 300ms delay to debounce rapid typing
                searchTimer = new javax.swing.Timer(300, event -> performSearch());
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        addItemButton = new JButton("Add Item");
        sellItemButton = new JButton("Sell Item");
        transferItemButton = new JButton("Transfer Item");
        sortButton = new JButton("Sort By");
        JButton exportButton = new JButton("Export");
        
        buttonPanel.add(addItemButton);
        buttonPanel.add(sellItemButton);
        buttonPanel.add(transferItemButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(exportButton);
        
        // Hook up Add Item button
        addItemButton.addActionListener(e -> openAddItemDialog());
        
        // Hook up Sell Item button
        sellItemButton.addActionListener(e -> openRemoveItemDialog());
        
        // Hook up Transfer Item button
        transferItemButton.addActionListener(e -> openTransferItemDialog());
        
        // Hook up Sort By button
        sortButton.addActionListener(e -> openSortDialog());
        
        // Hook up Export button
        exportButton.addActionListener(e -> openExportDialog());
        
        actionPanel.add(searchPanel, BorderLayout.NORTH);
        actionPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void openAddItemDialog() {
        String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
        if (selectedWarehouse == null || selectedWarehouse.equals("Select a Warehouse...")) {
            JOptionPane.showMessageDialog(frame, "Please select a warehouse first.", "No Warehouse Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Warehouse warehouse = warehouseMap.get(selectedWarehouse);
        if (warehouse != null) {
            AddItemDialog dialog = new AddItemDialog(frame, warehouse, warehouseManager);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                loadWarehouseInventory(selectedWarehouse);
                JOptionPane.showMessageDialog(frame, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void openRemoveItemDialog() {
        String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
        if (selectedWarehouse == null || selectedWarehouse.equals("Select a Warehouse...")) {
            JOptionPane.showMessageDialog(frame, "Please select a warehouse first.", "No Warehouse Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Warehouse warehouse = warehouseMap.get(selectedWarehouse);
        if (warehouse != null) {
            RemoveItemDialog dialog = new RemoveItemDialog(frame, warehouse);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                loadWarehouseInventory(selectedWarehouse);
                JOptionPane.showMessageDialog(frame, "Item removed/sold successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void openSortDialog() {
        SortDialog dialog = new SortDialog(frame, currentSortMode);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            currentSortMode = dialog.getSelectedSortMode();
            // Reload current view with new sort mode
            String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
            if (selectedWarehouse != null && !selectedWarehouse.equals("Select a Warehouse...")) {
                loadWarehouseInventory(selectedWarehouse);
            } else {
                loadAllWarehousesInventory();
            }
        }
    }
    
    private void openTransferItemDialog() {
        TransferItemDialog dialog = new TransferItemDialog(frame, warehouseMap, warehouseManager);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            // Refresh inventory to show changes
            String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
            if (selectedWarehouse != null && !selectedWarehouse.equals("Select a Warehouse...")) {
                loadWarehouseInventory(selectedWarehouse);
            } else {
                loadAllWarehousesInventory();
            }
        }
    }
    
    private void performSearch() {
        String searchQuery = searchField.getText().trim();
        
        if (searchQuery.isEmpty()) {
            // If search is empty, show all warehouses inventory instead
            String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
            if (selectedWarehouse != null && !selectedWarehouse.equals("Select a Warehouse...")) {
                loadWarehouseInventory(selectedWarehouse);
            } else {
                loadAllWarehousesInventory();
            }
            return;
        }
        
        // Recreate table with warehouse column for unified search results
        String[] columnNames = {"Warehouse", "Item ID", "Item Name", "Quantity", "Unit Price", "Expiration Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable.setModel(tableModel);
        
        // Try to parse as item ID (numeric search)
        Integer itemID = null;
        try {
            itemID = Integer.parseInt(searchQuery);
        } catch (NumberFormatException e) {
            // Not a number, will search as text first
        }
        
        // Prepare lowercase search query for case-insensitive partial matching
        String lowerSearchQuery = searchQuery.toLowerCase();
        
        // Search through all warehouses
        for (Map.Entry<String, Warehouse> warehouseEntry : warehouseMap.entrySet()) {
            String warehouseName = warehouseEntry.getKey();
            Warehouse warehouse = warehouseEntry.getValue();
            
            try {
                java.util.List<Item> results = new java.util.ArrayList<>();
                java.util.Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
                
                // Priority 1: Search for partial matches on keywords (case-insensitive)
                for (Map.Entry<Integer, java.util.List<Item>> entry : itemsByID.entrySet()) {
                    String[] keywords = warehouse.getItemKeywords(entry.getKey());
                    for (String keyword : keywords) {
                        if (keyword.toLowerCase().contains(lowerSearchQuery)) {
                            results.addAll(entry.getValue());
                            break;  // Don't add duplicates if multiple keywords match
                        }
                    }
                }
                
                // Priority 2: If no keyword results, search for partial matches on item name
                if (results.isEmpty()) {
                    for (Map.Entry<Integer, java.util.List<Item>> entry : itemsByID.entrySet()) {
                        String itemName = warehouse.getItemName(entry.getKey());
                        if (itemName.toLowerCase().contains(lowerSearchQuery)) {
                            results.addAll(entry.getValue());
                        }
                    }
                }
                
                // Priority 3: If no name results and query is numeric, search by ID
                if (results.isEmpty() && itemID != null) {
                    results.addAll(warehouse.searchByID(itemID));
                }
                
                // Add results to table
                for (Item item : results) {
                    String exprDateStr = "N/A";
                    if (item.isPerishable()) {
                        PerishableItem perishable = (PerishableItem) item;
                        if (perishable.getExpr() != null) {
                            exprDateStr = perishable.getExpr().toString();
                        }
                    }
                    
                    String itemName = warehouse.getItemName(item.getSKU());
                    tableModel.addRow(new Object[]{
                        warehouseName,
                        String.valueOf(item.getSKU()),
                        itemName,
                        item.getStock(),
                        0.0,
                        exprDateStr,
                        "Active"
                    });
                }
            } catch (Exception ex) {
                System.err.println("Error searching in " + warehouseName + ": " + ex.getMessage());
            }
        }
    }
    
    private void setupLayout() {
        frame.setVisible(true);
    }
    
    // Method to add items to the table
    public void addInventoryRow(String itemID, String itemName, int quantity, double unitPrice, String expirationDate, String status) {
        tableModel.addRow(new Object[]{itemID, itemName, quantity, unitPrice, expirationDate, status});
    }
    
    // Method to clear the table
    public void clearTable() {
        tableModel.setRowCount(0);
    }
    
    // Method to get the selected row data
    public int getSelectedRow() {
        return inventoryTable.getSelectedRow();
    }
    
    // Method to add warehouse to dropdown and store reference
    public void addWarehouse(String warehouseName, Warehouse warehouse) {
        warehouseSelector.addItem(warehouseName);
        warehouseMap.put(warehouseName, warehouse);
    }
    
    // Legacy method for backwards compatibility
    public void addWarehouse(String warehouseName) {
        warehouseSelector.addItem(warehouseName);
    }
    
    // Method to add action listeners
    public void addWarehouseSelectorListener(ActionListener listener) {
        warehouseSelector.addActionListener(listener);
    }
    
    public void addAddItemListener(ActionListener listener) {
        addItemButton.addActionListener(listener);
    }
    
    public void addSellItemListener(ActionListener listener) {
        sellItemButton.addActionListener(listener);
    }
    
    public void addTransferItemListener(ActionListener listener) {
        transferItemButton.addActionListener(listener);
    }
    
    public void addSortListener(ActionListener listener) {
        sortButton.addActionListener(listener);
    }
    
    private void openExportDialog() {
        ExportDialog exportDialog = new ExportDialog(frame, warehouseManager);
        exportDialog.setVisible(true);
        
        if (exportDialog.isConfirmed()) {
            java.util.List<String> selectedWarehouses = exportDialog.getSelectedWarehouses();
            
            // Show file chooser to select save location
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Export File");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
            fileChooser.setSelectedFile(new java.io.File("warehouse_export.csv"));
            
            int result = fileChooser.showSaveDialog(frame);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                
                // Ensure .csv extension
                if (!filePath.endsWith(".csv")) {
                    filePath += ".csv";
                }
                
                try {
                    ExportUtil.exportWarehousesToCSV(filePath, selectedWarehouses, warehouseManager);
                    JOptionPane.showMessageDialog(frame, "Export successful!\nFile saved to: " + filePath, "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Error during export: " + e.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Method to refresh the inventory display with all warehouses
    public void refreshInventory() {
        loadAllWarehousesInventory();
    }
    
    // Getter methods
    public String getSearchQuery() {
        return searchField.getText();
    }
    
    public String getSelectedWarehouse() {
        Object selected = warehouseSelector.getSelectedItem();
        return selected != null ? selected.toString() : null;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public WarehouseManager getWarehouseManager() {
        return warehouseManager;
    }
}
