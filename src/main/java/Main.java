import dao.DatabaseInitializer;
import dao.UserDao;
import ui.AppData;
import ui.Authentication;
import ui.Console;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. Initialize database (tables + foreign keys)
        DatabaseInitializer.initialize();

        // 2. Instantiate DAOs & utilities (one shared instance each)
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);
        Console.setScanner(scanner);
        AppData.loadAllData();
        seedDefaultAccounts(userDao);

        // 3. Delegate everything else to Authentication
        new Authentication(userDao, scanner).start();

        System.out.println("\nThank you for using the Small Store Sales and Credit Tracking System!");
        scanner.close();
    }

    // Creates the default Super Admin account (the only default account).
    private static void seedDefaultAccounts(UserDao userDao) {
        if (!userDao.roleExists("SUPERADMIN")) {
            userDao.insertWithStatus("Super Admin", "superadmin", "super123", "SUPERADMIN", "APPROVED");
        }
    }
}
