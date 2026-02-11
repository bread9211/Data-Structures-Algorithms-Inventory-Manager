import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddItemDialog extends JDialog {
    private JTextField itemIDField;
    private JTextField quantityField;
    private JLabel itemNameLabel;
    private JButton addButton;
    private JButton cancelButton;
    private JList<String> searchResultsList;
    private DefaultListModel<String> searchResultsModel;
    private JScrollPane searchScrollPane;
    
    private boolean confirmed = false;
    private Warehouse warehouse;
    private WarehouseManager warehouseManager;
    
    public AddItemDialog(JFrame parent, Warehouse warehouse, WarehouseManager warehouseManager) {
        super(parent, "Add Item to Warehouse", true);
        this.warehouse = warehouse;
        this.warehouseManager = warehouseManager;
        setSize(500, 450);
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
        mainPanel.add(new JLabel("Item ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        itemIDField = new JTextField();
        itemIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchResults();
                updateItemName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchResults();
                updateItemName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchResults();
                updateItemName();
            }
        });
        mainPanel.add(itemIDField, gbc);
        
        // Search Results Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Search Results:"), gbc);
        
        // Search Results List
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        searchResultsModel = new DefaultListModel<>();
        searchResultsList = new JList<>(searchResultsModel);
        searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && searchResultsList.getSelectedValue() != null) {
                String selected = searchResultsList.getSelectedValue();
                // Extract SKU from "SKU - ItemName" format
                String sku = selected.split(" - ")[0];
                itemIDField.setText(sku);
            }
        });
        searchScrollPane = new JScrollPane(searchResultsList);
        searchScrollPane.setPreferredSize(new Dimension(300, 100));
        mainPanel.add(searchScrollPane, gbc);
        
        // Item Name Label (display only)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
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
    }
    
    private void updateSearchResults() {
        searchResultsModel.clear();
        
        try {
            String skuText = itemIDField.getText().trim();
            if (skuText.isEmpty()) {
                return;
            }
            
            // Get all unique SKUs across all warehouses using WarehouseManager API
            java.util.Set<Integer> allSkus = new java.util.HashSet<>();
            
            if (warehouseManager != null) {
                allSkus = warehouseManager.getAllSKUs();
            } else if (warehouse != null) {
                // Fallback: search only current warehouse if warehouseManager is unavailable
                Map<Integer, List<Item>> itemsByID = warehouse.getItemsByID();
                allSkus.addAll(itemsByID.keySet());
            }
            
            // Search for SKUs that start with the input
            for (Integer sku : allSkus) {
                String skuStr = String.valueOf(sku);
                if (skuStr.startsWith(skuText)) {
                    String itemName = (warehouseManager != null) 
                        ? warehouseManager.getItemNameForSKU(sku) 
                        : warehouse.getItemName(sku);
                    String displayText = sku + " - " + itemName;
                    searchResultsModel.addElement(displayText);
                }
            }
        } catch (NumberFormatException ex) {
            // Ignore - user is still typing
        }
    }
    
    private void updateItemName() {
        try {
            String idText = itemIDField.getText().trim();
            if (idText.isEmpty()) {
                itemNameLabel.setText("Unknown");
                itemNameLabel.setForeground(new Color(100, 100, 100));
                return;
            }
            
            int itemID = Integer.parseInt(idText);
            
            // Search for this item ID using WarehouseManager API
            boolean found = false;
            if (warehouseManager != null) {
                Warehouse warehouseWithItem = warehouseManager.findWarehouseBySKU(itemID);
                if (warehouseWithItem != null) {
                    String itemName = warehouseManager.getItemNameForSKU(itemID);
                    itemNameLabel.setText(itemName);
                    itemNameLabel.setForeground(new Color(0, 100, 0));
                    found = true;
                }
            }
            
            if (!found) {
                // Check current warehouse as fallback
                List<Item> items = warehouse.searchByID(itemID);
                if (!items.isEmpty()) {
                    itemNameLabel.setText(warehouse.getItemName(itemID));
                    itemNameLabel.setForeground(new Color(0, 100, 0));
                    return;
                }
                
                itemNameLabel.setText("Unknown Item ID");
                itemNameLabel.setForeground(new Color(200, 100, 100));
            }
        } catch (NumberFormatException ex) {
            itemNameLabel.setText("Invalid ID");
            itemNameLabel.setForeground(new Color(200, 100, 100));
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
            
            // Validate that item ID exists and get its ItemID metadata
            ItemID existingItemID = null;
            if (warehouseManager != null) {
                existingItemID = warehouseManager.getItemIDForSKU(itemID);
            } else {
                // Fallback to current warehouse
                existingItemID = warehouse.getItemIDForSKU(itemID);
            }
            
            if (existingItemID == null) {
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
                        Item newItem = new Item(itemID, quantity, null);
                        warehouse.addItem(newItem, itemID);
                        
                        confirmed = true;
                        dispose();
                    }
                }
                return;
            }
            
            // Add item to warehouse using WarehouseManager or directly
            Item newItem = new Item(itemID, quantity, null);
            warehouse.addItem(newItem, existingItemID);
            
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
