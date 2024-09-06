package org.assignment.util.extended;

import com.google.cloud.firestore.DocumentReference;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseItem;

// IDK if needed

// imports from example
import com.google.cloud.firestore.Firestore;
import org.assignment.util.FirebaseConnection;

import java.util.HashMap;
import java.util.Map;

import static org.assignment.util.Utility.DbMethods.db;

public class Item extends BaseItem {
    private ItemCategory ItemCategory;
    private Supplier Supplier;


    public Item(){
        this(null,0.0,0,null,null);
    }
    public Item(String name, double unitPrice, int quantity, Supplier supplier) {
        super(name, unitPrice, quantity); // Call the superclass constructor
        // testing without itemCategory
        this.Supplier = supplier;
    }

    public Item(String name, double unitPrice, int quantity, ItemCategory itemCategory, Supplier supplier) {
        super(name, unitPrice, quantity); // Call the superclass constructor
        this.ItemCategory = itemCategory;
        this.Supplier = supplier;
    }

    // getters and setters
    public ItemCategory getItemCategory(){
        return this.ItemCategory;
    }

    public Supplier getSupplier(){
        return this.Supplier;
    }

    public void setItemCategory(ItemCategory itemCategory){
        this.ItemCategory=itemCategory;
    }

    public void setSupplier(Supplier supplier){
        this.Supplier=supplier;
    }

    @Override
    public double getUnitPrice() {
        // Custom behavior: log access or modify the return value
        System.out.println("Accessing unitPrice in Item class");
        return super.getUnitPrice();
    }

    @Override
    public void setUnitPrice(double unitPrice) {
        // Custom behavior: add validation or modify input
        System.out.println("Setting unitPrice in Item class");
        super.setUnitPrice(unitPrice);
    }

    // setters
//    @Override
//    protected void setUnitPrice(double unitPrice) {
//        this.unitPrice = unitPrice;
//    }
//
//    // getters
//    @Override
//    protected double getUnitPrice() {
//        return this.unitPrice;
//    }

    public boolean secureInstance(Item item){
        if(ItemMenu.confirmSave(item)) {
            dbCreateNewItem();
            return true;
        }
        return false;
    }

    // create New Item
    public void dbCreateNewItem(){
        this.setId(DbMethods.dbCreateNewId("Item","ITEM",4));
        System.out.println("set id?");
        System.out.println(getId());
        DbMethods.writeToDb(createMap(), "Item", getId());
    }

    public Map<String, Object> createMap() {
        DocumentReference itemCategoryRef = db.collection("ItemCategory").document(getItemCategory().getId());
        System.out.println("MAP issue");
        DocumentReference supplierRef = db.collection("Supplier").document(getSupplier().getId());

        Map<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("name", getName());
        itemInfo.put("unitPrice", getUnitPrice());
        itemInfo.put("quantity", getQuantity());
        itemInfo.put("ItemCategory", itemCategoryRef);
        itemInfo.put("Supplier", supplierRef);

        return itemInfo;
    }

    // Modify
    public static <T> void modifyInDb(String id, int choice, T newValue){
        DbMethods.modifyInDb(id,"Item",editChoice(choice),newValue);
    }

    // Delete
    public void deleteInDb(String id){
        DbMethods.deleteInDb(id, "Item");
    }

    // View
    public void viewInDb(String id){
        DbMethods.viewInDb(id,"Item");
    }

    // Will be used in DbMethods;
    public static String editChoice(int choice) {
        switch (choice) {
            case 1:
                return "name";
            case 2:
                return "unitPrice";
            case 3:
                return "quantity";
            case 4:
                return "ItemCategory";
            case 5:
                return "Supplier";
            default:
                return null;
        }
    }

}
