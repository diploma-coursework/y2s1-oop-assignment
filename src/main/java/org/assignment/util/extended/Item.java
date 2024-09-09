package org.assignment.util.extended;

import org.assignment.util.base.BaseItem;

import java.util.HashMap;

public class Item extends BaseItem {
    private ItemCategory itemCategory;
    private Supplier supplier;

    public Item() {
        this("", "", 0.0,0,null,null);
    }

    public Item(String id, String name, double unitPrice, int quantity, ItemCategory itemCategory, Supplier supplier) {
        super(name, unitPrice, quantity);
        this.itemCategory = itemCategory;
        this.supplier = supplier;
    }

    // setters
    public void setItemCategory(ItemCategory itemCategory){ this.itemCategory = itemCategory; }
    public void setSupplier(Supplier supplier){ this.supplier = supplier; }

    // getters
    public ItemCategory getItemCategory(){ return this.itemCategory; }
    public Supplier getSupplier(){ return this.supplier; }

    public static Item fromHashMap(HashMap<String, Object> map) {
        return new Item(
                (String) map.get("id"),
                (String) map.get("name"),
                (double) map.get("unitPrice"),
                (int) map.get("quantity"),
                (ItemCategory) map.get("ItemCategory"),
                (Supplier) map.get("Supplier")
                );
    }
}
