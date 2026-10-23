package ui;

<<<<<<< HEAD
import dao.UserDao;
import model.*;

import java.util.Scanner;

=======
import model.*;

>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
import static ui.AppData.*;
import static ui.Console.*;
import static ui.CreditMenu.creditMenu;
import static ui.CustomerMenu.customersMenu;
import static ui.ProductMenu.productsMenu;
import static ui.RecordsMenu.recordsMenu;
import static ui.SalesMenu.salesMenu;

public class MainMenu {

<<<<<<< HEAD
    private static UserDao userDao;

    static void mainMenu(UserDao sharedUserDao, Scanner scanner) {
        userDao = sharedUserDao;
        Console.setScanner(scanner);
=======
    static void mainMenu() {
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
        boolean loggedIn = true;
        boolean isAdmin = currentUser.isAdmin();
        while (loggedIn) {
            printHeader("MAIN MENU");
            int option = 1;

            int dashboardOption = option++;
            System.out.println(dashboardOption + ". Dashboard");
            int productsOption = option++;
            System.out.println(productsOption + ". Products");

            // Making sales is done by Staff only.
            int salesOption = -1;
            if (!isAdmin) {
                salesOption = option++;
                System.out.println(salesOption + ". Sales");
            }

            int customersOption = option++;
            System.out.println(customersOption + ". Customers");
            int creditOption = option++;
            System.out.println(creditOption + ". Credit");
            int recordsOption = option++;
            System.out.println(recordsOption + ". Records");

            int reportsOption = -1;
            if (isAdmin) {
                reportsOption = option++;
                System.out.println(reportsOption + ". Reports");
            }
<<<<<<< HEAD
            int userMgmtOption = -1;
            if (isAdmin) {
                userMgmtOption = option++;
                System.out.println(userMgmtOption + ". User Management");
            }
            // Admin: delete Staff accounts. Admin and Staff: delete their own account.
            int deleteStaffOption = -1;
            if (isAdmin) {
                deleteStaffOption = option++;
                System.out.println(deleteStaffOption + ". Delete Staff Accounts");
            }
            int deleteOwnOption = option++;
            System.out.println(deleteOwnOption + ". Delete My Account");
=======
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
            int logoutOption = option;
            System.out.println(logoutOption + ". Logout");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            if (choice == dashboardOption) dashboard();
            else if (choice == productsOption) productsMenu();
            else if (!isAdmin && choice == salesOption) salesMenu();
            else if (choice == customersOption) customersMenu();
            else if (choice == creditOption) creditMenu();
            else if (choice == recordsOption) recordsMenu();
            else if (isAdmin && choice == reportsOption) reportsMenu();
<<<<<<< HEAD
            else if (isAdmin && choice == userMgmtOption) AdminMenu.adminMenu(userDao);
            else if (isAdmin && choice == deleteStaffOption) AccountDeletion.deleteAccounts(userDao, "STAFF");
            else if (choice == deleteOwnOption) {
                if (AccountDeletion.deleteOwnAccount(userDao)) {
                    loggedIn = false;
                }
            } else if (choice == logoutOption) {
=======
            else if (choice == logoutOption) {
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
                if (confirmLogout()) {
                    activityLogDao.insert(currentUser.userId(), "LOGOUT");
                    currentUser = null;
                    loggedIn = false;
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    static boolean confirmLogout() {
        System.out.println();
        System.out.println("Are you sure you want to logout?");
        System.out.println();
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice = readInt("Enter your choice: ");
        return choice == 1;
    }

    // ================= DASHBOARD =================

    static void dashboard() {
        printHeader("DASHBOARD");

        double totalSales = 0;
        for (Sale s : sales) totalSales += s.getTotal();

        double unpaidCredit = 0;
        for (Credit c : credits) unpaidCredit += c.getRemainingBalance();

        System.out.println("Total Products       : " + products.size());
        System.out.println("Total Customers      : " + customers.size());
        System.out.println("Total Sales          : " + peso(totalSales));
        System.out.println("Unpaid Credit        : " + peso(unpaidCredit));
        pause();
    }

    static void reportsMenu() {
        if (!currentUser.isAdmin()) {
            System.out.println("\nAccess denied. Admin only.");
            pause();
            return;
        }

        printHeader("REPORTS");

        double totalSales = 0;
        for (Sale s : sales) totalSales += s.getTotal();

        double outstandingCredit = 0;
        for (Credit c : credits) outstandingCredit += c.getRemainingBalance();

        System.out.println("Total Sales            : " + peso(totalSales));
        System.out.println("Total Products         : " + products.size());
        System.out.println("Total Customers        : " + customers.size());
        System.out.println("Outstanding Credit     : " + peso(outstandingCredit));
        pause();
    }
}
