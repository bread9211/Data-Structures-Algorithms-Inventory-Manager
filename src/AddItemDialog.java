import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class AddItemDialog extends JDialog {
    private JTextField itemIDField;
    private JTextField quantityField;
    private JLabel itemNameLabel;
    private JButton addButton;
    private JButton cancelButton;
    
    private DefaultListModel<String> searchResultsModel;
    private JList<String> searchResultsList;
    private int selectedSKU = -1;
    private javax.swing.Timer searchTimer;
    
    private boolean confirmed = false;
    private Warehouse warehouse;
    private WarehouseManager warehouseManager;
    
    public AddItemDialog(JFrame parent, Warehouse warehouse, WarehouseManager warehouseManager) {
        super(parent, "Add Item to Warehouse", true);
        this.warehouse = warehouse;
        this.warehouseManager = warehouseManager;
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Item ID Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Search item: "), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        itemIDField = new JTextField();
        itemIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleSearch();
            }
        });
        mainPanel.add(itemIDField, gbc);
        
        // Search Results List
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        searchResultsModel = new DefaultListModel<>();
        searchResultsList = new JList<>(searchResultsModel);
        searchResultsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selected = searchResultsList.getSelectedValue();
                    if (selected != null) {
                        // Check if this is the "Create New Item" option
                        if (selected.startsWith(">>> Create New Item")) {
                            // Auto-generate unique SKU and open register dialog
                            int newSKU = generateUniqueSKU();
                            openRegisterItemDialog(newSKU);
                            searchResultsModel.clear();
                            searchResultsList.clearSelection();
                        } else {
                            // Extract SKU from selected result (format: "SKU - Name")
                            int dashIndex = selected.indexOf(" - ");
                            if (dashIndex > 0) {
                                try {
                                    int sku = Integer.parseInt(selected.substring(0, dashIndex));
                                    itemIDField.setText(String.valueOf(sku));
                                } catch (NumberFormatException ex) {
                                    // Ignore
                                }
                            }
                        }
                    }
                }
            }
        });
        
        // Add double-click listener for opening register dialog
        searchResultsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selected = searchResultsList.getSelectedValue();
                    if (selected != null && selected.startsWith(">>> Create New Item")) {
                        // Auto-generate unique SKU and open register dialog
                        int newSKU = generateUniqueSKU();
                        openRegisterItemDialog(newSKU);
                        searchResultsModel.clear();
                        searchResultsList.clearSelection();
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(searchResultsList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        mainPanel.add(scrollPane, gbc);
        
        // Reset layout constraints
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Item Name Label (display only)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Item Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        itemNameLabel = new JLabel("Unknown");
        itemNameLabel.setForeground(new Color(100, 100, 100));
        mainPanel.add(itemNameLabel, gbc);
        
        // Quantity Label and Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Quantity:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        quantityField = new JTextField();
        mainPanel.add(quantityField, gbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        
        addButton.addActionListener(e -> onAddClicked());
        cancelButton.addActionListener(e -> onCancelClicked());
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
        
        // Initialize search timer with 200ms debounce
        searchTimer = new javax.swing.Timer(200, e -> performSearch());
        searchTimer.setRepeats(false);
    }
    
    private void scheduleSearch() {
        searchTimer.restart();
    }
        private java.util.Collection<Warehouse> getAllWarehouses() {
        try {
            java.lang.reflect.Field field = WarehouseManager.class.getDeclaredField("warehouses");
            field.setAccessible(true);
            java.util.Map<String, Warehouse> warehousesMap = (java.util.Map<String, Warehouse>) field.get(warehouseManager);
            return warehousesMap.values();
        } catch (Exception e) {
            // Fallback: return just the current warehouse
            return java.util.Collections.singleton(warehouse);
        }
    }
        private void performSearch() {
        String input = itemIDField.getText().trim();
        searchResultsModel.clear();
        
        if (input.isEmpty()) {
            return;
        }
        
        String lowerInput = input.toLowerCase();
        java.util.Set<Integer> foundSKUs = new java.util.LinkedHashSet<>();
        
        // Search across all warehouses
        java.util.Collection<Warehouse> allWarehouses = getAllWarehouses();
        
        // Tier 1: Search by item name in all warehouses
        for (Warehouse w : allWarehouses) {
            java.util.Map<Integer, java.util.List<Item>> itemsByID = w.getItemsByID();
            for (int sku : itemsByID.keySet()) {
                String itemName = w.getItemName(sku);
                if (itemName != null && itemName.toLowerCase().contains(lowerInput)) {
                    foundSKUs.add(sku);
                }
            }
        }
        
        // Tier 2: Search by SKU (number match) in all warehouses
        for (Warehouse w : allWarehouses) {
            java.util.Map<Integer, java.util.List<Item>> itemsByID = w.getItemsByID();
            for (int sku : itemsByID.keySet()) {
                String skuStr = String.valueOf(sku);
                if (skuStr.contains(input)) {
                    foundSKUs.add(sku);
                }
            }
        }
        
        // Add all found results to the list
        for (int sku : foundSKUs) {
            // Get the item name from the first warehouse that has it
            String itemName = "Unknown";
            for (Warehouse w : allWarehouses) {
                if (w.getItemsByID().containsKey(sku)) {
                    itemName = w.getItemName(sku);
                    break;
                }
            }
            searchResultsModel.addElement(sku + " - " + itemName);
        }
        
        // Update item info if only one result
        if (searchResultsModel.size() == 1) {
            updateItemInfo();
        } else if (searchResultsModel.size() == 0) {
            itemNameLabel.setText("No items found - select option to create new");
            selectedSKU = -1;
            // Add option to create new item
            searchResultsModel.addElement(">>> Create New Item ID: " + input);
        }
    }
    
    private void updateItemInfo() {
        String input = itemIDField.getText().trim();
        
        try {
            int sku = Integer.parseInt(input);
            selectedSKU = sku;
            
            // Search for this SKU across all warehouses
            java.util.Collection<Warehouse> allWarehouses = getAllWarehouses();
            String itemName = null;
            int totalStock = 0;
            
            for (Warehouse w : allWarehouses) {
                java.util.Map<Integer, java.util.List<Item>> itemsByID = w.getItemsByID();
                if (itemsByID.containsKey(sku)) {
                    if (itemName == null) {
                        itemName = w.getItemName(sku);
                    }
                    java.util.List<Item> items = itemsByID.get(sku);
                    for (Item item : items) {
                        totalStock += item.getStock();
                    }
                }
            }
            
            if (itemName != null) {
                itemNameLabel.setText(itemName + " (Stock: " + totalStock + ")");
                itemNameLabel.setForeground(new Color(0, 100, 0));
            } else {
                itemNameLabel.setText("Item not found");
                selectedSKU = -1;
            }
        } catch (NumberFormatException e) {
            itemNameLabel.setText("Invalid ID");
            itemNameLabel.setForeground(new Color(200, 100, 100));
            selectedSKU = -1;
        }
    }
    
    private int generateUniqueSKU() {
        int maxSKU = -1;
        
        // Search all warehouses to find the highest existing SKU
        java.util.Collection<Warehouse> allWarehouses = getAllWarehouses();
        for (Warehouse w : allWarehouses) {
            java.util.Map<Integer, java.util.List<Item>> itemsByID = w.getItemsByID();
            for (int sku : itemsByID.keySet()) {
                if (sku > maxSKU) {
                    maxSKU = sku;
                }
            }
        }
        
        // Return the next unique SKU
        return maxSKU + 1;
    }
    
    private void openRegisterItemDialog(int itemID) {
        RegisterItemDialog registerDialog = new RegisterItemDialog((JFrame) SwingUtilities.getWindowAncestor(this), itemID, warehouse);
        registerDialog.setVisible(true);
        
        if (registerDialog.isConfirmed()) {
            // Refresh warehouse reference to ensure we have the latest data
            warehouse = warehouseManager.getCurrent();
            
            // Item was registered, now the user can add it
            itemIDField.setText(String.valueOf(itemID));
            selectedSKU = itemID;
            
            // Clear search results and directly update item info with the newly registered item
            searchResultsModel.clear();
            updateItemInfo();
        }
    }
    
    private void onAddClicked() {
        try {
            if (itemIDField.getText().trim().isEmpty() || quantityField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int itemID = Integer.parseInt(itemIDField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if item ID exists in any warehouse
            java.util.Collection<Warehouse> allWarehouses = getAllWarehouses();
            boolean itemExists = false;
            
            for (Warehouse w : allWarehouses) {
                java.util.Map<Integer, java.util.List<Item>> itemsByID = w.getItemsByID();
                if (itemsByID.containsKey(itemID)) {
                    itemExists = true;
                    break;
                }
            }
            
            if (!itemExists) {
                // Item ID doesn't exist - prompt user to register new item
                int response = JOptionPane.showConfirmDialog(
                    this,
                    "Item ID " + itemID + " does not exist.\n\nWould you like to register a new item with this ID?",
                    "Item Not Found",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    // Open register new item dialog
                    RegisterItemDialog registerDialog = new RegisterItemDialog((JFrame) SwingUtilities.getWindowAncestor(this), itemID, warehouse);
                    registerDialog.setVisible(true);
                    
                    if (registerDialog.isConfirmed()) {
                        // Item was registered, now add it to warehouse
                        ItemID itemIDObj = warehouse.getItemIDBySkU(itemID);
                        Item newItem = new Item(itemID, quantity, null);
                        warehouse.addItem(newItem, itemIDObj);
                        
                        confirmed = true;
                        dispose();
                    }
                }
                return;
            }
            
            // Add item to warehouse directly
            ItemID itemIDObj = warehouse.getItemIDBySkU(itemID);
            Item newItem = new Item(itemID, quantity, null);
            warehouse.addItem(newItem, itemIDObj);
            
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onCancelClicked() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
