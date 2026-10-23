package dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id, product_name, price, stock_quantity, status FROM products ORDER BY product_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(rs.getString("product_id"), rs.getString("product_name"),
                        rs.getDouble("price"), rs.getInt("stock_quantity"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Product p) {
        String sql = "INSERT INTO products (product_id, product_name, price, stock_quantity, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving product: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Product p) {
        String sql = "UPDATE products SET product_name = ?, price = ?, stock_quantity = ?, status = ? "
                + "WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getStatus());
            ps.setString(5, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY")) {
                System.out.println("This product cannot be deleted because it is already used in sales.");
                System.out.println("Edit the product and set its status to Inactive instead.");
            } else {
                System.out.println("Error deleting product: " + e.getMessage());
            }
            return false;
        }
    }
}
