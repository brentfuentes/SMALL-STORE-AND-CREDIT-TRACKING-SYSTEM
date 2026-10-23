package ui;

import dao.UserDao;
<<<<<<< HEAD
import java.util.Scanner;
=======
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
import model.ActivityLog;
import model.User;

import java.util.List;

import static ui.AppData.*;
import static ui.Console.*;
import static ui.MainMenu.confirmLogout;

public class SuperAdminMenu {

<<<<<<< HEAD
    private static UserDao userDao;

    static void superAdminMenu(UserDao sharedUserDao, Scanner scanner) {
        userDao = sharedUserDao;
        Console.setScanner(scanner);
=======
    private static final UserDao userDao = new UserDao();

    static void superAdminMenu() {
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
        boolean loggedIn = true;
        while (loggedIn) {
            printHeader("SUPER ADMIN MENU");
            System.out.println("1. Pending Accounts");
            System.out.println("2. Activity Log");
<<<<<<< HEAD
            System.out.println("3. Delete Admin/Staff Accounts");
            System.out.println("4. Logout");
=======
            System.out.println("3. Logout");
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
            System.out.println();
            int choice = readInt("Enter your choice: ");

            if (choice == 1) pendingAccounts();
            else if (choice == 2) activityLog();
<<<<<<< HEAD
            else if (choice == 3) AccountDeletion.deleteAccounts(userDao, "ADMIN", "STAFF");
            else if (choice == 4) {
=======
            else if (choice == 3) {
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

    // ================= PENDING ACCOUNTS =================

    static void pendingAccounts() {
        printHeader("PENDING ACCOUNTS");

        List<User> pending = userDao.findByStatus("PENDING");
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
            pause();
            return;
        }

        for (int i = 0; i < pending.size(); i++) {
            User u = pending.get(i);
            System.out.println((i + 1) + ". " + u.fullName() + " (" + u.username() + ") | Role: " + u.userType());
        }
        System.out.println();

        int selection = readInt("Select account number (0 to go back): ");
        if (selection == 0) return;
        if (selection < 1 || selection > pending.size()) {
            System.out.println("Invalid input. Please try again.");
            pause();
            return;
        }
        User chosen = pending.get(selection - 1);

        System.out.println();
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        int action = readInt("Enter your choice: ");

        if (action == 1) {
<<<<<<< HEAD
            if (userDao.updateStatus(chosen.userId(), "APPROVED")) {
=======
            if (userDao.updateStatus(chosen.userId(), "ACTIVE")) {
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
                System.out.println("Account of " + chosen.username() + " approved.");
            }
        } else if (action == 2) {
            if (userDao.updateStatus(chosen.userId(), "REJECTED")) {
                System.out.println("Account of " + chosen.username() + " rejected.");
            }
        } else {
            System.out.println("Invalid input. Please try again.");
        }
        pause();
    }

    // ================= ACTIVITY LOG =================

    static void activityLog() {
        printHeader("ACTIVITY LOG");

        List<ActivityLog> logs = activityLogDao.findLatest(50);
        if (logs.isEmpty()) {
            System.out.println("No activity recorded yet.");
            pause();
            return;
        }

        System.out.println("Showing the latest " + logs.size() + " activities (newest first):");
        System.out.println();
        System.out.printf("%-20s %-15s %-12s %s%n", "Date and Time", "Username", "Role", "Action");
        System.out.println("---------------------------------------------------------------");
        for (ActivityLog log : logs) {
            System.out.printf("%-20s %-15s %-12s %s%n",
                    log.logTime(), log.username(), log.userType(), log.action());
        }
        pause();
    }
}
