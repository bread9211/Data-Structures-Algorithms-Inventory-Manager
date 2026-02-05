import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class TransferItemDialog extends JDialog {
    private JComboBox<String> sourceWarehouseCombo;
    private JComboBox<String> recipientWarehouseCombo;
    private JTextField searchField;
    private JTable itemsTable;
    private DefaultTableModel tableModel;
    private JButton transferButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private Map<String, Warehouse> warehouseMap;
    private WarehouseManager warehouseManager;
    private javax.swing.Timer searchTimer;
    
    public TransferItemDialog(JFrame parent, Map<String, Warehouse> warehouseMap, WarehouseManager warehouseManager) {
        super(parent, "Transfer Item", true);
        this.warehouseMap = warehouseMap;
        this.warehouseManager = warehouseManager;
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Source Warehouse Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Source Warehouse:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        sourceWarehouseCombo = new JComboBox<>();
        for (String warehouseName : warehouseMap.keySet()) {
            sourceWarehouseCombo.addItem(warehouseName);
        }
        
        // Set default source warehouse to current warehouse if available, otherwise first warehouse
        String defaultSource = null;
        Warehouse currentWarehouse = warehouseManager.getCurrent();
        if (currentWarehouse != null) {
            // Find the name of the current warehouse
            for (Map.Entry<String, Warehouse> entry : warehouseMap.entrySet()) {
                if (entry.getValue() == currentWarehouse) {
                    defaultSource = entry.getKey();
                    break;
                }
            }
        }
        
        if (defaultSource != null) {
            sourceWarehouseCombo.setSelectedItem(defaultSource);
        } else if (sourceWarehouseCombo.getItemCount() > 0) {
            sourceWarehouseCombo.setSelectedIndex(0);
        }
        
        sourceWarehouseCombo.addActionListener(e -> {
            updateRecipientWarehouseCombo();
            updateItemsTable();
        });
        mainPanel.add(sourceWarehouseCombo, gbc);
        
        // Recipient Warehouse Selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Recipient Warehouse:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        recipientWarehouseCombo = new JComboBox<>();
        updateRecipientWarehouseCombo();
        mainPanel.add(recipientWarehouseCombo, gbc);
        
        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Search Items:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
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
        mainPanel.add(searchField, gbc);
        
        // Items Table
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        tableModel = new DefaultTableModel(new String[]{"Select", "SKU", "Item Name", "Stock", "Transfer Qty", "Expiration Date"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                if (column == 4) return Integer.class;
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 4; // Checkbox and quantity columns are editable
            }
        };
        
        itemsTable = new JTable(tableModel);
        itemsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        mainPanel.add(scrollPane, gbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        transferButton = new JButton("Transfer Selected");
        cancelButton = new JButton("Cancel");
        
        transferButton.addActionListener(e -> onTransferClicked());
        cancelButton.addActionListener(e -> onCancelClicked());
        
        buttonPanel.add(transferButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
        
        // Populate items table with initial warehouse selection
        updateItemsTable();
    }
    
    private void updateRecipientWarehouseCombo() {
        String currentSource = (String) sourceWarehouseCombo.getSelectedItem();
        recipientWarehouseCombo.removeAllItems();
        
        for (String warehouseName : warehouseMap.keySet()) {
            if (!warehouseName.equals(currentSource)) {
                recipientWarehouseCombo.addItem(warehouseName);
            }
        }
    }
    
    private void scheduleSearch() {
        if (searchTimer != null) {
            searchTimer.stop();
        }
        searchTimer = new javax.swing.Timer(300, e -> updateItemsTable());
        searchTimer.setRepeats(false);
        searchTimer.start();
    }
    
    private void updateItemsTable() {
        tableModel.setRowCount(0);
        
        String selectedSourceWarehouse = (String) sourceWarehouseCombo.getSelectedItem();
        if (selectedSourceWarehouse == null) {
            return;
        }
        
        Warehouse warehouse = warehouseMap.get(selectedSourceWarehouse);
        if (warehouse == null) {
            return;
        }
        
        String searchText = searchField.getText().trim().toLowerCase();
        Map<Integer, List<Item>> itemsByID = warehouse.getItemsByID();
        
        for (Map.Entry<Integer, List<Item>> entry : itemsByID.entrySet()) {
            int sku = entry.getKey();
            List<Item> items = entry.getValue();
            
            if (items == null || items.isEmpty()) continue;
            
            String itemName = warehouse.getItemName(sku);
            String searchQuery = searchText;
            
            // Search logic: keywords -> names -> IDs
            boolean matches = false;
            
            if (searchQuery.isEmpty()) {
                matches = true;
            } else {
                // Check keywords
                String[] keywords = warehouse.getItemKeywords(sku);
                for (String keyword : keywords) {
                    if (keyword.toLowerCase().contains(searchQuery)) {
                        matches = true;
                        break;
                    }
                }
                
                // Check name
                if (!matches && itemName.toLowerCase().contains(searchQuery)) {
                    matches = true;
                }
                
                // Check SKU
                if (!matches && String.valueOf(sku).contains(searchQuery)) {
                    matches = true;
                }
            }
            
            if (matches) {
                // Calculate total stock across all instances with this SKU
                int totalStock = 0;
                String expirationText = "N/A";
                for (Item item : items) {
                    if (item != null) {
                        totalStock += item.getStock();
                        // Get expiration from first perishable item
                        if (item instanceof PerishableItem && expirationText.equals("N/A")) {
                            expirationText = ((PerishableItem) item).getExpr().toString();
                        }
                    }
                }
                
                tableModel.addRow(new Object[]{
                    false,
                    sku,
                    itemName,
                    totalStock,
                    totalStock,  // Default transfer quantity is total stock
                    expirationText
                });
            }
        }
    }
    
    private void onTransferClicked() {
        String sourceWarehouse = (String) sourceWarehouseCombo.getSelectedItem();
        String recipientWarehouse = (String) recipientWarehouseCombo.getSelectedItem();
        
        if (sourceWarehouse == null || recipientWarehouse == null) {
            JOptionPane.showMessageDialog(this, "Please select both source and recipient warehouses.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (sourceWarehouse.equals(recipientWarehouse)) {
            JOptionPane.showMessageDialog(this, "Source and recipient warehouses must be different.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Collect items to transfer organized by SKU with quantities
        java.util.Map<Integer, Integer> skuToQuantity = new java.util.HashMap<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                int sku = (Integer) tableModel.getValueAt(i, 1);
                int quantity = ((Number) tableModel.getValueAt(i, 4)).intValue();
                
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Transfer quantity must be greater than 0.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int totalStock = ((Number) tableModel.getValueAt(i, 3)).intValue();
                if (quantity > totalStock) {
                    JOptionPane.showMessageDialog(this, "Cannot transfer more than available stock for SKU " + sku, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                skuToQuantity.put(sku, quantity);
            }
        }
        
        if (skuToQuantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select items to transfer.", "No Items Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Warehouse sourceWh = warehouseMap.get(sourceWarehouse);
        
        int transferredQty = 0;
        Set<Integer> instancesToTransfer = new java.util.HashSet<>();
        
        // For each SKU, prepare instances to transfer
        for (java.util.Map.Entry<Integer, Integer> entry : skuToQuantity.entrySet()) {
            int sku = entry.getKey();
            int quantityToTransfer = entry.getValue();
            
            // Get instance IDs for this SKU
            Set<Integer> instanceIDs = getInstanceIDsForSKU(sourceWh, sku);
            if (instanceIDs == null) continue;
            
            // For each instance, handle full or partial transfers
            int remainingQty = quantityToTransfer;
            java.util.List<Integer> instanceList = new java.util.ArrayList<>(instanceIDs);
            
            for (int instanceID : instanceList) {
                if (remainingQty <= 0) break;
                
                Item item = getItemByInstanceID(sourceWh, instanceID);
                if (item == null) continue;
                
                int itemStock = item.getStock();
                int qtyFromThisInstance = Math.min(remainingQty, itemStock);
                
                if (qtyFromThisInstance == itemStock) {
                    // Transfer the entire instance
                    instancesToTransfer.add(instanceID);
                    transferredQty += qtyFromThisInstance;
                } else {
                    // Split instance: keep part in source, transfer part
                    sourceWh.splitInstance(instanceID, qtyFromThisInstance);
                    // After splitInstance with qtyFromThisInstance, the original instance is reduced to (itemStock - qtyFromThisInstance)
                    // and a new instance is created with qtyFromThisInstance to transfer
                    Set<Integer> currentInstances = getInstanceIDsForSKU(sourceWh, sku);
                    int newInstanceID = -1;
                    for (int id : currentInstances) {
                        if (!instanceList.contains(id)) {
                            newInstanceID = id;
                            break;
                        }
                    }
                    if (newInstanceID != -1) {
                        instancesToTransfer.add(newInstanceID);
                        transferredQty += qtyFromThisInstance;
                    }
                }
                
                remainingQty -= qtyFromThisInstance;
            }
        }
        
        if (instancesToTransfer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items to transfer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Use WarehouseManager to perform the trade
        warehouseManager.setCurrent(sourceWarehouse);
        warehouseManager.tradeItems(recipientWarehouse, instancesToTransfer, new java.util.HashSet<>());
        
        confirmed = true;
        JOptionPane.showMessageDialog(this, transferredQty + " unit(s) transferred successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private Item getItemByInstanceID(Warehouse warehouse, int instanceID) {
        try {
            java.lang.reflect.Field itemsByChronoField = Warehouse.class.getDeclaredField("itemsByChrono");
            itemsByChronoField.setAccessible(true);
            java.util.List<Item> itemsByChrono = (java.util.List<Item>) itemsByChronoField.get(warehouse);
            
            if (instanceID >= 0 && instanceID < itemsByChrono.size()) {
                return itemsByChrono.get(instanceID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private int[] getItemPrices(Warehouse warehouse, int sku) {
        try {
            java.lang.reflect.Field stockedIDsField = Warehouse.class.getDeclaredField("stockedIDs");
            stockedIDsField.setAccessible(true);
            java.util.Map<Integer, LocalItemID> stockedIDs = (java.util.Map<Integer, LocalItemID>) stockedIDsField.get(warehouse);
            
            if (stockedIDs.containsKey(sku)) {
                ItemID itemID = stockedIDs.get(sku).getReference();
                return new int[]{itemID.getSellPrice(), itemID.getBuyPrice()};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }
    
    private java.util.Set<Integer> getInstanceIDsForSKU(Warehouse warehouse, int sku) {
        try {
            java.lang.reflect.Field itemsBySKUField = Warehouse.class.getDeclaredField("itemsBySKU");
            itemsBySKUField.setAccessible(true);
            java.util.Map<Integer, java.util.Set<Integer>> itemsBySKU = (java.util.Map<Integer, java.util.Set<Integer>>) itemsBySKUField.get(warehouse);
            
            return itemsBySKU.get(sku);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void onCancelClicked() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
