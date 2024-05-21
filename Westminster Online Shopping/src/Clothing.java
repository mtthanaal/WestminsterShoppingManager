// Clothing class, extending the Product class
class Clothing extends Product {
    private String size; // Size of the clothing
    private String color; // Color of the clothing

    // Constructor to initialize Clothing object
    public Clothing(String productId, String productName, int availableItems, double price,
                    String size, String color) {
        // Call the constructor of the superclass (Product)
        super(productId, productName, availableItems, price);
        // Initialize Clothing-specific attributes
        this.size = size;
        this.color = color;
    }

    // Getter method for size
    public String getSize() {
        return size;
    }

    // Getter method for color
    public String getColor() {
        return color;
    }

    // Override the displayInfo method to provide clothing-specific information
    @Override
    public void displayInfo() {
        System.out.println("Clothing Information:");
        System.out.println(toString()); // Call the toString method of the superclass
        System.out.println("Size: " + size);
        System.out.println("Color: " + color);
    }

    // Override the getType method to specify the type as "Clothing"
    @Override
    public String getType() {
        return "Clothing";
    }
}
