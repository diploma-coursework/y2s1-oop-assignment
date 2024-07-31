package org.assignment.util;

import java.time.LocalDateTime;

public class BaseTransaction {
    private String id;
    private BaseItem item;
    private BaseStaff staff;
    private char transactionType; // + for add, - for subtract
    private int quantity;
    private LocalDateTime transactionDate;

    public BaseTransaction( BaseItem item, BaseStaff staff, char transactionType, int quantity, LocalDateTime transactionDate) {
        // id is auto generated
        this.item = item;
        this.staff = staff;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public BaseItem getItem() {return item;}

    public void setItem(BaseItem item) {this.item = item;}

    public BaseStaff getStaff() {return staff;}

    public void setStaff(BaseStaff staff) {this.staff = staff;}

    public char getTransactionType() {return transactionType;}

    public void setTransactionType(char transactionType) {this.transactionType = transactionType;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public LocalDateTime getTransactionDate() {return transactionDate;}

    public void setTransactionDate(LocalDateTime transactionDate) {this.transactionDate = transactionDate;}
}
