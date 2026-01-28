import javax.swing.*;
import java.awt.*;

public class AddItemDialog extends JDialog {
    private JTextField itemIDField;
    private JTextField quantityField;
    private JButton addButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private int itemID;
    private int quantity;
    
    public AddItemDialog(JFrame parent) {
        super(parent, "Add Item to Warehouse", true);
        setSize(400, 250);
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
        mainPanel.add(itemIDField, gbc);
        
        // Quantity Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void onAddClicked() {
        try {
            if (itemIDField.getText().trim().isEmpty() || quantityField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            itemID = Integer.parseInt(itemIDField.getText().trim());
            quantity = Integer.parseInt(quantityField.getText().trim());
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
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
    
    public int getItemID() {
        return itemID;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
