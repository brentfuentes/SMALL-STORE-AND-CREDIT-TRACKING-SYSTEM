package dao;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.DriverManager;
=======
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

<<<<<<< HEAD
    static final String DATA_FOLDER = "data";
    static final String DB_FILE = DATA_FOLDER + "/scts.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    // Every connection enforces FOREIGN KEY rules (SQLite has them OFF by default).
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
        return conn;
=======
    private static final String DATA_FOLDER = "data";
    private static final String DB_FILE = DATA_FOLDER + "/scts.db";
    // foreign_keys=on makes SQLite enforce the FOREIGN KEY rules below.
    private static final String URL = "jdbc:sqlite:" + DB_FILE + "?foreign_keys=on";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Creates the data folder and all tables if they do not exist yet.
    public static void initialize() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        backupOldDatabaseIfNeeded();

        String[] tables = {
                "CREATE TABLE IF NOT EXISTS users ("
                        + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "full_name TEXT NOT NULL, "
                        + "username TEXT NOT NULL UNIQUE COLLATE NOCASE, "
                        + "password TEXT NOT NULL, "
                        + "user_type TEXT NOT NULL, "
                        + "status TEXT NOT NULL DEFAULT 'PENDING')",

                "CREATE TABLE IF NOT EXISTS products ("
                        + "product_id TEXT PRIMARY KEY, "
                        + "product_name TEXT NOT NULL, "
                        + "price REAL NOT NULL, "
                        + "stock_quantity INTEGER NOT NULL, "
                        + "status TEXT NOT NULL DEFAULT 'Active')",

                "CREATE TABLE IF NOT EXISTS customers ("
                        + "customer_id TEXT PRIMARY KEY, "
                        + "user_id INTEGER NOT NULL, "
                        + "customer_name TEXT NOT NULL, "
                        + "contact_number TEXT, "
                        + "status TEXT NOT NULL DEFAULT 'Active', "
                        + "FOREIGN KEY (user_id) REFERENCES users(user_id))",

                "CREATE TABLE IF NOT EXISTS sales ("
                        + "transaction_id TEXT PRIMARY KEY, "
                        + "user_id INTEGER NOT NULL, "
                        + "customer_id TEXT, "
                        + "sale_date TEXT NOT NULL, "
                        + "total_amount REAL NOT NULL, "
                        + "payment REAL NOT NULL, "
                        + "change_amount REAL NOT NULL, "
                        + "payment_type TEXT NOT NULL, "
                        + "transaction_status TEXT NOT NULL DEFAULT 'Completed', "
                        + "FOREIGN KEY (user_id) REFERENCES users(user_id), "
                        + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",

                // One row for every product inside a sale (a sale can have many products).
                "CREATE TABLE IF NOT EXISTS sale_items ("
                        + "item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "transaction_id TEXT NOT NULL, "
                        + "product_id TEXT NOT NULL, "
                        + "quantity INTEGER NOT NULL, "
                        + "unit_price REAL NOT NULL, "
                        + "subtotal REAL NOT NULL, "
                        + "FOREIGN KEY (transaction_id) REFERENCES sales(transaction_id), "
                        + "FOREIGN KEY (product_id) REFERENCES products(product_id))",

                "CREATE TABLE IF NOT EXISTS credits ("
                        + "credit_id TEXT PRIMARY KEY, "
                        + "customer_id TEXT NOT NULL, "
                        + "customer_name TEXT NOT NULL, "
                        + "credit_amount REAL NOT NULL, "
                        + "amount_paid REAL NOT NULL, "
                        + "status TEXT NOT NULL, "
                        + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",

                "CREATE TABLE IF NOT EXISTS activity_logs ("
                        + "log_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id INTEGER NOT NULL, "
                        + "action TEXT NOT NULL, "
                        + "log_time TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), "
                        + "FOREIGN KEY (user_id) REFERENCES users(user_id))"
        };

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            for (String sql : tables) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error setting up database: " + e.getMessage());
        }
    }

    // A database file made by an older version of this system has different columns.
    // It is renamed to scts_old_backup.db (nothing is deleted) and a new one is created.
    private static void backupOldDatabaseIfNeeded() {
        File db = new File(DB_FILE);
        if (!db.exists()) {
            return;
        }

        boolean hasUsersTable = false;
        boolean hasUserIdColumn = false;
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA table_info(users)")) {
            while (rs.next()) {
                hasUsersTable = true;
                if ("user_id".equals(rs.getString("name"))) {
                    hasUserIdColumn = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking database: " + e.getMessage());
            return;
        }

        if (hasUsersTable && !hasUserIdColumn) {
            File backup = new File(DATA_FOLDER, "scts_old_backup.db");
            if (backup.exists()) {
                backup.delete();
            }
            if (db.renameTo(backup)) {
                System.out.println("Old database found. It was saved as data/scts_old_backup.db");
                System.out.println("A new database with the updated tables was created.");
            }
        }
>>>>>>> 7c1f542e4a2df12ef376119142193edcd113c99b
    }
}
