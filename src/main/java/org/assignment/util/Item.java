package org.assignment.util;

public class Item extends BaseItem{
    public Item(String name, double unitPrice, BaseCategory category, BaseSupplier supplier) {
        super(name, unitPrice, category, supplier);
    }

    public Item(String name, double unitPrice, int quantity, BaseCategory category, BaseSupplier supplier) {
        super(name, unitPrice,quantity, category, supplier);
    }

}
