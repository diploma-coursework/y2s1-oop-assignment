package org.assignment.util;
import com.google.firebase.database.annotations.NotNull;

import java.util.Scanner;

public class BaseItem {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private BaseCategory category;
    private BaseSupplier supplier;
    private static int noOfItems = 1;

    // Constructor
    public BaseItem(String name, double unitPrice, BaseCategory category, BaseSupplier supplier) {
        this.id = StringId("P");
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = 0;
        this.category = category;
        this.supplier = supplier;
        noOfItems++;
    }

    public BaseItem(String name, double unitPrice, int quantity, BaseCategory category, BaseSupplier supplier) {
        this.id = StringId("P");
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
        noOfItems++;
    }

    // Getters

    private String getId() {return id;}

    private String getName() {return name;}

    private double getUnitPrice() {return unitPrice;}

    private int getQuantity() {return quantity;}

    private BaseCategory getCategory() {return category;}

    private BaseSupplier getSupplier() {return supplier;}

    private int getNoOfItems(){return noOfItems;}

    // Setters

    private void setName(String name) {this.name = name;}

    private void setUnitPrice(double unitPrice) {this.unitPrice = unitPrice;}

    private void setQuantity(int quantity) {this.quantity = quantity;}

    private void setCategory(BaseCategory category) {this.category = category;}

    private void setSupplier(BaseSupplier supplier) {this.supplier = supplier;}

    // Annotations

    @Override
    public String toString(){
        return "Item : "+getId()+"\nName : "+getName()+"\nUnit Price : "+String.format("%.2f", getUnitPrice())+"\nQuantity : "+getQuantity()+"\nCategory : "+getCategory()+"\nSupplier : "+getSupplier()+"\n";
    }

    // Methods
    public void createItem() {
        // Menu
        // Accept input
        // get all required parameters
        Scanner scanner = new Scanner(System.in);
        System.out.println("Name : ");
        String name = scanner.nextLine();
        System.out.println("Unit Price : ");
        double unitPrice = scanner.nextDouble();
        // check if name exist
        // select Category
        // select supplier
        // add to database
        // save time information


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

    @NotNull
    protected String StringId(String identifier){
        return identifier + String.format("%04d",getNoOfItems());
    }
}
