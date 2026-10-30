package dao;

import model.Credit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreditDao {

    public List<Credit> findAll() {
        List<Credit> list = new ArrayList<>();
        String sql = "SELECT credit_id, customer_id, customer_name, credit_amount, "
                + "amount_paid, status FROM credits ORDER BY credit_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Credit(rs.getString("credit_id"), rs.getString("customer_id"),
                        rs.getString("customer_name"), rs.getDouble("credit_amount"),
                        rs.getDouble("amount_paid"), rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading credits: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Credit c) {
        String sql = "INSERT INTO credits (credit_id, customer_id, customer_name, "
                + "credit_amount, amount_paid, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCreditId());
            ps.setString(2, c.getCustomerId());
            ps.setString(3, c.getCustomerName());
            ps.setDouble(4, c.getCreditAmount());
            ps.setDouble(5, c.getAmountPaid());
            ps.setString(6, c.getStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving credit: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Credit c) {
        String sql = "UPDATE credits SET amount_paid = ?, status = ? WHERE credit_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, c.getAmountPaid());
            ps.setString(2, c.getStatus());
            ps.setString(3, c.getCreditId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating credit: " + e.getMessage());
            return false;
        }
    }
}
