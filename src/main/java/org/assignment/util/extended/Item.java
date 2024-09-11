package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseItem;
import org.assignment.util.base.DatabaseOperations;

import javax.swing.text.Document;
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
    public static CollectionReference getItemCollection() { return Item.itemCollection; }

    // database operations implementation
    public boolean addToDb() {
        try {
            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("name", this.name);
            docValues.put("unitPrice", this.unitPrice);
            docValues.put("quantity", this.quantity);
            docValues.put("itemCategory", ItemCategory.getItemCategoryCollection().document(this.itemCategory.getId()));
            docValues.put("supplier", Supplier.getSupplierCollection().document(this.supplier.getId()));

            itemCollection.document(this.id).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static Item getFromDb(String id) {
        try {
            // get from cache if available
            Item cachedItemObject = (Item) FirebaseConnection.getCachedDocument(itemCollection, itemCollection.document(id));
            if (cachedItemObject != null) return cachedItemObject;

            // fetch from db
            DocumentSnapshot doc = itemCollection.document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> docData = doc.getData();
            if (docData == null) return null;

            // create new instance and assign values
            Item fetchedItem = new Item();
            fetchedItem.setId(doc.getId());
            fetchedItem.setName(Optional.ofNullable(docData.get("name").toString()).orElse(""));
            fetchedItem.setUnitPrice(Double.parseDouble(Optional.ofNullable(docData.get("unitPrice")).orElse(0.0).toString()));
            fetchedItem.setQuantity(Integer.parseInt(Optional.ofNullable(docData.get("quantity")).orElse(0).toString()));
            fetchedItem.setItemCategory(ItemCategory.getFromDb(((DocumentReference) docData.get("itemCategory")).getId()));
            fetchedItem.setSupplier(Supplier.getFromDb(((DocumentReference) docData.get("supplier")).getId()));

            // cache and return
            FirebaseConnection.cacheNewDocument(itemCollection, itemCollection.document(id), fetchedItem);
            return fetchedItem;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateToDb(String fieldName) { // TODO: method body
        return true;
//        try {
//            Field field = this.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//
//            itemCategoryCollection.document(this.id).update(fieldName, field.get(this)).get();
//            return true;
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return false;
//        }
    }

    public boolean deleteFromDb() { // TODO: method body
        return true;
//        try {
//            itemCategoryCollection.document(this.id).delete().get();
//            return true;
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return false;
//        }
    }
}
