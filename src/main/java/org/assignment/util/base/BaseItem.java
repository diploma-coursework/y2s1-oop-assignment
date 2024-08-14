package org.assignment.util.base;

import org.assignment.util.extended.Supplier;

abstract public class BaseItem {
    protected String id;
    protected String name;
    protected double unitPrice;
    protected int quantity;

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
    protected void setId(String id) {
        this.id = id;
    }
    protected void setName(String name) {
        this.name = name;
    }
    protected void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    protected void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // getters
    protected String getId() {
        return this.id;
    }
    protected String getName() {
        return this.name;
    }
    protected double getUnitPrice() {
        return this.unitPrice;
    }
    protected int getQuantity() {
        return this.quantity;
    }
}
