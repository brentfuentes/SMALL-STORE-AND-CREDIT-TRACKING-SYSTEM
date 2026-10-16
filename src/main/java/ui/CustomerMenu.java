package ui;

import model.*;

import static ui.AppData.*;
import static ui.Console.*;

public class CustomerMenu {

    static void customersMenu() {
        boolean back = false;
        boolean isAdmin = currentUser.isAdmin();
        while (!back) {
            printHeader("CUSTOMERS");
            int option = 1;
            int addOption = -1;
            if (!isAdmin) {
                addOption = option++;
                System.out.println(addOption + ". Add Customer");
            }
            int viewOption = option++;
            System.out.println(viewOption + ". View Customers");
            int searchOption = option++;
            System.out.println(searchOption + ". Search Customer");
            int editOption = -1, deleteOption = -1;
            if (isAdmin) {
                editOption = option++;
                System.out.println(editOption + ". Edit Customer");
                deleteOption = option++;
                System.out.println(deleteOption + ". Delete Customer");
            }
            int backOption = option;
            System.out.println(backOption + ". Back");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            if (!isAdmin && choice == addOption) addCustomer();
            else if (choice == viewOption) viewCustomers();
            else if (choice == searchOption) searchCustomer();
            else if (isAdmin && choice == editOption) editCustomer();
            else if (isAdmin && choice == deleteOption) deleteCustomer();
            else if (choice == backOption) back = true;
            else System.out.println("Invalid input. Please try again.");
        }
    }

    static void addCustomer() {
        System.out.println();
        System.out.print("Customer Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            return;
        }
        System.out.print("Contact Number: ");
        String contact = sc.nextLine().trim();

        String id = nextCustomerId();
        Customer newCustomer = new Customer(id, name, contact, "Active");
        customerDao.insert(newCustomer, currentUser.userId());
        customers.add(newCustomer);

        System.out.println();
        System.out.println("Customer ID: " + id);
        System.out.println("Customer added successfully!");
        pause();
    }

    static void viewCustomers() {
        printHeader("CUSTOMER LIST");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer c : customers) {
                System.out.println("ID: " + c.getId() + " | Name: " + c.getName() +
                        " | Contact: " + c.getContact() +
                        " | Status: " + c.getStatus());
            }
        }
        pause();
    }

    static void searchCustomer() {
        System.out.println();
        System.out.print("Enter Customer ID or Name: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Customer c : customers) {
            if (c.getId().equalsIgnoreCase(keyword) || c.getName().toLowerCase().contains(keyword)) {
                System.out.println("ID: " + c.getId() + " | Name: " + c.getName() +
                        " | Contact: " + c.getContact() +
                        " | Status: " + c.getStatus());
                found = true;
            }
        }
        if (!found) System.out.println("Customer not found.");
        pause();
    }

    static void editCustomer() {
        System.out.println();
        System.out.print("Enter Customer ID to edit: ");
        String id = sc.nextLine().trim();
        Customer c = findCustomer(id);
        if (c == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Leave blank to keep the current value.");
        System.out.print("Customer Name [" + c.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) c.setName(name);

        System.out.print("Contact Number [" + c.getContact() + "]: ");
        String contact = sc.nextLine().trim();
        if (!contact.isEmpty()) c.setContact(contact);

        System.out.print("Status (Active/Inactive) [" + c.getStatus() + "]: ");
        String statusInput = sc.nextLine().trim();
        if (statusInput.equalsIgnoreCase("Active")) {
            c.setStatus("Active");
        } else if (statusInput.equalsIgnoreCase("Inactive")) {
            c.setStatus("Inactive");
        } else if (!statusInput.isEmpty()) {
            System.out.println("Invalid input. Status not changed.");
        }

        customerDao.update(c);
        System.out.println("Customer updated successfully!");
        pause();
    }

    static void deleteCustomer() {
        System.out.println();
        System.out.print("Enter Customer ID to delete: ");
        String id = sc.nextLine().trim();
        Customer c = findCustomer(id);
        if (c == null) {
            System.out.println("Customer not found.");
            return;
        }
        if (customerDao.delete(c.getId())) {
            customers.remove(c);
            System.out.println("Customer deleted successfully!");
        }
        pause();
    }

    static Customer findCustomer(String id) {
        for (Customer c : customers) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }
}
