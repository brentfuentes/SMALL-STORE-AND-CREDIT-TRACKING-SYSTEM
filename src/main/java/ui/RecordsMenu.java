package ui;

import model.*;

import static ui.AppData.*;
import static ui.Console.*;
import static ui.CreditMenu.printCreditDetails;

public class RecordsMenu {

    static void recordsMenu() {
        boolean back = false;
        while (!back) {
            printHeader("RECORDS");
            System.out.println("1. Sales Records");
            System.out.println("2. Credit Records");
            System.out.println("3. Back");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: salesRecords(); break;
                case 2: creditRecords(); break;
                case 3: back = true; break;
                default: System.out.println("Invalid input. Please try again.");
            }
        }
    }

    static void salesRecords() {
        printHeader("SALES RECORDS");
        if (sales.isEmpty()) {
            System.out.println("No sales records found.");
        } else {
            for (Sale s : sales) {
                System.out.println("Transaction ID : " + s.getTransactionId());
                System.out.println("Date           : " + s.getDate());
                System.out.println("Total Amount   : " + peso(s.getTotal()));
                System.out.println("Payment        : " + peso(s.getPayment()));
                System.out.println("Change         : " + peso(s.getChange()));
                System.out.println("Payment Type   : " + s.getPaymentType());
                System.out.println("----------------------------------------");
            }
        }
        pause();
    }

    static void creditRecords() {
        printHeader("CREDIT RECORDS");
        if (credits.isEmpty()) {
            System.out.println("No credit records found.");
        } else {
            for (Credit c : credits) {
                System.out.println("Credit ID         : " + c.getCreditId());
                printCreditDetails(c);
                System.out.println("----------------------------------------");
            }
        }
        pause();
    }
}
