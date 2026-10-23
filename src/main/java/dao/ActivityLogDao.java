package dao;

import model.ActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDao {

    // action is REGISTER, LOGIN or LOGOUT. The time is filled in by the database.
    public void insert(int userId, String action) {
        String sql = "INSERT INTO activity_logs (user_id, action) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving activity log: " + e.getMessage());
        }
    }

    // Newest activity first. The username and user type come from the users table.
    public List<ActivityLog> findLatest(int limit) {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT a.log_id, a.user_id, u.username, u.user_type, a.action, a.log_time "
                + "FROM activity_logs a JOIN users u ON a.user_id = u.user_id "
                + "ORDER BY a.log_id DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ActivityLog(rs.getInt("log_id"), rs.getInt("user_id"),
                            rs.getString("username"), rs.getString("user_type"),
                            rs.getString("action"), rs.getString("log_time")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading activity log: " + e.getMessage());
        }
        return list;
    }
}
