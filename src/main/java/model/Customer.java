package model;

public class Customer {
    private String id;
    private String name;
    private String contact;
    private String status; // "Active" or "Inactive"

    public Customer(String id, String name, String contact, String status) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getStatus() { return status; }
    public boolean isActive() { return status.equalsIgnoreCase("Active"); }

    public void setName(String name) { this.name = name; }
    public void setContact(String contact) { this.contact = contact; }
    public void setStatus(String status) { this.status = status; }
}
