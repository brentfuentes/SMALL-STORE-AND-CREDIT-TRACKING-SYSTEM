package ui;

import dao.UserDao;
import model.User;

import java.util.Scanner;

import static ui.AppData.*;
import static ui.Console.*;
import static ui.MainMenu.mainMenu;
import static ui.SuperAdminMenu.superAdminMenu;

public class Authentication {

    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    // Entry point of the CLI: login / register / exit.
    public void start() {
        boolean running = true;
        while (running) {
            printHeader("SMALL STORE SALES AND CREDIT\n          TRACKING SYSTEM");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private void login() {
        System.out.println();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User found = userDao.login(username, password);

        if (found == null) {
            System.out.println("\nInvalid username or password.");
            return;
        }

        if (found.status().equalsIgnoreCase("PENDING")) {
            System.out.println("\nYour account is still pending approval by the Super Admin.");
            return;
        }
        if (found.status().equalsIgnoreCase("REJECTED")) {
            System.out.println("\nYour account was rejected by the Super Admin.");
            return;
        }

        if (found.status().equalsIgnoreCase("DELETED")) {
            System.out.println("\nThis account has been deleted.");
            return;
        }

        currentUser = found;
        activityLogDao.insert(found.userId(), "LOGIN");
        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + currentUser.fullName() + ".");

        if (currentUser.isSuperAdmin()) {
            superAdminMenu(userDao, scanner);
        } else {
            mainMenu(userDao, scanner);
        }
    }

    private void register() {
        System.out.println();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        if (userDao.usernameExists(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        System.out.println();
        System.out.println("1. Admin");
        System.out.println("2. Staff");
        int roleChoice = readInt("Enter role: ");
        String role;
        if (roleChoice == 1) {
            role = "ADMIN";
        } else if (roleChoice == 2) {
            role = "STAFF";
        } else {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        int newUserId = userDao.insert(fullName, username, password, role);
        if (newUserId != -1) {
            activityLogDao.insert(newUserId, "REGISTER");
            System.out.println("\nAccount created successfully!");
            System.out.println("Please wait for the Super Admin to approve your account before logging in.");
        }
    }
}
