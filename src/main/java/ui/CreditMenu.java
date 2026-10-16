package ui;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static ui.AppData.*;
import static ui.Console.*;

public class CreditMenu {

    static void creditMenu() {
        boolean back = false;
        boolean isAdmin = currentUser.isAdmin();
        while (!back) {
            printHeader("CREDIT");
            int option = 1;
            int viewOption = option++;
            System.out.println(viewOption + ". View Customer Credit");
            int paymentOption = -1;
            if (!isAdmin) {
                paymentOption = option++;
                System.out.println(paymentOption + ". Record Payment");
            }
            int unpaidOption = option++;
            System.out.println(unpaidOption + ". View Unpaid Credit");
            int backOption = option;
            System.out.println(backOption + ". Back");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            if (choice == viewOption) viewCustomerCredit();
            else if (!isAdmin && choice == paymentOption) recordPayment();
            else if (choice == unpaidOption) viewUnpaidCredit();
            else if (choice == backOption) back = true;
            else System.out.println("Invalid input. Please try again.");
        }
    }

    static void viewCustomerCredit() {
        System.out.println();
        System.out.print("Enter Customer Name or ID: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Credit c : credits) {
            if (c.getCustomerId().equalsIgnoreCase(keyword) ||
                    c.getCustomerName().toLowerCase().contains(keyword)) {
                printCreditDetails(c);
                found = true;
            }
        }
        if (!found) System.out.println("No credit record found.");
        pause();
    }

    static void recordPayment() {
        System.out.println();
        System.out.print("Enter Customer Name or ID: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        List<Credit> matches = new ArrayList<>();
        for (Credit c : credits) {
            if (!c.getStatus().equals("Fully Paid") &&
                    (c.getCustomerId().equalsIgnoreCase(keyword) ||
                            c.getCustomerName().toLowerCase().contains(keyword))) {
                matches.add(c);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No outstanding credit record found.");
            return;
        }

        System.out.println();
        for (int i = 0; i < matches.size(); i++) {
            Credit c = matches.get(i);
            System.out.println((i + 1) + ". " + c.getCustomerName() +
                    " | Credit ID: " + c.getCreditId() +
                    " | Remaining Balance: " + peso(c.getRemainingBalance()));
        }

        int selection = readInt("Select credit record: ");
        if (selection < 1 || selection > matches.size()) {
            System.out.println("Invalid input. Please try again.");
            return;
        }
        Credit credit = matches.get(selection - 1);

        double amount = readDouble("Payment Amount: ");
        if (amount <= 0) {
            System.out.println("Invalid input. Payment must be greater than zero.");
            return;
        }
        if (amount > credit.getRemainingBalance()) {
            System.out.println("Invalid input. Payment cannot be greater than the remaining balance.");
            return;
        }

        credit.addPayment(amount);
        creditDao.update(credit);

        System.out.println();
        printCreditDetails(credit);
        System.out.println();
        System.out.println("Payment recorded successfully!");
        pause();
    }

    static void viewUnpaidCredit() {
        printHeader("UNPAID CREDIT");
        boolean found = false;
        for (Credit c : credits) {
            if (!c.getStatus().equals("Fully Paid")) {
                printCreditDetails(c);
                System.out.println();
                found = true;
            }
        }
        if (!found) System.out.println("No unpaid credit records.");
        pause();
    }

    static void printCreditDetails(Credit c) {
        System.out.println("Customer Name     : " + c.getCustomerName());
        System.out.println("Credit Amount     : " + peso(c.getCreditAmount()));
        System.out.println("Amount Paid       : " + peso(c.getAmountPaid()));
        System.out.println("Remaining Balance : " + peso(c.getRemainingBalance()));
        System.out.println("Status            : " + c.getStatus());
    }
}
