package org.assignment.util.base;

abstract public class BaseItem {
    protected String id;
    protected String name;
    protected double unitPrice;
    protected int quantity;

    public BaseItem() {
        this(null, 0.0, 0);
    }

    public BaseItem(String name, double unitPrice, int quantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
