package dao;

import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id, customer_name, contact_number, status "
                + "FROM customers ORDER BY customer_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(rs.getString("customer_id"), rs.getString("customer_name"),
                        rs.getString("contact_number"), rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
        return list;
    }

    // userId is the staff/admin who added the customer.
    public boolean insert(Customer c, int userId) {
        String sql = "INSERT INTO customers (customer_id, user_id, customer_name, contact_number, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getId());
            ps.setInt(2, userId);
            ps.setString(3, c.getName());
            ps.setString(4, c.getContact());
            ps.setString(5, c.getStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Customer c) {
        String sql = "UPDATE customers SET customer_name = ?, contact_number = ?, status = ? "
                + "WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getContact());
            ps.setString(3, c.getStatus());
            ps.setString(4, c.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY")) {
                System.out.println("This customer cannot be deleted because they already have sales or credit records.");
                System.out.println("Edit the customer and set the status to Inactive instead.");
            } else {
                System.out.println("Error deleting customer: " + e.getMessage());
            }
            return false;
        }
    }
}
