package org.assignment.util.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DbMethods {
    public static Firestore db = FirebaseConnection.getDb();


    // Original Deserialize
    public static <T>T deserializeds(String id,Class<T> tClass) {

        ApiFuture<DocumentSnapshot> future = db.collection(tClass.getSimpleName()).document(id).get();
        try {
            System.out.println("Fetching document with ID: " + id + " from collection: " + tClass.getSimpleName());
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                System.out.println("Document exists, attempting to deserialize into class: " + tClass.getSimpleName());
                return document.toObject(tClass);
            } else {
                System.err.println("No such document!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("1) Error getting document: " + e.getMessage());
            return null;
        }
    }

    // TODO: Fix deserialized

    public static <T> T deserialized(String id, Class<T> tClass) {
        ApiFuture<DocumentSnapshot> future = db.collection(tClass.getSimpleName()).document(id).get();
        try {
            DocumentSnapshot document = future.get();
            System.out.println("Doc = " + document);
            if (document.exists()) {
//                T obj = document.toObject(tClass);
                System.out.println("Issue Here?");
                resolveReferences(document, tClass); // TODO: Dynamically resolve DocumentReferences
                return document.toObject(tClass);
            } else {
                System.err.println("No such document!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("2) Error getting document: " + e.getMessage());
            return null;
        }
    }

    private static<T> void resolveReferences(DocumentSnapshot document, T tClass) throws ExecutionException, InterruptedException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        // tClass.getClass().getDeclaredFields()

        if (document.exists()) {
            // Get all fields as a Map
            Map<String, Object> fields = document.getData();

            // Iterate over the Map entries
//            assert fields != null;
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                if(entry == null){
                    System.out.println("Entry is null here");
                }
                System.out.println("Index. " + entry);
                String field = entry.getKey();
                Object fieldValue = entry.getValue();

                // Print field name and value
                System.out.println("Field name: " + field);
                System.out.println("Field value: " + fieldValue);

                // Optionally handle different types of values
                if (fieldValue instanceof DocumentReference) {
                    System.out.println("Have Doc Ref");

                    DocumentReference docRef = (DocumentReference) fieldValue;

                    // Fetch the referenced document
                    DocumentSnapshot refDocument = docRef.get().get();

                    System.out.println("DOCREF =" + docRef);
                    System.out.println("REFDOC =" + refDocument);

                    // Based on the field name, determine the class to deserialize into
                    if (field.equals("ItemCategory")) {
                        System.out.println("IS ItemCategory datatype");
                        // Manually create ItemCategory if direct deserialization fails
                        Map<String, Object> data = refDocument.getData();
                        System.out.println("Data : " + data);
                        if (data != null) {
                            ItemCategory itemCategory = new ItemCategory();
                            itemCategory.setName((String) data.get("name"));
                            itemCategory.setDescription((String) data.get("description"));
                            System.out.println("Reach here before listDocRef");
                            List<DocumentReference> listDocRef = (List<DocumentReference>) data.get("Item");
                            System.out.println("Reach here after listDocRef");
                            if (listDocRef != null) {
                                System.out.println("ListDocRef is not null :" + listDocRef);
                                List<Item> items = new ArrayList<>();
                                for (DocumentReference index : listDocRef) {
                                    try {
                                        System.out.println("an iteration");
                                        System.out.println("index = " + index);
                                        DocumentSnapshot itemSnapshot = index.get().get();
                                        System.out.println("finish index.get().get() :" + itemSnapshot);
                                        if (itemSnapshot.exists()) {
                                            System.out.println("itemSnapshot exist :" + index);
                                            Item item = itemSnapshot.toObject(Item.class);
                                            System.out.println("the iteration Item is -" + item);
                                            items.add(item);
                                        } else {
                                            System.out.println("Item document does not exist for reference: " + index.getPath());
                                        }
                                    } catch (ExecutionException | InterruptedException e) {
                                        System.err.println("Error fetching referenced document: " + e.getMessage());
                                    }
                                }
//                                itemCategory.setItems(items);  TODO: fix
                                System.out.println("Entering setField");
                                setField(tClass, field, itemCategory);
                            }
                        } else {
                            System.out.println("ItemCategory document has no data!");
                        }

//                        ItemCategory itemCategory = refDocument.toObject(ItemCategory.class);
                        // Assign the deserialized object to the field in the tClass
//                        setField(tClass, field, itemCategory); // Helper method to set the field value
                    } else if (field.equals("Supplier")) {
                        Supplier supplier = refDocument.toObject(Supplier.class);
                        setField(tClass, field, supplier);
                    } else if (field.equals("Item")){
                        System.out.println("IS Item datatype");
                        Item item = refDocument.toObject(Item.class);
                        setField(tClass, field, item);
                    }
//                    Class<?> clazz = Class.forName(field); // TODO: trying to mask fieldValue with a class of name of the field
//                    clazz.cast(fieldValue);

                    // Handle DocumentReference
//                    DocumentReference docRef = (DocumentReference) fieldValue;
                    // Resolve the document reference (if you have a method for this)
//                    Object resolvedObject = resolveReference(docRef, getFieldType(tClass, field));
                    // Set the resolved object to tClass
//                    setField(tClass, field, clazz);

//                    Class<?> clazz = Class.forName(field); // TODO: trying to mask fieldValue with a class of name of the field
//                    clazz.cast(fieldValue);

//                    Item hello = (clazz.getClass()) fieldValue;
//                    clazz docRef = (clazz) document.get(field);
//
//                    if (docRef != null) {
//                        Object resolvedObject = resolveReference(docRef, field.getType());
//                        field.set(tClass, resolvedObject); // Set the resolved object
//                    }
                }
                else{
                    System.out.println("Shitter");
                }
            }
        } else {
            System.out.println("No such document!");
        }

//        for (Field field : document.getData()) {
//            field.setAccessible(true); // Allow access to private fields
//
//            try {
//                Object fieldValue = field.get(tClass); // Get the current field's value
//
//                // Check if the field is a DocumentReference
//                if (fieldValue instanceof DocumentReference) {
//                    System.out.println("Have Doc Ref");
//                    DocumentReference docRef = (DocumentReference) document.get(field.getName());
//                    if (docRef != null) {
//                        Object resolvedObject = resolveReference(docRef, field.getType());
//                        field.set(tClass, resolvedObject); // Set the resolved object
//                    }
//                }
//
//                // Check if the field is a collection of DocumentReferences (List, etc.)
//                if (fieldValue instanceof List) {
//                    List<DocumentReference> docRefs = (List<DocumentReference>) document.get(field.getName());
//                    if (docRefs != null) {
//                        List<Object> resolvedObjects = new ArrayList<>();
//                        for (DocumentReference docRef : docRefs) {
//                            Object resolvedObject = resolveReference(docRef, getListType(field));
//                            resolvedObjects.add(resolvedObject);
//                        }
//                        field.set(tClass, resolvedObjects); // Set the resolved list of objects
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                System.err.println("Error accessing field: " + e.getMessage());
//            }
//        }
    }

    public static <T> T resolveReference(DocumentReference docRef, Class<T> tClass) throws ExecutionException, InterruptedException {
        if (docRef != null) {
            DocumentSnapshot snapshot = docRef.get().get();
            if (snapshot.exists()) {
                return snapshot.toObject(tClass);
            }
        }
        return null;
    }

    private static void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("Entered setField");
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
        System.out.println("Success setField");
    }

    private static Class<?> getListType(Field field) {
        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        return (Class<?>) listType.getActualTypeArguments()[0];
    }

    // ---------------

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
