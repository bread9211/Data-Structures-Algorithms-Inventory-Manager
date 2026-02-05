import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.sql.Date;

public class RemoveItemDialog extends JDialog {
    private JTextField instanceIDField;
    private JTextField quantityField;
    private JLabel itemNameLabel;
    private JLabel currentStockLabel;
    private JComboBox<String> actionComboBox;
    private JButton removeButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private Warehouse warehouse;
    private int selectedInstanceID = -1;
    
    public RemoveItemDialog(JFrame parent, Warehouse warehouse) {
        super(parent, "Remove/Sell Item", true);
        this.warehouse = warehouse;
        setSize(500, 350);
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
        
        // Instance ID Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Instance ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        instanceIDField = new JTextField();
        instanceIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateItemInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateItemInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateItemInfo();
            }
        });
        mainPanel.add(instanceIDField, gbc);
        
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
        
        // Current Stock Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Current Stock:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        currentStockLabel = new JLabel("0");
        currentStockLabel.setForeground(new Color(100, 100, 100));
        mainPanel.add(currentStockLabel, gbc);
        
        // Action Label and ComboBox
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Action:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        actionComboBox = new JComboBox<>(new String[]{"Reduce Quantity", "Remove Entire Item"});
        mainPanel.add(actionComboBox, gbc);
        
        // Quantity Label and Field (only for reduce quantity)
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void updateItemInfo() {
        String input = instanceIDField.getText().trim();
        
        try {
            int instanceID = Integer.parseInt(input);
            selectedInstanceID = instanceID;
            
            // Get item from warehouse by instance ID
            List<java.util.Map.Entry<Integer, java.util.List<Item>>> itemsList = 
                new java.util.ArrayList<>(warehouse.getItemsByID().entrySet());
            
            Item item = null;
            for (java.util.Map.Entry<Integer, java.util.List<Item>> entry : itemsList) {
                for (Item i : entry.getValue()) {
                    if (i != null && i.getSKU() == instanceID) {
                        item = i;
                        break;
                    }
                }
                if (item != null) break;
            }
            
            // If not found by SKU matching, try to get from itemsByChrono via reflection
            if (item == null) {
                try {
                    java.lang.reflect.Field field = Warehouse.class.getDeclaredField("itemsByChrono");
                    field.setAccessible(true);
                    java.util.List<Item> itemsByChrono = (java.util.List<Item>) field.get(warehouse);
                    if (instanceID >= 0 && instanceID < itemsByChrono.size()) {
                        item = itemsByChrono.get(instanceID);
                    }
                } catch (Exception e) {
                    // Reflection failed, item stays null
                }
            }
            
            if (item != null) {
                String itemName = warehouse.getItemName(item.getSKU());
                itemNameLabel.setText(itemName);
                currentStockLabel.setText(String.valueOf(item.getStock()));
            } else {
                itemNameLabel.setText("Item not found");
                currentStockLabel.setText("0");
            }
        } catch (NumberFormatException e) {
            itemNameLabel.setText("Invalid ID");
            currentStockLabel.setText("0");
            selectedInstanceID = -1;
        }
    }
    
    private void onRemoveClicked() {
        if (selectedInstanceID < 0) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Instance ID.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String action = (String) actionComboBox.getSelectedItem();
        
        if (action.equals("Remove Entire Item")) {
            // Remove the entire item instance
            Item removedItem = warehouse.removeItemInstance(selectedInstanceID);
            if (removedItem != null) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not remove item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Reduce quantity
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Item result = warehouse.removeItemQuantity(selectedInstanceID, quantity);
                if (result != null) {
                    confirmed = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not reduce quantity.", "Error", JOptionPane.ERROR_MESSAGE);
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
