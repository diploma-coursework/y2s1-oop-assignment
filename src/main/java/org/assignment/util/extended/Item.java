package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseItem;
import org.assignment.util.base.DatabaseOperations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Item extends BaseItem implements DatabaseOperations {
    private ItemCategory itemCategory;
    private Supplier supplier;
    private static final CollectionReference itemCollection = FirebaseConnection.getDb().collection("Item");

    public Item() {
        this("", "", 0.0,0,null,null);
    }

    public Item(String id, String name, double unitPrice, int quantity, ItemCategory itemCategory, Supplier supplier) {
        super(name, unitPrice, quantity);
        this.itemCategory = itemCategory;
        this.supplier = supplier;
    }

    // setters
    public void setItemCategory(ItemCategory itemCategory) { this.itemCategory = itemCategory; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    // getters
    public ItemCategory getItemCategory() { return this.itemCategory; }
    public Supplier getSupplier() { return this.supplier; }

    // database operations implementation
    public boolean addToDb() {
        try {
            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("name", this.name);
            docValues.put("description", this.description);

            itemCategoryCollection.document(this.id).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean getFromDb(String id) {
        try {
            DocumentSnapshot doc = itemCategoryCollection.document(this.id).get().get();
            if (!doc.exists()) return false;

            Map<String, Object> docData = doc.getData();
            if (docData == null) return false;

            this.id = doc.getId();
            this.name = Optional.ofNullable(docData.get("name")).orElse("").toString();
            this.description = Optional.ofNullable(docData.get("description")).orElse("").toString();

            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean updateToDb(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            itemCategoryCollection.document(this.id).update(fieldName, field.get(this)).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteFromDb() {
        try {
            itemCategoryCollection.document(this.id).delete().get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
