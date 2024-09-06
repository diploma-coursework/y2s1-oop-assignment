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
        System.out.println("extended setter/getters");
        return this.ItemCategory;
    }

    public Supplier getSupplier(){
        System.out.println("extended setter/getters");
        return this.Supplier;
    }

    public void setItemCategory(ItemCategory itemCategory){
        System.out.println("extended setter/getters");
        this.ItemCategory=itemCategory;
    }

    public void setSupplier(Supplier supplier){
        System.out.println("extended setter/getters");
        this.Supplier=supplier;
    }

//    @Override
//    public double getUnitPrice() {
//        // Custom behavior: log access or modify the return value
//        System.out.println("Accessing unitPrice in Item class");
//        return super.getUnitPrice();
//    }
//
//    @Override
//    public void setUnitPrice(double unitPrice) {
//        // Custom behavior: add validation or modify input
//        System.out.println("Setting unitPrice in Item class");
//        super.setUnitPrice(unitPrice);
//    }

    // Base setters
    @Override
    public void setId(String id) {
        System.out.println("Overrided setter/getters");
        this.id = id;
    }

    @Override
    public void setName(String name) {
        System.out.println("Overrided setter/getters");
        this.name = name;
    }

    @Override
    public void setUnitPrice(double unitPrice) {
        System.out.println("Overrided setter/getters");
        this.unitPrice = unitPrice;
    }

    @Override
    public void setQuantity(int quantity) {
        System.out.println("Overrided setter/getters");
        this.quantity = quantity;
    }

    // Base getters
    @Override
    public String getId() {
        System.out.println("Overrided setter/getters");
        return this.id;
    }
    @Override
    public String getName() {
        System.out.println("Overrided setter/getters");
        return this.name;
    }

    @Override
    public double getUnitPrice() {
        System.out.println("Overrided setter/getters");
        return this.unitPrice;
    }

    @Override
    public int getQuantity() {
        System.out.println("Overrided setter/getters");
        return this.quantity;
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
