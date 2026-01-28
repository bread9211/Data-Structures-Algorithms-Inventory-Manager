import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.*;
import java.util.*;
public class UserGUI {
    private JFrame frame;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel tablePanel;
    private JPanel actionPanel;
    
    // Header components
    private JLabel titleLabel;
    private JComboBox<String> warehouseSelector;
    
    // WarehouseManager reference
    private WarehouseManager warehouseManager;
    private Map<String, Warehouse> warehouseMap;
    
    // Action buttons
    private JButton addItemButton;
    private JButton sellItemButton;
    private JButton transferItemButton;
    private JButton searchButton;
    private JButton sortButton;
    private JButton themeSwitchButton;
    
    // Search/filter components
    private JTextField searchField;

    // State variables
    private boolean isDarkMode = false;
    
    public UserGUI(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
        this.warehouseMap = new HashMap<>();
        FlatLightLaf.setup();

        initializeFrame();
        createHeaderPanel();
        createTablePanel();
        createActionPanel();
        setupLayout();
    }
    
    private void initializeFrame() {
        frame = new JFrame("WarehouseManager - Inventory Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        frame.add(mainPanel);
    }
    
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Warehouse Selection"));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);
        titleLabel = new JLabel("Current Warehouse:");
        warehouseSelector = new JComboBox<>();
        warehouseSelector.addItem("Select a Warehouse...");
        leftPanel.add(titleLabel);
        leftPanel.add(warehouseSelector);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        rightPanel.setOpaque(false);
        themeSwitchButton = new JButton("Dark Mode");
        themeSwitchButton.setFocusPainted(false);
        themeSwitchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        rightPanel.add(themeSwitchButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        themeSwitchButton.addActionListener(e -> toggleTheme());
        warehouseSelector.addActionListener(e -> onWarehouseSelected());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void onWarehouseSelected() {
        String selectedWarehouse = (String) warehouseSelector.getSelectedItem();
        if (selectedWarehouse != null && !selectedWarehouse.equals("Select a Warehouse...")) {
            loadWarehouseInventory(selectedWarehouse);
        } else {
            clearTable();
        }
    }
    
    private void loadWarehouseInventory(String warehouseName) {
        clearTable();
        // Items will be added when the warehouse is populated with data
        // The warehouse dropdown selection is now linked and ready to display items
    }

    private void toggleTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                themeSwitchButton.setText("Dark Mode");
            } else {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                themeSwitchButton.setText("Light Mode");
            }
            isDarkMode = !isDarkMode;
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Inventory List"));
        
        // Create table with columns: Item ID, Item Name, Quantity, Unit Price, Expiration Date
        String[] columnNames = {"Item ID", "Item Name", "Quantity", "Unit Price", "Expiration Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable = new JTable(tableModel);
        
        // Make table read only
        inventoryTable.setDefaultEditor(Object.class, null);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.getTableHeader().setReorderingAllowed(false);
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }
    
    private void createActionPanel() {
        actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout(10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions & Search"));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        addItemButton = new JButton("Add Item");
        sellItemButton = new JButton("Sell Item");
        transferItemButton = new JButton("Transfer Item");
        sortButton = new JButton("Sort By");
        
        buttonPanel.add(addItemButton);
        buttonPanel.add(sellItemButton);
        buttonPanel.add(transferItemButton);
        buttonPanel.add(sortButton);
        
        actionPanel.add(searchPanel, BorderLayout.NORTH);
        actionPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        frame.setVisible(true);
    }
    
    // Method to add items to the table
    public void addInventoryRow(String itemID, String itemName, int quantity, double unitPrice, String expirationDate, String status) {
        tableModel.addRow(new Object[]{itemID, itemName, quantity, unitPrice, expirationDate, status});
    }
    
    // Method to clear the table
    public void clearTable() {
        tableModel.setRowCount(0);
    }
    
    // Method to get the selected row data
    public int getSelectedRow() {
        return inventoryTable.getSelectedRow();
    }
    
    // Method to add warehouse to dropdown and store reference
    public void addWarehouse(String warehouseName, Warehouse warehouse) {
        warehouseSelector.addItem(warehouseName);
        warehouseMap.put(warehouseName, warehouse);
    }
    
    // Legacy method for backwards compatibility
    public void addWarehouse(String warehouseName) {
        warehouseSelector.addItem(warehouseName);
    }
    
    // Method to add action listeners
    public void addWarehouseSelectorListener(ActionListener listener) {
        warehouseSelector.addActionListener(listener);
    }
    
    public void addAddItemListener(ActionListener listener) {
        addItemButton.addActionListener(listener);
    }
    
    public void addSellItemListener(ActionListener listener) {
        sellItemButton.addActionListener(listener);
    }
    
    public void addTransferItemListener(ActionListener listener) {
        transferItemButton.addActionListener(listener);
    }
    
    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    
    public void addSortListener(ActionListener listener) {
        sortButton.addActionListener(listener);
    }
    
    // Getter methods
    public String getSearchQuery() {
        return searchField.getText();
    }
    
    public String getSelectedWarehouse() {
        Object selected = warehouseSelector.getSelectedItem();
        return selected != null ? selected.toString() : null;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public WarehouseManager getWarehouseManager() {
        return warehouseManager;
    }
}
