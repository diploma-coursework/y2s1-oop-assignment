package org.assignment.util;

import com.google.firebase.database.annotations.NotNull;

public class Item {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private Category category;
    private Supplier supplier;
    private static int noOfItems = 1;

    public Item(){
        noOfItems++;
    }

    public Item(String id, String name, double unitPrice, int quantity, Category category,Supplier supplier) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
        noOfItems++;
    }

    // getters
    public int getNoOfItems(){
        return noOfItems;
    }




    @NotNull
    protected String StringId(String identifier){
        return identifier + getNoOfItems();
    }


}
