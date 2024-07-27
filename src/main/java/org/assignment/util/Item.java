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

    // Constructor

    public Item(String id, String name, double unitPrice, int quantity, Category category,Supplier supplier) {
        this.id = StringId("P");
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
        noOfItems++;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
            return unitPrice;
        }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getNoOfItems(){
        return noOfItems;
    }

    // Setters

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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    // Annotations

    @Override
    public String toString(){
        return "Item : "+getId()+"\nName : "+getName()+"\nUnit Price : "+String.format("%.2f", getUnitPrice())+"\nQuantity : "+getQuantity()+"\nCategory : "+getCategory()+"\nSupplier : "+getSupplier()+"\n";
    }

    @NotNull
    protected String StringId(String identifier){
        return identifier + String.format("%04d",getNoOfItems());
    }

}
