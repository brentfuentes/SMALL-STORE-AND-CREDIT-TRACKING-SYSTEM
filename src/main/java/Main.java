import dao.DatabaseConnection;
import dao.UserDao;
import ui.AppData;
import ui.AuthMenu;
import ui.Console;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initialize();
        AppData.loadAllData();
        seedDefaultAccounts();

        AuthMenu.welcomeScreen();

        System.out.println("\nThank you for using the Small Store Sales and Credit Tracking System!");
        Console.sc.close();
    }

    // Creates the default Super Admin account (the only default account).
    private static void seedDefaultAccounts() {
        UserDao userDao = new UserDao();
        if (!userDao.roleExists("SUPERADMIN")) {
            userDao.insertWithStatus("Super Admin", "superadmin", "super123", "SUPERADMIN", "ACTIVE");
        }
    }
}
