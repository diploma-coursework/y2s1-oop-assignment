package org.assignment.util.extended;

import jdk.jfr.Category;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseItem;

// IDK if needed
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// imports from example
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;

import java.util.HashMap;
import java.util.Map;

public class Item extends BaseItem {
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

    public void dbCreateNewItem(){
        dbCreateNewItem("Example",0.0,0,null,null);
    }

    public void dbCreateNewItem(String name, double unitPrice,int quantity, ItemCategory itemCategory, Supplier supplier){
        writeToDb(createMap(name, unitPrice, quantity, itemCategory, supplier),"item");

    }

    // store Item
    public Map<String, Object> createMap(String name, double unitPrice,int quantity, ItemCategory itemCategory, Supplier supplier){
        Map<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("name", name);
        itemInfo.put("unitPrice",unitPrice);
        itemInfo.put("quantity", quantity);
        itemInfo.put("itemCategory", itemCategory);
        itemInfo.put("supplier", supplier);

        return itemInfo;
    }

    public void writeToDb(Map<String, Object> itemInfo , String collection){
        DbMethods.writeToDb(itemInfo , collection, "ITEM", 4);
    }


    public <T> void editInDb(String id, String collection, int choice, T newValue){
        DbMethods.editInDb(id, collection, choice, newValue);
    }

    public static String Choice(int choice) {
        // This is Example, Every class need @Override this
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
