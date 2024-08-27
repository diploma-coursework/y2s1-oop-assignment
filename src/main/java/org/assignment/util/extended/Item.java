package org.assignment.util.extended;

import jdk.jfr.Category;
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

    // store Item
    public void dbCreateNewItem(String name, double unitPrice,int quantity, ItemCategory itemCategory, Supplier supplier){
        Firestore db = FirebaseConnection.getDb();

        try {
            Map<String, Object> itemInfo = new HashMap<>();
            itemInfo.put("name", name);
            itemInfo.put("unitPrice",unitPrice);
            itemInfo.put("quantity", quantity);
            itemInfo.put("itemCategory", itemCategory);
            itemInfo.put("supplier", supplier);

            ApiFuture<WriteResult> future = db.collection("item").document(dbCreateNewId()).set(itemInfo);
            WriteResult result = future.get();

            System.out.println(result.getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get Item by ID
    public static void dbGetItemById(String itemId) {
        Firestore db = FirebaseConnection.getDb();
        // itemId example, ITEM0001
        if(itemId.length() <8 && !itemId.startsWith("ITEM")){
            return;
        }

        try {
            int count = Integer.parseInt(itemId.substring(4));
            ApiFuture<DocumentSnapshot> future = db.collection("item").document(itemId).get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                System.out.println("Item data: " + document.getData());
            } else {
                System.out.println("No such item found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // No. of Item
    private static int dbGetIdCount(){
        Firestore db = FirebaseConnection.getDb();

        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document("item").get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Object fieldObject = document.get("amount");

                if (fieldObject instanceof Number) {
                    return ((Number) fieldObject).intValue();
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // No. of item +1
    private static void dbIncrementCount() {
        Firestore db = FirebaseConnection.getDb();

        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document("item").get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Object fieldObject = document.get("amount");

                if (fieldObject instanceof Number) {
                    // Increment the value by 1
                    int newAmount = ((Number) fieldObject).intValue() + 1;

                    // Update the document with the new value
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("amount", newAmount);

                    ApiFuture<WriteResult> updateFuture = db.collection("idNumber").document("item").set(updatedData);
                    WriteResult result = updateFuture.get();
                } else {
                    System.out.println("Field 'amount' is not a number.");
                }
            } else {
                System.out.println("Document does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create Item ID
    private static String dbCreateNewId(){
                if(dbGetIdCount() != -1){
                    dbIncrementCount();
                    return "ITEM" +String.format("%04d",dbGetIdCount());
                }
                else{
                    return "ERROR";
                }
    }
}
