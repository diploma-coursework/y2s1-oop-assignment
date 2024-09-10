package org.assignment.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Staff;
import org.assignment.util.extended.Supplier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DbMethodsTest {
    public static Firestore db = FirebaseConnection.getDb();

    public static <T> T getDocAsClassObject(String id, Class<T> tClass) {
        try {
            // get requested document snapshot
            DocumentSnapshot document = db.collection(tClass.getSimpleName()).document(id).get().get();
            if (!document.exists()) return null;

            // get document references
            Map<String, Object> documentData = document.getData();
            if (documentData == null) return null;

            for (Map.Entry<String, Object> entry : documentData.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                System.out.printf("%s    %s: %s\n", tClass.getSimpleName(), key, value); // TEMP

                if (value instanceof DocumentReference) {
                    documentData.remove(key);
//                    DocumentSnapshot referencedDoc = ((DocumentReference) value).get().get();
//                    documentData.put(key, getDocAsClassObject(referencedDoc.getId(), getClassFromName(key)));
                }
            }
            System.out.println("here x");
            System.out.println(documentData);
            System.out.println("here y");


            // convert Map data to provided T class
            T instance = tClass.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : documentData.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.printf("%s : %s\n", key, value);
                System.out.println("here 1");

                try {
                    System.out.println("here 2");
                    Field field = tClass.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(instance, value);
                    System.out.println("here 3");

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.printf("Error setting %s.%s", tClass.getSimpleName(), e.getMessage());
                }
            }
            return instance;
        } catch (Exception e) {
            System.err.println("Error getting document: " + e.getMessage());
            return null;
        }
    }

    private static Class<?> getClassFromName(String name) {
        return switch (name) {
            case "Item" ->  Item.class;
            case "ItemCategory" -> ItemCategory.class;
            case "Staff" -> Staff.class;
            case "Supplier" -> Supplier.class;
            default -> null;
        };
    }

}
