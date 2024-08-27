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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Item extends BaseItem {
    private ItemCategory itemCategory;
    private Supplier supplier;

    public void dbCreateNewItem(){
        Firestore db = FirebaseConnection.getDb();

        try {
            Map<String, Object> itemInfo = new HashMap<>();
            itemInfo.put("name", "ExampleItem1");
            itemInfo.put("unitPrice",3.0);
            itemInfo.put("quantity", 0);


            ApiFuture<WriteResult> future = db.collection("item").document("id").set(itemInfo);
            WriteResult result = future.get();

            System.out.println(result.getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void dbGetItemById(String itemId) {
        Firestore db = FirebaseConnection.getDb();
        // itemId example, ITEM0001
        if(itemId.length() <8 && !itemId.startsWith("ITEM")){
            return;
        }

        try {
            int count = Integer.parseInt(itemId.substring(4));
            ApiFuture<DocumentSnapshot> future = db.collection("Count").document("item").get();
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

    public static void dbGetIdCount(){
        Firestore db = FirebaseConnection.getDb();

        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document("item").get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                // Retrieve the field as an Object
//                double count = document.getDouble("amount");
                Object fieldObject = document.get("amount");

                // Ensure the object is not null and is a Number
                if (fieldObject instanceof Number) {
                    int fieldValue = ((Number) fieldObject).intValue();
                } else {
                    System.out.println("Field is not a number.");
                }
            } else {
                System.out.println("Document does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
