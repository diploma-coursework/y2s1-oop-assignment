package org.assignment.util.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;

import org.assignment.util.extended.*;

import java.util.HashMap;
import java.util.Map;

public class DbMethods {

    // Write
    public static void writeToDb(Map<String, Object> itemInfo , String collection, String idFormat, int idSize){
        Firestore db = FirebaseConnection.getDb();
        try {
            ApiFuture<WriteResult> future = db.collection(collection).document(dbCreateNewId(collection,idFormat,idSize)).set(itemInfo);
            WriteResult result = future.get();

            System.out.println(result.getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String Choice(int choice) {
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

    // Method to edit specific fields in the Firestore document
    public static <T> void editInDb(String id, String collection, int choice, T newValue) {
        String fieldName = Choice(choice);

        if (fieldName == null) {
            System.err.println("Invalid choice");
            return;
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(fieldName, newValue);

        // Call the modifyInDb method to update the document in the database
        modifyInDb(updateMap, id, collection);
    }

    // Modify method to update fields in Firestore
    public static void modifyInDb(Map<String, Object> itemInfo, String id, String collection) {
        Firestore db = FirebaseConnection.getDb();

        // Reference to the document in the specified collection
        DocumentReference docRef = db.collection(collection).document(id);

        // Update the document with the provided fields
        ApiFuture<WriteResult> future = docRef.update(itemInfo);

        try {
            // Block on response and check if it was successful
            WriteResult result = future.get();
            System.out.println("Document successfully updated at: " + result.getUpdateTime());
        } catch (Exception e) {
            System.err.println("Error updating document: " + e.getMessage());
        }
    }


    // Delete


    // View


    //

    // No. of Item
    private static int dbGetIdCount(String Class){
        Firestore db = FirebaseConnection.getDb();

        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document(Class).get();
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
    private static void dbIncrementCount(String Class) {
        Firestore db = FirebaseConnection.getDb();

        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document(Class).get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Object fieldObject = document.get("amount");

                if (fieldObject instanceof Number) {
                    // Increment the value by 1
                    int newAmount = ((Number) fieldObject).intValue() + 1;

                    // Update the document with the new value
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("amount", newAmount);

                    ApiFuture<WriteResult> updateFuture = db.collection("idNumber").document(Class).set(updatedData);
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
    private static String dbCreateNewId(String Class, String idFormat, int idSize){
        String idFormatSize = "%0" + idSize + "d";
        if(dbGetIdCount(Class) != -1){
            dbIncrementCount(Class);
            return idFormat +String.format(idFormatSize,dbGetIdCount(Class));
        }
        else{
            return "ERROR";
        }
    }
}
