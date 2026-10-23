package ui;

import model.*;

import static ui.AppData.*;
import static ui.Console.*;

public class ProductMenu {

    static void productsMenu() {
        boolean back = false;
        boolean isAdmin = currentUser.isAdmin();
        while (!back) {
            printHeader("PRODUCTS & INVENTORY");
            int option = 1;
            int addOption = -1, editOption = -1, deleteOption = -1;

            if (isAdmin) {
                addOption = option++;
                System.out.println(addOption + ". Add Product");
            }
            int viewOption = option++;
            System.out.println(viewOption + ". View Products");
            int searchOption = option++;
            System.out.println(searchOption + ". Search Product");
            if (isAdmin) {
                editOption = option++;
                System.out.println(editOption + ". Edit Product");
                deleteOption = option++;
                System.out.println(deleteOption + ". Delete Product");
            }
            int backOption = option;
            System.out.println(backOption + ". Back");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            if (isAdmin && choice == addOption) addProduct();
            else if (choice == viewOption) viewProducts();
            else if (choice == searchOption) searchProduct();
            else if (isAdmin && choice == editOption) editProduct();
            else if (isAdmin && choice == deleteOption) deleteProduct();
            else if (choice == backOption) back = true;
            else System.out.println("Invalid input. Please try again.");
        }
    }

    static void addProduct() {
        System.out.println();
        System.out.print("Product Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        double price = readDouble("Price: ");
        if (price < 0) {
            System.out.println("Invalid input. Price cannot be negative.");
            return;
        }

        int stock = readInt("Stock: ");
        if (stock < 0) {
            System.out.println("Invalid input. Stock cannot be negative.");
            return;
        }

        String id = nextProductId();
        Product newProduct = new Product(id, name, price, stock, "Active");
        productDao.insert(newProduct);
        products.add(newProduct);

        System.out.println();
        System.out.println("Product ID: " + id);
        System.out.println("Product added successfully!");
        pause();
    }

    static void viewProducts() {
        printHeader("PRODUCT LIST");
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Product p : products) {
                System.out.println("ID: " + p.getId() + " | Name: " + p.getName() +
                        " | Price: " + peso(p.getPrice()) + " | Stock: " + p.getStock() +
                        " | Status: " + p.getStatus());
            }
        }
        pause();
    }

    static void searchProduct() {
        System.out.println();
        System.out.print("Enter Product ID or Name: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(keyword) || p.getName().toLowerCase().contains(keyword)) {
                System.out.println("ID: " + p.getId() + " | Name: " + p.getName() +
                        " | Price: " + peso(p.getPrice()) + " | Stock: " + p.getStock() +
                        " | Status: " + p.getStatus());
                found = true;
            }
        }
        if (!found) System.out.println("Product not found.");
        pause();
    }

    static void editProduct() {
        System.out.println();
        System.out.print("Enter Product ID to edit: ");
        String id = sc.nextLine().trim();
        Product p = findProduct(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Leave blank to keep the current value.");
        System.out.print("Product Name [" + p.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) p.setName(name);

        System.out.print("Price [" + p.getPrice() + "]: ");
        String priceInput = sc.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                double price = Double.parseDouble(priceInput);
                if (price < 0) {
                    System.out.println("Invalid input. Price cannot be negative.");
                } else {
                    p.setPrice(price);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Price not changed.");
            }
        }

        System.out.print("Stock [" + p.getStock() + "]: ");
        String stockInput = sc.nextLine().trim();
        if (!stockInput.isEmpty()) {
            try {
                int stock = Integer.parseInt(stockInput);
                if (stock < 0) {
                    System.out.println("Invalid input. Stock cannot be negative.");
                } else {
                    p.setStock(stock);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Stock not changed.");
            }
        }

        System.out.print("Status (Active/Inactive) [" + p.getStatus() + "]: ");
        String statusInput = sc.nextLine().trim();
        if (statusInput.equalsIgnoreCase("Active")) {
            p.setStatus("Active");
        } else if (statusInput.equalsIgnoreCase("Inactive")) {
            p.setStatus("Inactive");
        } else if (!statusInput.isEmpty()) {
            System.out.println("Invalid input. Status not changed.");
        }

        productDao.update(p);
        System.out.println("Product updated successfully!");
        pause();
    }

    static void deleteProduct() {
        System.out.println();
        System.out.print("Enter Product ID to delete: ");
        String id = sc.nextLine().trim();
        Product p = findProduct(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }
        if (productDao.delete(p.getId())) {
            products.remove(p);
            System.out.println("Product deleted successfully!");
        }
        pause();
    }

    static Product findProduct(String id) {
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }
}
