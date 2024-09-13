package org.assignment.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DbMethods {
    public static Firestore db = FirebaseConnection.getDb();


    // Write
    public static void writeToDb(Map<String, Object> itemInfo , String collection, String id){
//        System.out.println("reached writeToDb");
        ApiFuture<WriteResult> future = db.collection(collection).document(id).set(itemInfo);
        dbVerification("created",future);
    }

    // find Class, Choice method, fieldName, Modify
    public static <T> void modifyInDb(String id, String collection, String field, T newValue) {
        DocumentReference docRef = db.collection(collection).document(id);
        if (field == null) {
            System.err.println("Invalid choice");
            return;
        }
        // need add try catch statement
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(field, newValue);

        ApiFuture<WriteResult> future = docRef.update(updateMap);
        dbVerification("modified",future);
    }

    public static <T> void modifyInDb(String id, String collection, String field,int index, T newValue) {
        DocumentReference docRef = db.collection(collection).document(id);
        if (field == null) {
            System.err.println("Invalid choice");
            return;
        }
        // need add try catch statement
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(field, newValue);

        ApiFuture<WriteResult> future = docRef.update(updateMap);
        dbVerification("modified",future);
    }

    // Delete
    public static void deleteInDb(String id, String collection) {
        DocumentReference docRef = db.collection(collection).document(id);
        ApiFuture<WriteResult> future = docRef.delete();

        dbVerification("deleted",future);
    }

    // View
    public static void viewInDb(String id, String collection) {
        DocumentReference docRef = db.collection(collection).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                // Print the document fields
                System.out.println("Document data: " + document.getData());
            } else {
                System.out.println("No Document");
            }
        } catch (Exception e) {
            System.err.println("3) Error getting document: " + e.getMessage());
        }
    }

    // getters


    // No. of Item
    private static int dbGetIdCount(String collection){
        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document(collection).get();
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
    private static void dbIncrementCount(String collection) {
        try {
            // Retrieve the document snapshot
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document(collection).get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Object fieldObject = document.get("amount");

                if (fieldObject instanceof Number) {
                    // Increment the value by 1
                    int newAmount = ((Number) fieldObject).intValue() + 1;

                    // Update the document with the new value
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("amount", newAmount);

                    ApiFuture<WriteResult> updateFuture = db.collection("idNumber").document(collection).set(updatedData);
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
    public static String dbCreateNewId(String collection, String idFormat, int idSize){
        // improve with StringBuilder
        String idFormatSize = "%0" + idSize + "d";
        if(dbGetIdCount(collection) != -1){
            dbIncrementCount(collection);
            return idFormat +String.format(idFormatSize,dbGetIdCount(collection));
        }
        else{
            return "ERROR";
        }
    }

    public static boolean isValidCode(String idFormat,String input) {
        if (input.length() != 8 && input.length() != 7) {
            return false;
        }

        String prefix;
        String suffix;

        if (input.length() == 8){
            prefix = input.substring(0, 4);
            suffix = input.substring(4);
        }
        else {
            prefix = input.substring(0, 3);
            suffix = input.substring(3);
        }

        return prefix.equals(idFormat) && suffix.chars().allMatch(Character::isDigit);
    }

    public static void dbVerification(String action, ApiFuture<WriteResult> future){
        try {
            WriteResult result = future.get();
            System.out.println("Document successfully " + action  + " at: " + result.getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error " + action  + " document: " + e.getMessage());
        }
    }
}
