import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class AddItemDialog extends JDialog {
    private JTextField itemIDField;
    private JTextField quantityField;
    private JLabel itemNameLabel;
    private JButton addButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private Warehouse warehouse;
    private WarehouseManager warehouseManager;
    
    public AddItemDialog(JFrame parent, Warehouse warehouse, WarehouseManager warehouseManager) {
        super(parent, "Add Item to Warehouse", true);
        this.warehouse = warehouse;
        this.warehouseManager = warehouseManager;
        setSize(500, 300);
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
                updateItemName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateItemName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateItemName();
            }
        });
        mainPanel.add(itemIDField, gbc);
        
        // Item Name Label (display only)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Item Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        itemNameLabel = new JLabel("Unknown");
        itemNameLabel.setForeground(new Color(100, 100, 100));
        mainPanel.add(itemNameLabel, gbc);
        
        // Quantity Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
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
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
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
            
            // Search all warehouses for this item ID
            if (warehouseManager != null) {
                List<Item> items = warehouse.searchByID(itemID);
                if (!items.isEmpty()) {
                    itemNameLabel.setText("Item ID " + itemID + " (found in warehouse)");
                    itemNameLabel.setForeground(new Color(0, 100, 0));
                    return;
                }
            }
            
            itemNameLabel.setText("Unknown Item ID");
            itemNameLabel.setForeground(new Color(200, 100, 100));
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
            
            // Validate that item ID exists in any warehouse
            List<Item> items = warehouse.searchByID(itemID);
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Item ID " + itemID + " is not a known item.", "Invalid Item", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Add item to warehouse directly
            Item newItem = new Item(itemID, quantity, null);
            warehouse.addItem(newItem, itemID);
            
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
