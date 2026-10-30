package ui;

import dao.ActivityLogDao;
import dao.CreditDao;
import dao.CustomerDao;
import dao.ProductDao;
import dao.SaleDao;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    static final ProductDao productDao = new ProductDao();
    static final CustomerDao customerDao = new CustomerDao();
    static final SaleDao saleDao = new SaleDao();
    static final CreditDao creditDao = new CreditDao();
    static final ActivityLogDao activityLogDao = new ActivityLogDao();

    static List<Product> products = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static List<Sale> sales = new ArrayList<>();
    static List<Credit> credits = new ArrayList<>();

    static User currentUser = null;

    public static void loadAllData() {
        products = productDao.findAll();
        customers = customerDao.findAll();
        sales = saleDao.findAll();
        credits = creditDao.findAll();
    }

    static String generateId(String prefix, List<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            try {
                max = Math.max(max, Integer.parseInt(id.substring(prefix.length())));
            } catch (Exception e) {
                // ignore ids that don't match the expected format
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    static String nextProductId() {
        List<String> ids = new ArrayList<>();
        for (Product p : products) ids.add(p.getId());
        return generateId("P", ids);
    }

    static String nextCustomerId() {
        List<String> ids = new ArrayList<>();
        for (Customer c : customers) ids.add(c.getId());
        return generateId("C", ids);
    }

    static String nextSaleId() {
        List<String> ids = new ArrayList<>();
        for (Sale s : sales) ids.add(s.getTransactionId());
        return generateId("TXN", ids);
    }

    static String nextCreditId() {
        List<String> ids = new ArrayList<>();
        for (Credit c : credits) ids.add(c.getCreditId());
        return generateId("CR", ids);
    }
}
