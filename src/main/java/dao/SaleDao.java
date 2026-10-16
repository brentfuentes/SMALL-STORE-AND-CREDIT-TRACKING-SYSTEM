package dao;

import model.Product;
import model.Sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SaleDao {

    public List<Sale> findAll() {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT transaction_id, sale_date, total_amount, payment, change_amount, "
                + "payment_type, customer_id FROM sales ORDER BY transaction_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String customerId = rs.getString("customer_id");
                list.add(new Sale(rs.getString("transaction_id"), rs.getString("sale_date"),
                        rs.getDouble("total_amount"), rs.getDouble("payment"),
                        rs.getDouble("change_amount"), rs.getString("payment_type"),
                        customerId == null ? "" : customerId));
            }
        } catch (SQLException e) {
            System.out.println("Error loading sales: " + e.getMessage());
        }
        return list;
    }

    // Saves the sale and all of its products together. If anything fails, nothing is saved.
    public boolean insert(Sale s, int userId, List<Product> itemProducts, List<Integer> itemQuantities) {
        String saleSql = "INSERT INTO sales (transaction_id, user_id, customer_id, sale_date, total_amount, "
                + "payment, change_amount, payment_type, transaction_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Completed')";
        String itemSql = "INSERT INTO sale_items (transaction_id, product_id, quantity, unit_price, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement saleStmt = conn.prepareStatement(saleSql);
                 PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {

                saleStmt.setString(1, s.getTransactionId());
                saleStmt.setInt(2, userId);
                if (s.getCustomerId() == null || s.getCustomerId().isEmpty()) {
                    saleStmt.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    saleStmt.setString(3, s.getCustomerId());
                }
                saleStmt.setString(4, s.getDate());
                saleStmt.setDouble(5, s.getTotal());
                saleStmt.setDouble(6, s.getPayment());
                saleStmt.setDouble(7, s.getChange());
                saleStmt.setString(8, s.getPaymentType());
                saleStmt.executeUpdate();

                for (int i = 0; i < itemProducts.size(); i++) {
                    Product p = itemProducts.get(i);
                    int qty = itemQuantities.get(i);
                    itemStmt.setString(1, s.getTransactionId());
                    itemStmt.setString(2, p.getId());
                    itemStmt.setInt(3, qty);
                    itemStmt.setDouble(4, p.getPrice());
                    itemStmt.setDouble(5, p.getPrice() * qty);
                    itemStmt.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Error saving sale: " + e.getMessage());
            return false;
        }
    }
}
