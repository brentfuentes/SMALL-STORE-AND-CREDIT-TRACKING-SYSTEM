package ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Console {

    public static final Scanner sc = new Scanner(System.in);

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
}
