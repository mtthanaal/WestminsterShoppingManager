import java.util.List;

// Interface representing a shopping manager responsible for managing products and shopping carts
interface ShoppingManager {
    // Adds a product to the list of available products
    void addProduct(Product product);

    // Deletes a product with the specified product ID
    void deleteProduct(String productId);

    // Prints information about all products
    void printProducts();

    // Saves the current state of products (e.g., to a file or database)
    void saveProducts();

    // Retrieves a list of products filtered by the specified product type
    List<Product> getFilteredProducts(String productType);

    // Adds a product to the shopping cart
    void addToShoppingCart(Product product);

    // Retrieves the shopping cart associated with the shopping manager
    ShoppingCart getShoppingCart();
}
