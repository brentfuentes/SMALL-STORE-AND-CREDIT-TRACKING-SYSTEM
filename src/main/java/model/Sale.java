package model;

public class Sale {
    private String transactionId;
    private String date;
    private double total;
    private double payment;
    private double change;
    private String paymentType; // "Cash" or "Credit"
    private String customerId;  // empty if Cash sale

    public Sale(String transactionId, String date, double total, double payment,
                double change, String paymentType, String customerId) {
        this.transactionId = transactionId;
        this.date = date;
        this.total = total;
        this.payment = payment;
        this.change = change;
        this.paymentType = paymentType;
        this.customerId = customerId;
    }

    public String getTransactionId() { return transactionId; }
    public String getDate() { return date; }
    public double getTotal() { return total; }
    public double getPayment() { return payment; }
    public double getChange() { return change; }
    public String getPaymentType() { return paymentType; }
    public String getCustomerId() { return customerId; }
}
