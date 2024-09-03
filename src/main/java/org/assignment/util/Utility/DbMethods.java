package org.assignment.util.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.extended.ItemCategory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbMethods {

    // Write
    public static void writeToDb(Map<String, Object> itemInfo , String collection, String id){
        Firestore db = FirebaseConnection.getDb();
        try {
            ApiFuture<WriteResult> future = db.collection(collection).document(id).set(itemInfo);
            WriteResult result = future.get();

            System.out.println(result.getUpdateTime());
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    // find Class, Choice method, fieldName, Modify
    public static <T> void editInDb( String id, String collection, int choice, T newValue) {
        String callingClassName = new Throwable().getStackTrace()[1].getClassName();

        Class<?> callingClass;
        try {
            callingClass = Class.forName(callingClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
            return;
        }

        if (!DbMethodsInterface.class.isAssignableFrom(callingClass)) {
            System.err.println(callingClass.getSimpleName() + "did not implement DbMethodsInterface");
            return;
        }

        String fieldName = null;
        try {
            Method choiceMethod = callingClass.getMethod("Choice", int.class);
            fieldName = (String) choiceMethod.invoke(null, choice);

        } catch (NoSuchMethodException e) {
            System.err.println("Choice method not found: " + e.getMessage());
        } catch (InvocationTargetException e) {
            System.err.println("Error invoking Choice method: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access to Choice method: " + e.getMessage());
        }

        if (fieldName == null) {
            System.err.println("Invalid choice");
            return;
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(fieldName, newValue);

        modifyInDb(updateMap, id, collection);
    }

    // Modify method
    public static void modifyInDb(Map<String, Object> itemInfo, String id, String collection) {
        Firestore db = FirebaseConnection.getDb();

        DocumentReference docRef = db.collection(collection).document(id);
        ApiFuture<WriteResult> future = docRef.update(itemInfo);

        try {
            WriteResult result = future.get();
            System.out.println("Document successfully updated at: " + result.getUpdateTime());
        } catch (Exception e) {
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    // Delete
    public static void deleteInDb(String id, String collection) {
        Firestore db = FirebaseConnection.getDb();

        DocumentReference docRef = db.collection(collection).document(id);
        ApiFuture<WriteResult> future = docRef.delete();

        try {
            WriteResult result = future.get();
            System.out.println("Document successfully deleted at: " + result.getUpdateTime());
        } catch (Exception e) {
            System.err.println("Error deleting document: " + e.getMessage());
        }
    }

    // View
    public static void viewInDb(String id, String collection) {
        Firestore db = FirebaseConnection.getDb();

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
            System.err.println("Error getting document: " + e.getMessage());
        }
    }

    // getters
    public static <T> String generateOrRetrieveCategoryId(T itemCategory) {
        // Implement logic to generate or retrieve an ID for the ItemCategory
        return "newCategoryId"; // Replace with actual ID generation logic
    }


    // No. of Item
    private static int dbGetIdCount(String collection){
        Firestore db = FirebaseConnection.getDb();

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
        Firestore db = FirebaseConnection.getDb();

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
        String idFormatSize = "%0" + idSize + "d";
        if(dbGetIdCount(collection) != -1){
            dbIncrementCount(collection);
            return idFormat +String.format(idFormatSize,dbGetIdCount(collection));
        }
        else{
            return "ERROR";
        }
    }
}
