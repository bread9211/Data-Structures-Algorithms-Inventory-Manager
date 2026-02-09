import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ExportDialog extends JDialog {
    private JPanel checkboxPanel;
    private Map<String, JCheckBox> warehouseCheckboxes;
    private JButton exportButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private List<String> selectedWarehouses;
    private WarehouseManager warehouseManager;

    public ExportDialog(JFrame parent, WarehouseManager warehouseManager) {
        super(parent, "Select Warehouses to Export", true);
        this.warehouseManager = warehouseManager;
        this.warehouseCheckboxes = new HashMap<>();
        this.selectedWarehouses = new ArrayList<>();
        
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title label
        JLabel titleLabel = new JLabel("Select warehouses to export:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Scrollable checkbox panel for warehouses
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Get all warehouse names and create checkboxes
        List<String> warehouseNames = warehouseManager.getAllWarehouseNames();
        for (String warehouseName : warehouseNames) {
            JCheckBox checkbox = new JCheckBox(warehouseName);
            warehouseCheckboxes.put(warehouseName, checkbox);
            checkboxPanel.add(checkbox);
        }
        
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setPreferredSize(new Dimension(360, 250));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        exportButton = new JButton("Export");
        exportButton.addActionListener(e -> onExportClicked());
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancelClicked());
        
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private void onExportClicked() {
        selectedWarehouses.clear();
        
        for (Map.Entry<String, JCheckBox> entry : warehouseCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedWarehouses.add(entry.getKey());
            }
        }
        
        // Validate: at least one but not all warehouses selected
        if (selectedWarehouses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one warehouse.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (selectedWarehouses.size() == warehouseCheckboxes.size()) {
            JOptionPane.showMessageDialog(this, "Please select fewer than all warehouses.", "All Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        confirmed = true;
        dispose();
    }

    private void onCancelClicked() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<String> getSelectedWarehouses() {
        return selectedWarehouses;
    }
}
