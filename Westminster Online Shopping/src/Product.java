import java.io.Serializable;

// Abstract class representing a general product
abstract class Product implements Serializable {
    private String productId; // Unique identifier for the product
    private String productName; // Name of the product
    private int availableItems; // Number of available items in stock
    private double price; // Price of the product

    // Constructor to initialize a Product object
    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }

    // Getter method for productId
    public String getProductId() {
        return productId;
    }

    // Getter method for productName
    public String getProductName() {
        return productName;
    }

    // Getter method for availableItems
    public int getAvailableItems() {
        return availableItems;
    }

    // Getter method for price
    public double getPrice() {
        return price;
    }

    // Abstract method to get the type of the product (to be implemented by subclasses)
    public abstract String getType();

    // Abstract method to display detailed information about the product (to be implemented by subclasses)
    public abstract void displayInfo();

    // Override the toString method to provide a string representation of the product
    @Override
    public String toString() {
        return "  Product ID:   " + productId +
                "  \nProduct Name:   " + productName +
                "  \nAvailable Items:   " + availableItems +
                "  \nPrice: Rupees  " + price;
    }
}
