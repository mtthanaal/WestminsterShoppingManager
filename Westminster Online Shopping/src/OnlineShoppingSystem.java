import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

// The class represents the graphical user interface for the online shopping system.
public class OnlineShoppingSystem extends JFrame {
    private WestminsterShoppingManager shoppingManager;
    private ShoppingCartGUI shoppingCartGUI;

    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;

    // Constructor initializes the online shopping system with a reference to the shopping manager.
    public OnlineShoppingSystem(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
        this.shoppingCartGUI = new ShoppingCartGUI();

        setTitle(" Shopping GUI ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponents();
    }
    // Method to create the "Add to Cart" panel.
    private JPanel createAddToCartPanel() {
        JPanel addToCartPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");

        addToCartButton.addActionListener(e -> addToCart());

        addToCartPanel.add(addToCartButton);

        return addToCartPanel;
    }

    // Method to create and set up GUI components.
    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        JButton shoppingCartButton = new JButton("Shopping Cart");

        productDetailsTextArea = new JTextArea();
        productDetailsTextArea.setEditable(false);
        productTable = new JTable(new DefaultTableModel()) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setHorizontalAlignment(SwingConstants.CENTER);

                if (row == 0) {
                    // Bold for the header row
                    renderer.setFont(renderer.getFont().deriveFont(Font.BOLD));
                }

                return renderer;
            }
        };

        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) productTable.getModel());
        productTable.setRowSorter(sorter);

        // Set the header renderer to make the headings bold
        JTableHeader header = productTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));

        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Set margins for the JScrollPane
        int marginSize = 10; // You can adjust this value
        tableScrollPane.setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));

        // Add an ItemListener to the productTypeComboBox
        productTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                displayProducts();
            }
        });

        addToCartButton.addActionListener(e -> addToCart());

        shoppingCartButton.addActionListener(e -> shoppingCartGUI.setVisible(true));

        productTable.getSelectionModel().addListSelectionListener(e -> displayProductDetails());

        // Layout setup
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Product Type: "));
        topPanel.add(productTypeComboBox);
        topPanel.add(addToCartButton);
        topPanel.add(shoppingCartButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(productDetailsTextArea, BorderLayout.CENTER);
        mainPanel.add(createAddToCartPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
        displayProducts();  // Initial product display
    }

    // Method to display products based on the selected product type.
    private void displayProducts() {
        String selectedProductType = (String) productTypeComboBox.getSelectedItem();
        List<Product> filteredProducts = getFilteredProducts(selectedProductType);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return ProductInfo.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Product ID");
        model.addColumn("Name");
        model.addColumn("Category");
        model.addColumn("Price");
        model.addColumn("Info");

        for (Product product : filteredProducts) {
            if (product instanceof Electronics) {
                model.addRow(new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        "Electronics",
                        product.getPrice(),
                        new ProductInfo(((Electronics) product).getBrand(), ((Electronics) product).getWarrantyPeriod(), "Electronics")
                });
            } else if (product instanceof Clothing) {
                model.addRow(new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        "Clothing",
                        product.getPrice(),
                        new ProductInfo(((Clothing) product).getSize(), ((Clothing) product).getColor(), "Clothing")
                });
            }
        }

        productTable.setModel(model);

        // Set the width of the columns
        setColumnWidth(0, 150);  // Product ID
        setColumnWidth(1, 200);  // Name
        setColumnWidth(2, 150);  // Category
        setColumnWidth(3, 100);  // Price
        setColumnWidth(4, 200);  // Info

        highlightLowAvailabilityProducts();
    }

    // Helper method to set the width of a column
    private void setColumnWidth(int columnIndex, int width) {
        TableColumn column = productTable.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(width);
        column.setMaxWidth(width + 50);  // Slightly bigger than min width
    }

    // Nested class to represent additional information for products.
    private static class ProductInfo {
        private String attribute1;
        private String attribute2;

        public ProductInfo(String attribute1, Object attribute2, String productType) {
            this.attribute1 = attribute1;
            this.attribute2 = String.valueOf(attribute2) + ("Electronics".equals(productType) ? " months warranty(Years)" : "");
        }

        @Override
        public String toString() {
            return attribute1 + " , " + attribute2;
        }
    }

    // Method to get a list of filtered products based on the product type.
    private List<Product> getFilteredProducts(String productType) {
        if ("Electronics".equals(productType)) {
            return shoppingManager.getElectronicsProducts();
        } else if ("Clothing".equals(productType)) {
            return shoppingManager.getClothingProducts();
        } else {
            return shoppingManager.getProductList();
        }
    }

    // Method to highlight low-availability products in the table.
    private void highlightLowAvailabilityProducts() {
        int columnCount = productTable.getColumnCount();
        int rowCount = productTable.getRowCount();

        RowSorter<?> rowSorter = productTable.getRowSorter();
        int[] sortedIndices = new int[rowCount];

        for (int i = 0; i < rowCount; i++) {
            sortedIndices[i] = rowSorter.convertRowIndexToModel(i);
        }

        for (int i = 0; i < rowCount; i++) {
            int row = (sortedIndices == null || sortedIndices.length == 0) ? i : sortedIndices[i];
            Object availableItemsObj = productTable.getValueAt(row, 3);

            if (availableItemsObj instanceof Integer) {
                int availableItems = (int) availableItemsObj;

                if (availableItems < 3) {
                    for (int col = 0; col < columnCount; col++) {
                        productTable.setDefaultRenderer(Object.class, new LowAvailabilityCellRenderer());
                    }
                }
            }
        }
    }

    // Nested class for rendering low-availability cells with red text.
    private static class LowAvailabilityCellRenderer extends DefaultTableCellRenderer {
        private static final Color RED_COLOR = Color.RED;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Object availableItemsObj = table.getValueAt(row, 3);
            if (availableItemsObj instanceof Integer) {
                int availableItems = (int) availableItemsObj;
                if (availableItems < 3) {
                    setForeground(RED_COLOR);
                }
            }

            return this;
        }
    }

    // Method to display detailed information about the selected product.
    private void displayProductDetails() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = shoppingManager.getProductById(productId);

            // Display detailed information in productDetailsTextArea
            productDetailsTextArea.setText("Selected Product - Details [Bold]\n"
                    + "Product ID: " + selectedProduct.getProductId() + " \n "
                    + "Category: " + (selectedProduct instanceof Electronics ? "Electronics" : "Clothing") + " \n"
                    + "Name: " + selectedProduct.getProductName() + " \n"
                    + (selectedProduct instanceof Clothing ?
                    ("Size: " + ((Clothing) selectedProduct).getSize() + " \n" +
                            "Color: " + ((Clothing) selectedProduct).getColor() + " \n") :
                    " ")
                    + "Items Available: " + selectedProduct.getAvailableItems());
        }
    }

    // Method to add the selected product to the shopping cart.
    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = shoppingManager.getProductById(productId);
            shoppingCartGUI.addProductToCart(selectedProduct);
        }
    }

    // The main method to start the online shopping system.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
            OnlineShoppingSystem shoppingSystem = new OnlineShoppingSystem(shoppingManager);
            shoppingSystem.setVisible(true);
        });
    }
}
