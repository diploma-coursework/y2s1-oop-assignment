package org.assignment.util;
import com.google.firebase.database.annotations.NotNull;

import java.util.Scanner;

public class Item {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private Category category;
    private Supplier supplier;
    private static int noOfItems = 1;

    // Constructor

    public Item(String name, double unitPrice, int quantity, Category category,Supplier supplier) {
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

    // Methods
    public void createItem() {
        // Menu
        Scanner scanner = new Scanner(System.in);
    }

    public void readItem(String id) {
        // Code to retrieve the item from the database by id
    }

    public void updateItem() {
        // Code to update this item in the database
    }

    public void deleteItem() {
        // Code to delete this item from the database
    }

    // Stock Management Methods
    public void checkStockLevel() {
        // Code to check current stock level of this item
    }

    public void updateStockLevel(int quantity) {
        // Code to update the stock level of this item
        this.quantity = quantity;
        // Additional database update logic here
    }

    public void performStockTake(int countedQuantity) {
        // Code to adjust the quantity based on stock take results
        this.quantity = countedQuantity;
        // Additional database update logic here
    }
    // Menu Stuff

}
