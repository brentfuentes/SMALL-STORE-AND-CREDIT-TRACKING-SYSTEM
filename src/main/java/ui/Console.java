package ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Console {

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3
    // The single shared Scanner. Main creates it and passes it in once at startup.
    public static Scanner sc = new Scanner(System.in);

    public static void setScanner(Scanner scanner) {
        sc = scanner;
    }
<<<<<<< HEAD
=======
=======
    public static final Scanner sc = new Scanner(System.in);
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3

    static void printHeader(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("   " + title);
        System.out.println("========================================");
        System.out.println();
    }

    static void pause() {
        System.out.println();
        System.out.print("Press ENTER to continue...");
        sc.nextLine();
    }

    static int readInt(String prompt) {
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

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
            }
        }
    }

    static String peso(double amount) {
        return String.format("\u20b1%,.2f", amount);
    }

    static String currentDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // Generates a simple sequential ID such as P001, C002, TXN003, based on the
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3

    // Prints any list of users as an aligned table: ID, Username, Role, Status.
    static void printUserTable(java.util.List<model.User> users) {
        String border = "+-------+-----------------+--------------+------------+";
        String row = "| %-5s | %-15s | %-12s | %-10s |%n";
        System.out.println(border);
        System.out.printf(row, "ID", "Username", "Role", "Status");
        System.out.println(border);
        for (model.User u : users) {
            System.out.printf(row, u.userId(), cut(u.username(), 15), cut(u.userType(), 12), cut(u.status(), 10));
        }
        System.out.println(border);
        System.out.println("Total: " + users.size());
    }

    private static String cut(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 1) + ".";
    }
<<<<<<< HEAD
=======
=======
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
>>>>>>> a5eae130fb9f00c22f8e9282d20f82cb795b53d3
}
