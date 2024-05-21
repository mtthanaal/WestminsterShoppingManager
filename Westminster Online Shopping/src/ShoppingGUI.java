import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Map;

// Main class for the Westminster Online Shopping GUI
public class ShoppingGUI extends JFrame {
    private final WestminsterShoppingManager shoppingManager;
    private final ShoppingCartGUI shoppingCartGUI;
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;

    // Constructor for the main shopping GUI
    public ShoppingGUI(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
        this.shoppingCartGUI = new ShoppingCartGUI();

        setTitle("  Westminster Online Shopping  ");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponents();
    }

    // Method to create GUI components
    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Initialize components
        productTypeComboBox = new JComboBox<>(new String[]{" All ", "  Electronics  ", "  Clothing  "});
        JButton addToCartButton = new JButton("  Add to Cart  ");
        JButton viewCartButton = new JButton("  View Shopping Cart  ");
        productDetailsTextArea = new JTextArea();
        productDetailsTextArea.setEditable(false);
        productTable = new JTable(new DefaultTableModel()) {
            // Override to set cell renderer properties
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

        // Set the header renderer to make the headings bold
        JTableHeader header = productTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));

        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Set margins for the JScrollPane
        int marginSize = 10;
        tableScrollPane.setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));

        // Add an ItemListener to the productTypeComboBox
        productTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                displayProducts();
            }
        });

        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> shoppingCartGUI.setVisible(true));

        productTable.getSelectionModel().addListSelectionListener(e -> displayProductDetails());

        // Layout setup
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("  Product Type:   "));
        topPanel.add(productTypeComboBox);
        topPanel.add(addToCartButton);
        topPanel.add(viewCartButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(productDetailsTextArea, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        displayProducts();  // Initial product display
    }

    // Method to display products based on the selected product type
    private void displayProducts() {
        String selectedProductType = (String) productTypeComboBox.getSelectedItem();
        List<Product> filteredProducts = getFilteredProducts(selectedProductType);

        DefaultTableModel model = new DefaultTableModel() {
            // Override to set column class and editability
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

        model.addColumn("  Product ID  ");
        model.addColumn("  Name  ");
        model.addColumn("  Category  ");
        model.addColumn("  Price  ");
        model.addColumn("  Info  ");

        // Populate the table with product data
        for (Product product : filteredProducts) {
            if (product instanceof Electronics) {
                model.addRow(new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        "  Electronics  ",
                        product.getPrice(),
                        new ProductInfo(((Electronics) product).getBrand(), ((Electronics) product).getWarrantyPeriod(), "  Electronics  ")
                });
            } else if (product instanceof Clothing) {
                model.addRow(new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        "  Clothing  ",
                        product.getPrice(),
                        new ProductInfo(((Clothing) product).getSize(), ((Clothing) product).getColor(), "  Clothing  ")
                });
            }
        }

        productTable.setModel(model);

        // Set the width of the columns
        setColumnWidth(0, 150);  // Product ID
        setColumnWidth(1, 200);  // Name
        setColumnWidth(2, 150);  // Category
        setColumnWidth(3, 100);   // Price
        setColumnWidth(4, 200);   // Info

        highlightLowAvailabilityProducts();
    }

    // Helper method to set the width of a column
    private void setColumnWidth(int columnIndex, int width) {
        TableColumn column = productTable.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(width);
        column.setMaxWidth(width + 50);  // Slightly bigger than min width
    }

    // Inner class representing additional information for a product
    private static class ProductInfo {
        private final String attribute1;
        private final String attribute2;

        public ProductInfo(String attribute1, Object attribute2, String productType) {
            this.attribute1 = attribute1;
            this.attribute2 = attribute2 + ("  Electronics  ".equals(productType) ? " Warranty (Years)" : "");
        }

        @Override
        public String toString() {
            return attribute1 + ", " + attribute2;
        }
    }

    // Method to get filtered products based on the selected product type
    private List<Product> getFilteredProducts(String productType) {
        if ("  Electronics  ".equals(productType)) {
            return shoppingManager.getElectronicsProducts();
        } else if ("  Clothing  ".equals(productType)) {
            return shoppingManager.getClothingProducts();
        } else {
            return shoppingManager.getProductList();
        }
    }

    // Method to highlight low availability products in the table
    private void highlightLowAvailabilityProducts() {
        int columnCount = productTable.getColumnCount();
        int rowCount = productTable.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            Object availableItemsObj = productTable.getValueAt(row, 2);

            if (availableItemsObj instanceof Integer) {
                int availableItems = (int) availableItemsObj;

                if (availableItems < 3) {
                    for (int col = 0; col < columnCount; col++) {
                        productTable.getComponentAt(row, col).setForeground(Color.RED);
                    }
                }
            } else {
                // Handle other cases if needed
            }
        }
    }

    // Method to display detailed product information in the text area
    private void displayProductDetails() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = shoppingManager.getProductById(productId);

            // Display detailed information in productDetailsTextArea
            productDetailsTextArea.setText("  \n        Selected Product - Details  \n"
                    + "        *******************************  \n"
                    + "        Product ID:   " + selectedProduct.getProductId() +   "\n"
                    + "        Category:   " + (selectedProduct instanceof Electronics ? "  Electronics" : "Clothing  ") +  "\n"
                    + "        Name:    " + selectedProduct.getProductName() +   " \n"
                    + (selectedProduct instanceof Clothing ?
                    ("        Size:  " + ((Clothing) selectedProduct).getSize() +  "\n" +
                            "        Color:  " + ((Clothing) selectedProduct).getColor() +  "\n") :
                    "")
                    + "        Items Available:  " + selectedProduct.getAvailableItems());
        }
    }

    // Method to add the selected product to the shopping cart
    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = shoppingManager.getProductById(productId);
            shoppingCartGUI.addProductToCart(selectedProduct);
        }
    }
}

