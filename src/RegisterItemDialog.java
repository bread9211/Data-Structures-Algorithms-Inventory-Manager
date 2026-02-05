import javax.swing.*;
import java.awt.*;

public class RegisterItemDialog extends JDialog {
    private JTextField itemNameField;
    private JTextField sellPriceField;
    private JTextField buyPriceField;
    private JTextField keywordsField;
    private JButton registerButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private int itemID;
    private Warehouse warehouse;
    
    public RegisterItemDialog(JFrame parent, int itemID, Warehouse warehouse) {
        super(parent, "Register New Item", true);
        this.itemID = itemID;
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
        
        // Item ID Label (display only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Item ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel idLabel = new JLabel(String.valueOf(itemID));
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(idLabel, gbc);
        
        // Item Name Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Item Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        itemNameField = new JTextField();
        mainPanel.add(itemNameField, gbc);
        
        // Sell Price Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Sell Price:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        sellPriceField = new JTextField();
        mainPanel.add(sellPriceField, gbc);
        
        // Buy Price Label and Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Buy Price:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        buyPriceField = new JTextField();
        mainPanel.add(buyPriceField, gbc);
        
        // Keywords Label and Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Keywords (comma-separated):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 0.5;
        keywordsField = new JTextField();
        mainPanel.add(keywordsField, gbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
        
        registerButton.addActionListener(e -> onRegisterClicked());
        cancelButton.addActionListener(e -> onCancelClicked());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weighty = 0;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void onRegisterClicked() {
        try {
            // Validate all fields
            String itemName = itemNameField.getText().trim();
            if (itemName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an item name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int sellPrice = Integer.parseInt(sellPriceField.getText().trim());
            int buyPrice = Integer.parseInt(buyPriceField.getText().trim());
            
            if (sellPrice <= 0 || buyPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Prices must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse keywords
            String keywordsText = keywordsField.getText().trim();
            String[] keywords = keywordsText.isEmpty() ? new String[0] : keywordsText.split(",\\s*");
            
            // Create new ItemID and add to warehouse
            ItemID newItemID = new ItemID(itemName, itemID, sellPrice, buyPrice, keywords);
            
            // Register the item in the warehouse using reflection to add to stockedIDs map
            try {
                java.lang.reflect.Field stockedIDsField = Warehouse.class.getDeclaredField("stockedIDs");
                stockedIDsField.setAccessible(true);
                java.util.Map<Integer, LocalItemID> stockedIDs = (java.util.Map<Integer, LocalItemID>) stockedIDsField.get(warehouse);
                
                // Create a LocalItemID wrapper with the new ItemID
                LocalItemID localItemID = new LocalItemID(newItemID, 0);
                stockedIDs.put(itemID, localItemID);
                
                // Also add an Item instance to the warehouse's itemsByID map so it can be found in search
                Item newItem = new Item(itemID, 1, null);  // Create with stock of 1
                warehouse.addItem(newItem, newItemID);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error registering item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for prices.", "Error", JOptionPane.ERROR_MESSAGE);
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
