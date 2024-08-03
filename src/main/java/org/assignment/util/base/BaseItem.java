package org.assignment.util.base;

import org.assignment.util.extended.Supplier;

public class BaseItem {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;

    public BaseItem() {
        this(null, null, 0.0, 0);
    }

    public BaseItem(String id, String name, double unitPrice, int quantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // setters
    private void setId(String id) {
        this.id = id;
    }
    private void setName(String name) {
        this.name = name;
    }
    private void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    private void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // getters
    private String getId() {
        return this.id;
    }
    private String getName() {
        return this.name;
    }
    private double getUnitPrice() {
        return this.unitPrice;
    }
    private int getQuantity() {
        return this.quantity;
    }
}
