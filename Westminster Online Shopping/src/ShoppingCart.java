import java.util.ArrayList;
import java.util.List;

// Represents a shopping cart that can hold a list of products
class ShoppingCart {
    private List<Product> products; // List to store products in the shopping cart

    // Constructor to initialize a new shopping cart with an empty list of products
    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    // Method to add a product to the shopping cart
    public void addProduct(Product product) {
        products.add(product);
    }

    // Method to remove a product from the shopping cart
    public void removeProduct(Product product) {
        products.remove(product);
    }

    // Method to get the list of products in the shopping cart
    public List<Product> getProducts() {
        return products;
    }
}
