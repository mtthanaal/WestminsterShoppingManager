import javax.swing.*;
import java.awt.*;
import java.util.List;

class OnlineShoppingCartGUI extends JFrame {
    private List<Product> cartItems;

    private JTextArea cartDetailsTextArea;

    public OnlineShoppingCartGUI() {
        this.cartItems = new java.util.ArrayList<>();

        setTitle("  Westminster Online Shopping  ");
        setSize(200, 350);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        createComponents();
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        cartDetailsTextArea = new JTextArea();
        cartDetailsTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(cartDetailsTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public void addProductToCart(Product product) {
        cartItems.add(product);
        updateCartDetails();
    }

    private void updateCartDetails() {
        StringBuilder cartInfo = new StringBuilder();
        double totalCost = 0;

        for (Product product : cartItems) {
            cartInfo.append(product.toString()).append(" \n");
            totalCost += product.getPrice();
        }

        // Apply discounts
        totalCost = applyDiscounts(totalCost);

        cartInfo.append(" \n Total Cost: Rupees").append(totalCost);
        cartDetailsTextArea.setText(cartInfo.toString());
        setVisible(true);
    }

    private double applyDiscounts(double totalCost) {
        return totalCost;
    }
}

