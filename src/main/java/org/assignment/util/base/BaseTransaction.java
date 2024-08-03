package org.assignment.util.base;

import java.time.LocalDate;
import org.assignment.util.enums.TransactionType;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.Staff;

public class BaseTransaction {
    private String id;
    private Item item;
    private Staff staff;
    private TransactionType transactionType;
    private int quantity;
    private LocalDate transactionDate;

    public BaseTransaction() {
        this(null, null, null, null, 0, null);
    }

    public BaseTransaction(String id, Item item, Staff staff, TransactionType transactionType, int quantity, LocalDate transactionDate) {
        this.id = id;
        this.item = item;
        this.staff = staff;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    // getters
    public String getId() {
        return id;
    }
    public BaseItem getItem() {
        return item;
    }
    public BaseStaff getStaff() {
        return staff;
    }
    public TransactionType getTransactionType() {
        return transactionType;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}
