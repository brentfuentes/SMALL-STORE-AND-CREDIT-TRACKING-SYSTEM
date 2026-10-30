package model;

public class Credit {
    private String creditId;
    private String customerId;
    private String customerName;
    private double creditAmount;
    private double amountPaid;
    private String status; // "Unpaid", "Partially Paid", "Fully Paid"

    public Credit(String creditId, String customerId, String customerName,
                  double creditAmount, double amountPaid, String status) {
        this.creditId = creditId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.creditAmount = creditAmount;
        this.amountPaid = amountPaid;
        this.status = status;
    }

    public String getCreditId() { return creditId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public double getCreditAmount() { return creditAmount; }
    public double getAmountPaid() { return amountPaid; }
    public String getStatus() { return status; }

    public double getRemainingBalance() { return creditAmount - amountPaid; }

    public void addPayment(double amount) {
        this.amountPaid += amount;
        updateStatus();
    }

    public void updateStatus() {
        double remaining = getRemainingBalance();
        if (remaining <= 0.0001) {
            status = "Fully Paid";
        } else if (amountPaid > 0) {
            status = "Partially Paid";
        } else {
            status = "Unpaid";
        }
    }
}
