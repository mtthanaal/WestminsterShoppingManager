import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;

// The class implements the ShoppingManager interface and serves as a manager for the shopping system.
class WestminsterShoppingManager implements ShoppingManager {
    private List<Product> productList;
    private ShoppingCart shoppingCart;
    private static final String FILE_NAME = "Thanaal.txt";
    private static final int MAX_PRODUCTS = 50;

    // Constructor initializes the shopping cart and product list.
    public WestminsterShoppingManager() {
        this.shoppingCart = new ShoppingCart();
        this.productList = new ArrayList<>();
        // Uncomment the line below if you want to load products from a file when the manager is initialized.
        // loadProductsFromFile();
    }

    // Getter method to retrieve the product list.
    public List<Product> getProductList() {
        return productList;
    }

    // Interface method to add a product to the system.
    @Override
    public void addProduct(Product product) {
        if (productList.size() < MAX_PRODUCTS) {
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Error: Maximum number of products reached.");
        }
    }

    // Interface method to delete a product from the system based on the product ID.
    @Override
    public void deleteProduct(String productId) {
        Product productToDelete = null;
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                productToDelete = product;
                break;
            }
        }

        if (productToDelete != null) {
            productList.remove(productToDelete);
            System.out.println("Product deleted successfully.");
            System.out.println("Deleted Product Information:");
            productToDelete.displayInfo();
            System.out.println("Total number of products left: " + productList.size());
        } else {
            System.out.println("Error: Product not found.");
        }
    }

    // Interface method to print the list of products.
    @Override
    public void printProducts() {
        Collections.sort(productList, Comparator.comparing(Product::getProductId));

        for (Product product : productList) {
            if (product instanceof Electronics) {
                System.out.println("Electronics Product:");
            } else if (product instanceof Clothing) {
                System.out.println("Clothing Product:");
            }

            product.displayInfo();
            System.out.println();
        }
    }

    // Interface method to save products to a file.
    @Override
    public void saveProducts() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(productList);
            System.out.println("Products saved successfully.");
        } catch (IOException e) {
            System.out.println("Error: Unable to save products to file.");
            e.printStackTrace();
        }
    }

    // Load products from a file if available.
    private void loadProductsFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                List<Product> savedProducts = (List<Product>) inputStream.readObject();
                productList.addAll(savedProducts);
                System.out.println("Products loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error: Unable to load products from file.");
                e.printStackTrace();
            }
        }
    }

    // Method to display the manager menu in the console.
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("\n*********Manager Menu:***********");
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Print the list of products");
            System.out.println("4. Save products to file");
            System.out.println("5. Load products from file");
            System.out.println("6. Open GUI");
            System.out.println("0. Exit");

            System.out.print("Choice one option from here: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addNewProduct(scanner);
                    break;
                case 2:
                    deleteProduct();
                    break;
                case 3:
                    printProducts();
                    break;
                case 4:
                    saveProducts();
                    break;
                case 5:
                    loadProductsFromFile();
                    break;
                case 6:
                    openGUI();
                    break;
                case 0:
                    System.out.println("Exit The Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

        } while (choice != 0);
    }

    // Helper method to add a new product based on user input.
    private void addNewProduct(Scanner scanner) {
        System.out.println("\n===== Add New Product =====");
        System.out.println("Select product type:");
        System.out.println("1. Electronics (Press 1)");
        System.out.println("2. Clothing (Press 2)");
        System.out.print("Enter your choice: ");

        int productType = scanner.nextInt();

        scanner.nextLine(); // Consume newline

        System.out.print("Enter product ID: ");
        String productId = scanner.nextLine();

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        int availableItems;
        while (true) {
            try {
                System.out.print("Enter number of available items: ");
                availableItems = Integer.parseInt(scanner.nextLine());
                break; // Break the loop if parsing is successful
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer for available items.");
            }
        }

        double price;
        while (true) {
            try {
                System.out.print("Enter price: ");
                price = Double.parseDouble(scanner.nextLine());
                break; // Break the loop if parsing is successful
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid amount for the price.");
            }
        }

        switch (productType) {
            case 1:
                System.out.print("Enter Brand: ");
                String brand = scanner.next();
                System.out.print("Enter Warranty Period (Year): ");
                int warrantyPeriod = scanner.nextInt();
                Electronics electronics = new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
                addProduct(electronics);
                break;
            case 2:
                System.out.print("Enter Size: ");
                String size = scanner.next();
                System.out.print("Enter Color: ");
                String color = scanner.next();
                Clothing clothing = new Clothing(productId, productName, availableItems, price, size, color);
                addProduct(clothing);
                break;
            default:
                System.out.println("Invalid product type.");
        }
    }

    // Helper method to delete a product based on user input.
    private void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Product ID to delete: ");
        String productId = scanner.next();
        deleteProduct(productId);
    }

    // Method to open the GUI for online shopping.
    private void openGUI() {
        SwingUtilities.invokeLater(() -> {
            ShoppingGUI onlineShopping = new ShoppingGUI(WestminsterShoppingManager.this);
            onlineShopping.setVisible(true);
        });
    }

    // Method to get a product by its ID.
    public Product getProductById(String productId) {
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    // Method to add a product to the shopping cart.
    public void addToShoppingCart(Product product) {
        shoppingCart.addProduct(product);
    }

    // Getter method to retrieve the shopping cart.
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    // Method to get a list of filtered products based on the product type.
    public List<Product> getFilteredProducts(String productType) {
        if ("Electronics".equals(productType)) {
            return productList.stream()
                    .filter(product -> product instanceof Electronics)
                    .collect(Collectors.toList());
        } else if ("Clothes".equals(productType)) {
            return productList.stream()
                    .filter(product -> product instanceof Clothing)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>(productList);
        }
    }

    // Method to get a list of clothing products.
    public List<Product> getClothingProducts() {
        return productList.stream()
                .filter(product -> product instanceof Clothing)
                .collect(Collectors.toList());
    }

    // Method to get a list of electronics products.
    public List<Product> getElectronicsProducts() {
        return productList.stream()
                .filter(product -> product instanceof Electronics)
                .collect(Collectors.toList());
    }

    // The main method to start the shopping manager.
    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        shoppingManager.displayMenu();
    }
}
