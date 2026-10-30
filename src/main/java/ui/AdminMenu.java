package ui;

import dao.UserDao;
import model.User;

import java.util.List;

import static ui.AppData.*;
import static ui.Console.*;

<<<<<<< HEAD
// User management screens for Admin: view users, view pending, approve, update role/status.
=======
// User management screens for Admin: view users, view pending, approve.
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3
public class AdminMenu {

    static void adminMenu(UserDao userDao) {
        boolean running = true;
        while (running) {
            printHeader("USER MANAGEMENT");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
<<<<<<< HEAD
            System.out.println("4. Update User Role/Status");
            System.out.println("5. Back");
=======
            System.out.println("4. Back");
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3
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
<<<<<<< HEAD
                    updateUserRoleAndStatus(userDao);
                    break;
                case 5:
=======
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3
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
<<<<<<< HEAD

    // ================= UPDATE ROLE / STATUS =================

    private static final List<String> VALID_ROLES = List.of("ADMIN", "STAFF");
    private static final List<String> VALID_STATUSES = List.of("APPROVED", "PENDING");

    static void updateUserRoleAndStatus(UserDao userDao) {
        printHeader("UPDATE USER ROLE / STATUS");
        List<User> users = userDao.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            pause();
            return;
        }
        printUserTable(users);
        System.out.println();

        User target = null;
        while (target == null) {
            int targetUserId = readIntInput("Enter Target User ID (0 to cancel): ");
            if (targetUserId == 0) {
                System.out.println("Update cancelled.");
                pause();
                return;
            }

            if (targetUserId == currentUser.userId()) {
                System.out.println("WARNING: Modifying the currently active admin account is restricted.");
                continue;
            }

            User found = null;
            for (User u : users) {
                if (u.userId() == targetUserId) {
                    found = u;
                    break;
                }
            }
            if (found == null) {
                System.out.println("User ID " + targetUserId + " does not exist. Try again.");
                continue;
            }
            if (found.isSuperAdmin()) {
                System.out.println("WARNING: The Super Admin account cannot be modified here.");
                continue;
            }
            target = found;
        }

        String newRole = readValidRole();
        if (newRole == null) {
            System.out.println("Update cancelled.");
            pause();
            return;
        }
        String newStatus = readValidStatus();
        if (newStatus == null) {
            System.out.println("Update cancelled.");
            pause();
            return;
        }

        if (userDao.updateUserRoleAndStatus(target.userId(), newRole, newStatus)) {
            activityLogDao.insert(currentUser.userId(),
                    "UPDATE USER: " + target.username() + " -> " + newRole + "/" + newStatus);
            System.out.println("User " + target.username() + " updated to " + newRole + " / " + newStatus + ".");
        } else {
            System.out.println("Could not update the user.");
        }
        pause();
    }

    // Keeps asking until the input is a whole number; letters/symbols never crash the program.
    static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Returns a valid role in upper case, or null if the admin entered 0 to cancel.
    static String readValidRole() {
        while (true) {
            System.out.print("Enter new role " + VALID_ROLES + " (0 to cancel): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;
            if (VALID_ROLES.contains(input)) return input;
            System.out.println("Invalid role. Please enter one of: " + String.join(", ", VALID_ROLES));
        }
    }

    // Returns a valid status in upper case, or null if the admin entered 0 to cancel.
    static String readValidStatus() {
        while (true) {
            System.out.print("Enter new status " + VALID_STATUSES + " (0 to cancel): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;
            if (VALID_STATUSES.contains(input)) return input;
            System.out.println("Invalid status. Please enter one of: " + String.join(", ", VALID_STATUSES));
        }
    }
=======
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3
}
