package model;

public class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String status; // "Active" or "Inactive"

    public Product(String id, String name, double price, int stock, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getStatus() { return status; }
    public boolean isActive() { return status.equalsIgnoreCase("Active"); }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setStatus(String status) { this.status = status; }

    public void reduceStock(int qty) { this.stock -= qty; }
}
