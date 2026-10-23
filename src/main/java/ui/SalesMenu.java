package ui;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static ui.AppData.*;
import static ui.Console.*;
import static ui.ProductMenu.findProduct;

public class SalesMenu {

    static void salesMenu() {
        boolean back = false;
        while (!back) {
            printHeader("SALES");
            System.out.println("1. New Sale");
            System.out.println("2. Back");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: newSale(); break;
                case 2: back = true; break;
                default: System.out.println("Invalid input. Please try again.");
            }
        }
    }

    static void newSale() {
        if (products.isEmpty()) {
            System.out.println("\nNo products available. Please add products first.");
            pause();
            return;
        }

        List<Product> itemProducts = new ArrayList<>();
        List<Integer> itemQuantities = new ArrayList<>();
        double total = 0;

        boolean addingItems = true;
        while (addingItems) {
            System.out.println();
            printHeader("AVAILABLE PRODUCTS");
            for (Product p : products) {
                if (!p.isActive()) continue;
                System.out.println(p.getId() + " - " + p.getName() +
                        " | Price: " + peso(p.getPrice()) + " | Stock: " + p.getStock());
            }

            System.out.print("\nEnter Product ID: ");
            String id = sc.nextLine().trim();
            Product p = findProduct(id);
            if (p == null) {
                System.out.println("Product not found.");
                continue;
            }
            if (!p.isActive()) {
                System.out.println("This product is inactive and cannot be sold.");
                continue;
            }

            int qty = readInt("Quantity: ");
            if (qty <= 0) {
                System.out.println("Invalid input. Quantity must be greater than zero.");
                continue;
            }

            int alreadyInCart = 0;
            for (int i = 0; i < itemProducts.size(); i++) {
                if (itemProducts.get(i).getId().equals(p.getId())) {
                    alreadyInCart += itemQuantities.get(i);
                }
            }

            if (qty + alreadyInCart > p.getStock()) {
                System.out.println("Invalid input. Not enough stock available.");
                continue;
            }

            double subtotal = p.getPrice() * qty;
            total += subtotal;
            itemProducts.add(p);
            itemQuantities.add(qty);

            System.out.println();
            System.out.println("Product: " + p.getName());
            System.out.println("Price: " + peso(p.getPrice()));
            System.out.println("Quantity: " + qty);
            System.out.println("Subtotal: " + peso(subtotal));

            System.out.println();
            System.out.println("1. Add another product");
            System.out.println("2. Proceed to payment");
            int next = readInt("Enter your choice: ");
            if (next == 2) addingItems = false;
        }

        System.out.println();
        System.out.println("Total: " + peso(total));

        System.out.println();
        System.out.println("Payment Type:");
        System.out.println();
        System.out.println("1. Cash");
        System.out.println("2. Credit");
        int payChoice = readInt("Enter choice: ");

        if (payChoice == 1) {
            processCashSale(itemProducts, itemQuantities, total);
        } else if (payChoice == 2) {
            processCreditSale(itemProducts, itemQuantities, total);
        } else {
            System.out.println("Invalid input. Sale cancelled.");
        }
        pause();
    }

    static void processCashSale(List<Product> itemProducts, List<Integer> itemQuantities, double total) {
        double payment = readDouble("Payment: ");
        if (payment < total) {
            System.out.println("Invalid input. Payment is less than the total amount.");
            System.out.println("Sale cancelled.");
            return;
        }
        double change = payment - total;

        for (int i = 0; i < itemProducts.size(); i++) {
            itemProducts.get(i).reduceStock(itemQuantities.get(i));
            productDao.update(itemProducts.get(i));
        }

        String txnId = nextSaleId();
        Sale sale = new Sale(txnId, currentDate(), total, payment, change, "Cash", "");
        saleDao.insert(sale, currentUser.userId(), itemProducts, itemQuantities);
        sales.add(sale);

        System.out.println();
        System.out.println("Change: " + peso(change));
        System.out.println();
        System.out.println("Sale completed successfully!");
    }

    static void processCreditSale(List<Product> itemProducts, List<Integer> itemQuantities, double total) {
        if (customers.isEmpty()) {
            System.out.println("No customers available. Please add a customer first.");
            return;
        }

        System.out.println();
        System.out.println("Available Customers:");
        System.out.println();
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i).getName());
        }

        int selection = readInt("Select customer: ");
        if (selection < 1 || selection > customers.size()) {
            System.out.println("Invalid input. Sale cancelled.");
            return;
        }
        Customer customer = customers.get(selection - 1);
        if (!customer.isActive()) {
            System.out.println("This customer is inactive. Sale cancelled.");
            return;
        }

        for (int i = 0; i < itemProducts.size(); i++) {
            itemProducts.get(i).reduceStock(itemQuantities.get(i));
            productDao.update(itemProducts.get(i));
        }

        String txnId = nextSaleId();
        Sale sale = new Sale(txnId, currentDate(), total, 0, 0, "Credit", customer.getId());
        saleDao.insert(sale, currentUser.userId(), itemProducts, itemQuantities);
        sales.add(sale);

        String creditId = nextCreditId();
        Credit credit = new Credit(creditId, customer.getId(), customer.getName(), total, 0, "Unpaid");
        creditDao.insert(credit);
        credits.add(credit);

        System.out.println();
        System.out.println("Credit recorded for " + customer.getName() + ": " + peso(total));
        System.out.println();
        System.out.println("Sale completed successfully!");
    }
}
