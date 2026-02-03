import javax.swing.*;
import java.awt.*;

public class SortDialog extends JDialog {
    private JComboBox<String> sortModeCombo;
    private JButton applyButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private String selectedSortMode = "Warehouse";
    
    public SortDialog(JFrame parent, String currentSortMode) {
        super(parent, "Sort Options", true);
        this.selectedSortMode = currentSortMode;
        setSize(400, 200);
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
        
        // Sort Mode Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Sort By:"), gbc);
        
        // Sort Mode Combo Box
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] sortModes = {"Warehouse", "ID", "Name", "Expiration Date"};
        sortModeCombo = new JComboBox<>(sortModes);
        sortModeCombo.setSelectedItem(selectedSortMode);
        mainPanel.add(sortModeCombo, gbc);
        
        // Button Panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> onApplyClicked());
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancelClicked());
        
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void onApplyClicked() {
        selectedSortMode = (String) sortModeCombo.getSelectedItem();
        confirmed = true;
        setVisible(false);
    }
    
    private void onCancelClicked() {
        confirmed = false;
        setVisible(false);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getSelectedSortMode() {
        return selectedSortMode;
    }
}
