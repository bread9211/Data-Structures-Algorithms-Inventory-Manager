import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.sql.Date;

public class RemoveItemDialog extends JDialog {
    private JTextField itemIDField;
    private JTextField quantityField;
    private JLabel itemNameLabel;
    private JLabel currentStockLabel;
    private JComboBox<String> actionComboBox;
    private JButton removeButton;
    private JButton cancelButton;
    private JList<String> searchResultsList;
    private DefaultListModel<String> searchResultsModel;
    private javax.swing.Timer searchTimer;
    
    private boolean confirmed = false;
    private Warehouse warehouse;
    private int selectedSKU = -1;
    
    public RemoveItemDialog(JFrame parent, Warehouse warehouse) {
        super(parent, "Remove/Sell Item", true);
        this.warehouse = warehouse;
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
        mainPanel.add(new JLabel("Item ID (SKU):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        itemIDField = new JTextField();
        itemIDField.getDocument().addDocumentListener(new DocumentListener() {
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
                
                // Create new timer with 200ms delay to debounce
                searchTimer = new javax.swing.Timer(200, event -> performSearch());
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
        mainPanel.add(itemIDField, gbc);
        
        // Search Results List
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 10, 10);
        
        searchResultsModel = new DefaultListModel<>();
        searchResultsList = new JList<>(searchResultsModel);
        searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selected = searchResultsList.getSelectedValue();
                    if (selected != null) {
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
        });
        
        JScrollPane scrollPane = new JScrollPane(searchResultsList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        mainPanel.add(scrollPane, gbc);
        
        // Reset layout constraints
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
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
        
        // Current Stock Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Total Stock:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        currentStockLabel = new JLabel("0");
        currentStockLabel.setForeground(new Color(100, 100, 100));
        mainPanel.add(currentStockLabel, gbc);
        
        // Action Label and ComboBox
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Action:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        actionComboBox = new JComboBox<>(new String[]{"Reduce Quantity", "Remove All Units"});
        mainPanel.add(actionComboBox, gbc);
        
        // Quantity Label and Field (only for reduce quantity)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Quantity to Remove:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        quantityField = new JTextField();
        quantityField.setText("1");
        mainPanel.add(quantityField, gbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        removeButton = new JButton("Remove");
        cancelButton = new JButton("Cancel");
        
        removeButton.addActionListener(e -> onRemoveClicked());
        cancelButton.addActionListener(e -> onCancelClicked());
        
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void performSearch() {
        String input = itemIDField.getText().trim();
        searchResultsModel.clear();
        
        if (input.isEmpty()) {
            return;
        }
        
        String lowerInput = input.toLowerCase();
        java.util.Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
        
        // Find all SKUs that contain the search string
        for (int sku : itemsByID.keySet()) {
            String skuStr = String.valueOf(sku);
            if (skuStr.contains(input)) {
                String itemName = warehouse.getItemName(sku);
                searchResultsModel.addElement(sku + " - " + itemName);
            }
        }
        
        // Update item info if only one result
        if (searchResultsModel.size() == 1) {
            updateItemInfo();
        } else if (searchResultsModel.size() == 0) {
            itemNameLabel.setText("No items found");
            currentStockLabel.setText("0");
            selectedSKU = -1;
        }
    }
    
    private void updateItemInfo() {
        String input = itemIDField.getText().trim();
        
        try {
            int sku = Integer.parseInt(input);
            selectedSKU = sku;
            
            // Get all items with this SKU and calculate total stock
            java.util.Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
            
            if (itemsByID.containsKey(sku)) {
                java.util.List<Item> items = itemsByID.get(sku);
                int totalStock = 0;
                for (Item item : items) {
                    totalStock += item.getStock();
                }
                
                String itemName = warehouse.getItemName(sku);
                itemNameLabel.setText(itemName);
                currentStockLabel.setText(String.valueOf(totalStock));
            } else {
                itemNameLabel.setText("Item not found");
                currentStockLabel.setText("0");
                selectedSKU = -1;
            }
        } catch (NumberFormatException e) {
            itemNameLabel.setText("Invalid ID");
            currentStockLabel.setText("0");
            selectedSKU = -1;
        }
    }
    
    private void onRemoveClicked() {
        if (selectedSKU < 0) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Item ID.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String action = (String) actionComboBox.getSelectedItem();
        
        if (action.equals("Remove All Units")) {
            // Remove all instances with this SKU
            java.util.Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
            
            if (!itemsByID.containsKey(selectedSKU)) {
                JOptionPane.showMessageDialog(this, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            java.util.List<Item> items = new java.util.ArrayList<>(itemsByID.get(selectedSKU));
            
            try {
                // Get itemsByChrono to find instance IDs
                java.lang.reflect.Field field = Warehouse.class.getDeclaredField("itemsByChrono");
                field.setAccessible(true);
                java.util.List<Item> itemsByChrono = (java.util.List<Item>) field.get(warehouse);
                
                // Remove all instances with this SKU
                int removedCount = 0;
                for (int i = 0; i < itemsByChrono.size(); i++) {
                    Item item = itemsByChrono.get(i);
                    if (item != null && item.getSKU() == selectedSKU) {
                        warehouse.removeItemInstance(i);
                        removedCount++;
                    }
                }
                
                if (removedCount > 0) {
                    confirmed = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not remove items.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error removing items: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Reduce quantity
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                java.util.Map<Integer, java.util.List<Item>> itemsByID = warehouse.getItemsByID();
                
                if (!itemsByID.containsKey(selectedSKU)) {
                    JOptionPane.showMessageDialog(this, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                java.util.List<Item> items = itemsByID.get(selectedSKU);
                int totalStock = 0;
                for (Item item : items) {
                    totalStock += item.getStock();
                }
                
                if (quantity > totalStock) {
                    JOptionPane.showMessageDialog(this, "Cannot remove " + quantity + " units. Total stock is " + totalStock + ".", "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Remove quantity from items with this SKU, starting with the first instance
                int remainingToRemove = quantity;
                try {
                    java.lang.reflect.Field field = Warehouse.class.getDeclaredField("itemsByChrono");
                    field.setAccessible(true);
                    java.util.List<Item> itemsByChrono = (java.util.List<Item>) field.get(warehouse);
                    
                    for (int i = 0; i < itemsByChrono.size() && remainingToRemove > 0; i++) {
                        Item item = itemsByChrono.get(i);
                        if (item != null && item.getSKU() == selectedSKU) {
                            int itemStock = item.getStock();
                            int toRemove = Math.min(remainingToRemove, itemStock);
                            
                            Item result = warehouse.removeItemQuantity(i, toRemove);
                            if (result != null) {
                                remainingToRemove -= toRemove;
                            }
                        }
                    }
                    
                    if (remainingToRemove == 0) {
                        confirmed = true;
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Could not remove all requested quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error reducing quantity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
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
