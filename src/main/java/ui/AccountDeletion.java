package ui;

import dao.UserDao;
import model.User;

import java.util.ArrayList;
import java.util.List;

import static ui.AppData.*;
import static ui.Console.*;

// Account deletion screens shared by the Super Admin, Admin and Staff menus.
// Accounts are marked DELETED (not erased) so old sales, credits and activity
// logs that point to the user stay valid. A DELETED account can no longer log in.
public class AccountDeletion {

    // Super Admin: Admin + Staff accounts. Admin: Staff accounts only (never other Admins).
    static void deleteAccounts(UserDao userDao, String... roles) {
        printHeader("DELETE ACCOUNT");

        List<User> accounts = new ArrayList<>();
        for (String role : roles) {
            for (User u : userDao.findActiveByRole(role)) {
                if (u.userId() != currentUser.userId()) {   // never list yourself
                    accounts.add(u);
                }
            }
        }
        if (accounts.isEmpty()) {
            System.out.println("No accounts available to delete.");
            pause();
            return;
        }

        for (int i = 0; i < accounts.size(); i++) {
            User u = accounts.get(i);
            System.out.println((i + 1) + ". " + u.fullName() + " (" + u.username() + ") | Role: " + u.userType());
        }
        System.out.println();

        int selection = readInt("Select account number to delete (0 to go back): ");
        if (selection == 0) return;
        if (selection < 1 || selection > accounts.size()) {
            System.out.println("Invalid input. Please try again.");
            pause();
            return;
        }
        User chosen = accounts.get(selection - 1);

        if (chosen.userId() == currentUser.userId()) {
            System.out.println("Use 'Delete My Account' to remove your own account.");
            pause();
            return;
        }

        if (confirm("Delete the account of " + chosen.username() + "? This cannot be undone.")
                && userDao.updateStatus(chosen.userId(), "DELETED")) {
            activityLogDao.insert(currentUser.userId(), "DELETE ACCOUNT: " + chosen.username());
            System.out.println("Account of " + chosen.username() + " deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
        pause();
    }

    // Deletes the logged-in user's own account. Returns true if it was deleted
    // (the caller must then log the user out).
    static boolean deleteOwnAccount(UserDao userDao) {
        printHeader("DELETE MY ACCOUNT");
        System.out.print("Enter your password to confirm: ");
        String password = sc.nextLine().trim();
        if (!password.equals(currentUser.password())) {
            System.out.println("Incorrect password. Account not deleted.");
            pause();
            return false;
        }

        if (confirm("Delete your account? This cannot be undone.")
                && userDao.updateStatus(currentUser.userId(), "DELETED")) {
            activityLogDao.insert(currentUser.userId(), "DELETE OWN ACCOUNT");
            System.out.println("Your account was deleted. You will be logged out.");
            currentUser = null;
            pause();
            return true;
        }
        System.out.println("Deletion cancelled.");
        pause();
        return false;
    }

    private static boolean confirm(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println("1. Yes");
        System.out.println("2. No");
        return readInt("Enter your choice: ") == 1;
    }
}
