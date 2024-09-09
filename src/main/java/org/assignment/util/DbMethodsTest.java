package org.assignment.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DbMethodsTest {
    public static Firestore db = FirebaseConnection.getDb();

    // TODO: Fix deserialized
    public static <T> T deserialized(String id, Class<T> tClass) {
        ApiFuture<DocumentSnapshot> future = db.collection(tClass.getSimpleName()).document(id).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
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
            Map<String, Object> fields = document.getData();

            // Iterate over the Map entries
            assert fields != null;
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                String field = entry.getKey();
                Object fieldValue = entry.getValue();

                // Print field name and value
                System.out.println("Field name: " + field);
                System.out.println("Field value: " + fieldValue);

                if (fieldValue instanceof DocumentReference docRef) {
                    // DocumentReference docRef = (DocumentReference) fieldValue; same, declared differently.

                    // personal data + StringID of Reference class
                    DocumentSnapshot refDocument = docRef.get().get();

                    // Based on the field name, determine the class to deserialize into
                    if (field.equals("ItemCategory")) {
                        // TODO: in Item, itemCategory is stored as DocumentReference.
                        Map<String, Object> data = refDocument.getData();
                        if (data != null) {
                            ItemCategory itemCategory = new ItemCategory();
                            itemCategory.setName((String) data.get("name"));
                            itemCategory.setDescription((String) data.get("description"));
                            for (String item : (List<String>) data.get("Item")) {
                                itemCategory.addItem(item);
                            }
                            fields.replace("ItemCategory", itemCategory);
                        }
                    } else if (field.equals("Supplier")) {
                        // TODO: in Item, itemCategory is stored as DocumentReference.
                        Map<String, Object> data = refDocument.getData();
                        if (data != null) {
                            Supplier supplier = new Supplier();
                            supplier.setName((String) data.get("name"));
                            supplier.setName((String) data.get("address"));
                            supplier.setName((String) data.get("contactNumber"));
                            supplier.setName((String) data.get("email"));
                            for (String item: (List<String>) data.get("Item")){
                                supplier.addItem(item);
                            }
                            fields.replace("Supplier",supplier);
                        }
                    }
                }
            }
        } else {
            System.out.println("No such document!");
        }
    }
}
