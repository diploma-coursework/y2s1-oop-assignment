package org.assignment.util.extended;

import com.google.cloud.firestore.DocumentReference;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.Utility.DbMethodsInterface;
import org.assignment.util.base.BaseItem;

// IDK if needed

// imports from example
import com.google.cloud.firestore.Firestore;
import org.assignment.util.FirebaseConnection;

import java.util.HashMap;
import java.util.Map;

public class Item extends BaseItem implements DbMethodsInterface {
    private ItemCategory itemCategory;
    private Supplier supplier;


    public Item(){
        this(null,0.0,0,null,null);
    }

    public Item(String name, double unitPrice, int quantity, ItemCategory itemCategory, Supplier supplier) {
        super(name, unitPrice, quantity); // Call the superclass constructor
        this.itemCategory = itemCategory;
        this.supplier = supplier;
    }

    // getters and setters
    public ItemCategory getItemCategory(){
        return this.itemCategory;
    }

    public Supplier getSupplier(){
        return this.supplier;
    }

    public boolean secureInstance(Item item){
        if(ItemMenu.cofirmSave(item)) {
            dbCreateNewItem(item.getName(), item.getUnitPrice(), item.getQuantity(), item.getItemCategory(), item.getSupplier());
            return true;
        }
        return false;
    }

    public void dbCreateNewItem(){
        dbCreateNewItem("Example",0.0,0,null,null);
    }

    // create New Item
    public void dbCreateNewItem(String name, double unitPrice,int quantity, ItemCategory itemCategory, Supplier supplier){
        Firestore db = FirebaseConnection.getDb();
        setId(DbMethods.dbCreateNewId("item","ITEM",4));
        DocumentReference itemCategoryRef = db.collection("itemCategories").document(itemCategory.getId());
        DocumentReference supplierRef = db.collection("suppliers").document(supplier.getId());

        DbMethods.writeToDb(createMap(name, unitPrice, quantity, itemCategoryRef, supplierRef), "item", getId());
    }

    public Map<String, Object> createMap(String name, double unitPrice, int quantity, DocumentReference itemCategory, DocumentReference supplier) {
        Map<String, Object> itemInfo = new HashMap<>();

        itemInfo.put("name", name);
        itemInfo.put("unitPrice", unitPrice);
        itemInfo.put("quantity", quantity);
        itemInfo.put("itemCategory", itemCategory);
        itemInfo.put("supplier", supplier);

        return itemInfo;
    }

    // Modify
    public <T> void editInDb(String id, int choice, T newValue){
        DbMethods.editInDb(id, "item", choice, newValue);
    }

    // Delete
    public void deleteInDb(String id){
        DbMethods.deleteInDb(id, "item");
    }

    // View
    public void viewInDb(String id){
        DbMethods.viewInDb(id,"item");
    }

    // Will be used in DbMethods;
    public static String Choice(int choice) {
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
