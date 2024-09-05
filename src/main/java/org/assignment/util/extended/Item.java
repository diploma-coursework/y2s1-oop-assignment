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

    // setters
    @Override
    protected void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    // getters
    @Override
    protected double getUnitPrice() {
        return this.unitPrice;
    }

    public boolean secureInstance(Item item){
        if(ItemMenu.confirmSave(item)) {
            dbCreateNewItem();
            return true;
        }
        return false;
    }

    // create New Item
    public void dbCreateNewItem(){
        setId(DbMethods.dbCreateNewId("Item","ITEM",4));
        DbMethods.writeToDb(createMap(), "Item", getId());
    }

    public Map<String, Object> createMap() {
        DocumentReference itemCategoryRef = db.collection("ItemCategories").document(getItemCategory().getId());
        System.out.println("MAP issue");
        DocumentReference supplierRef = db.collection("Suppliers").document(getSupplier().getId());

        Map<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("name", getName());
        itemInfo.put("unitPrice", getUnitPrice());
        itemInfo.put("quantity", getQuantity());
        itemInfo.put("ItemCategory", itemCategoryRef);
        itemInfo.put("Supplier", supplierRef);

        return itemInfo;
    }

    // Modify
    public <T> void modifyInDb(String id, int choice, T newValue){
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
    public String editChoice(int choice) {
        switch (choice) {
            case 1:
                return "name";
            case 2:
                return "unitPrice";
            case 3:
                return "quantity";
            case 4:
                return "itemCategory";
            case 5:
                return "supplier";
            default:
                return null;
        }
    }

}
