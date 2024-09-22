package org.assignment.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;


import java.util.HashMap;
import java.util.Map;

public class DatabaseIdManager {
    public final static Firestore db = FirebaseConnection.getDb();

    // No. of Item
    private static int dbGetIdCount(String collection){
        try {
            // 1 line for ApiFuture<DocumentSnapshot>, 1 line for DocumentSnapShot, better for thread, might cause common exception found when using thread
            ApiFuture<DocumentSnapshot> future = db.collection("idNumber").document(collection).get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Object fieldObject = document.get("amount");
                if (fieldObject instanceof Number) {return ((Number) fieldObject).intValue();}
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void dbIncrementCount(String collection) {
        try {
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("amount", dbGetIdCount(collection)+1);
            // reason to use .get() here, making sure the process is complete, before continuing
            db.collection("idNumber").document(collection).set(updatedData).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create Item ID
    public static String dbCreateNewId(String collection, String idFormat, int idSize){

        StringBuilder idFormatSize = new StringBuilder();
        idFormatSize.append(idFormat).append(String.format("%0"+idSize+"d",dbGetIdCount(collection)));
        if(dbGetIdCount(collection) != -1){
            dbIncrementCount(collection);
            return idFormatSize.toString();
        }
        else return "ERR";
    }

    public static boolean isValidCode(String idFormat,String input) {
        if (input.length() != 8 && input.length() != 7) {
            return false;
        }

        String prefix, suffix;

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
}
