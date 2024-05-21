// Electronics class, extending the Product class
class Electronics extends Product {
    private String brand; // Brand of the electronics product
    private int warrantyPeriod; // Warranty period of the electronics product

    // Constructor to initialize Electronics object
    public Electronics(String productId, String productName, int availableItems, double price,
                       String brand, int warrantyPeriod) {
        // Call the constructor of the superclass (Product)
        super(productId, productName, availableItems, price);
        // Initialize Electronics-specific attributes
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getter method for brand
    public String getBrand() {
        return brand;
    }

    // Getter method for warranty period
    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    // Override the displayInfo method to provide electronics-specific information
    @Override
    public void displayInfo() {
        System.out.println("  Electronics Information:  ");
        System.out.println(toString()); // Call the toString method of the superclass
        System.out.println("  Brand:   " + brand);
        System.out.println("  Warranty Period:   " + warrantyPeriod + " Years ");
    }

    // Override the getType method to specify the type as "Electronics"
    @Override
    public String getType() {
        return "  Electronics  ";
    }
}