// Class representing the shopping cart GUI
class ShoppingCartGUI extends JFrame {
    private final List<Product> cartItems;
    private final Map<String, Integer> userPurchaseHistory;

    private JTextArea cartDetailsTextArea;

    // Constructor for the shopping cart GUI
    public ShoppingCartGUI() {
        this.cartItems = new ArrayList<>();
        this.userPurchaseHistory = new HashMap<>();
        setTitle("  Shopping Cart  ");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        createComponents();
    }

    // Method to create GUI components for the shopping cart
    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        cartDetailsTextArea = new JTextArea();
        cartDetailsTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(cartDetailsTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    // Method to add a product to the shopping cart
    public void addProductToCart(Product product) {
        cartItems.add(product);
        updateCartDetails();
    }

    // Method to update and display the shopping cart details
    private void updateCartDetails() {
        StringBuilder cartInfo = new StringBuilder();
        double totalCost = 0;

        for (Product product : cartItems) {
            cartInfo.append(product.toString()).append("\n");
            totalCost += product.getPrice();
        }

        // Apply discounts
        totalCost = applyDiscounts(totalCost);
        cartInfo.append("\n Total Cost: Rupees  ").append(totalCost);
        cartDetailsTextArea.setText(cartInfo.toString());
        setVisible(true);
    }

    // Method to apply discounts based on user purchase history
    private double applyDiscounts(double totalCost) {
        String currentUser = "  sample_user  ";

        if (!userPurchaseHistory.containsKey(currentUser)) {
            // First purchase discount
            totalCost *= 0.9;
            userPurchaseHistory.put(currentUser, 1);
        } else {
            // Subsequent purchases
            int purchaseCount = userPurchaseHistory.get(currentUser);
            if (purchaseCount >= 3) {
                // Category discount
                totalCost *= 0.8;
            }
            userPurchaseHistory.put(currentUser, purchaseCount + 1);
        }

        return totalCost;
    }
}
