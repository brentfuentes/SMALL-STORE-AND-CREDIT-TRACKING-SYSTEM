package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Saves a new user with status PENDING. Returns the new user_id, or -1 if it failed.
    public int insert(String fullName, String username, String password, String userType) {
        String sql = "INSERT INTO users (full_name, username, password, user_type, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, userType);
            ps.setString(5, "PENDING");
            ps.executeUpdate();

            // Read back the id that SQLite just created (same connection).
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return -1;
        }
    }

    // Used only to create the default accounts (already APPROVED) on first run.
    public boolean insertWithStatus(String fullName, String username, String password,
                                    String userType, String status) {
        String sql = "INSERT INTO users (full_name, username, password, user_type, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, userType);
            ps.setString(5, status);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    // Returns the matching user, or null if the username/password is wrong.
    public User login(String username, String password) {
        String sql = "SELECT user_id, full_name, username, password, user_type, status "
                + "FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readUser(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error counting users: " + e.getMessage());
            return 0;
        }
    }

    public boolean roleExists(String userType) {
        String sql = "SELECT 1 FROM users WHERE user_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userType);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking role: " + e.getMessage());
            return false;
        }
    }

    public List<User> findByStatus(String status) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, full_name, username, password, user_type, status "
                + "FROM users WHERE status = ? ORDER BY user_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readUser(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return list;
    }

    // All registered accounts, whatever their role or status.
    public List<User> getAllUsers() {
        return queryUsers("SELECT user_id, full_name, username, password, user_type, status "
                + "FROM users ORDER BY user_id");
    }

    // Accounts waiting for approval, across all roles.
    public List<User> getPendingUsers() {
        return queryUsers("SELECT user_id, full_name, username, password, user_type, status "
                + "FROM users WHERE status = 'PENDING' ORDER BY user_id");
    }

    // Changes a PENDING account to APPROVED. Returns true if a row was updated.
    public boolean approveUser(int userId) {
        String sql = "UPDATE users SET status = 'APPROVED' WHERE user_id = ? AND status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error approving user: " + e.getMessage());
            return false;
        }
    }

    private List<User> queryUsers(String sql) {
        List<User> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(readUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return list;
    }

    // Active accounts of one role (used by the delete-account screens).
    public List<User> findActiveByRole(String userType) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, full_name, username, password, user_type, status "
                + "FROM users WHERE user_type = ? AND status = 'APPROVED' ORDER BY user_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readUser(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user status: " + e.getMessage());
            return false;
        }
    }

    private User readUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("user_id"), rs.getString("full_name"),
                rs.getString("username"), rs.getString("password"),
                rs.getString("user_type"), rs.getString("status"));
    }
}
