package org.assignment.util.base;

import org.assignment.util.extended.Supplier;
import org.assignment.util.extended.Category;
import org.assignment.util.extended.Supplier;

public class BaseItem {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private Category category;
    private Supplier supplier;

    public BaseItem() {
        this(null, null, 0.0, 0, null, null);
    }

    public BaseItem(String id, String name, double unitPrice, int quantity, Category category, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
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
    private void setCategory(Category category) {
        this.category = category;
    }
    private void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    // setters
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
    private Category getCategory() {
        return this.category;
    }
    private Supplier getSupplier() {
        return this.supplier;
    }
}
