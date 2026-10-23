package ui;

import dao.UserDao;
import model.User;

import java.util.List;

import static ui.AppData.*;
import static ui.Console.*;

// User management screens for Admin: view users, view pending, approve.
public class AdminMenu {

    static void adminMenu(UserDao userDao) {
        boolean running = true;
        while (running) {
            printHeader("USER MANAGEMENT");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Back");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewAllUsers(userDao);
                    break;
                case 2:
                    viewPendingUsers(userDao);
                    break;
                case 3:
                    approveUserAccount(userDao);
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    static void viewAllUsers(UserDao userDao) {
        printHeader("ALL SYSTEM USERS");
        List<User> users = userDao.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            printUserTable(users);
        }
        pause();
    }

    static void viewPendingUsers(UserDao userDao) {
        printHeader("PENDING REGISTRATIONS");
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("No pending registrations.");
        } else {
            printUserTable(pending);
        }
        pause();
    }

    static void approveUserAccount(UserDao userDao) {
        printHeader("APPROVE USER ACCOUNT");
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("No pending registrations.");
            pause();
            return;
        }
        printUserTable(pending);
        System.out.println();

        while (true) {
            int targetId = readInt("Enter Target User ID to approve (0 to cancel): ");
            if (targetId == 0) {
                System.out.println("Approval cancelled.");
                pause();
                return;
            }

            User target = null;
            for (User u : pending) {
                if (u.userId() == targetId) {
                    target = u;
                    break;
                }
            }
            if (target == null) {
                System.out.println("ID " + targetId + " is not in the pending list. Try again.");
                continue;
            }

            if (userDao.approveUser(target.userId())) {
                activityLogDao.insert(currentUser.userId(), "APPROVE ACCOUNT: " + target.username());
                System.out.println("Account of " + target.username() + " approved.");
            } else {
                System.out.println("Could not approve the account.");
            }
            pause();
            return;
        }
    }
}
