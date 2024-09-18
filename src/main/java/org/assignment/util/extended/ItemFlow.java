package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.assignment.util.DbMethods;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class ItemFlow implements DatabaseOperations {
    private Supplier supplier;
    private Item item;
    private int delta;
    private LocalDate updateTime;
    private static final CollectionReference itemFlowCategory = FirebaseConnection.getDb().collection("ItemFlow");

    public ItemFlow() {
        this(null, null, 0, LocalDate.now());
    }

    public ItemFlow(Supplier supplier, Item item, int delta, LocalDate updateTime) {
        this.supplier = supplier;
        this.item = item;
        this.delta = delta;
        this.updateTime = updateTime;
    }

    // setters
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public boolean setSupplier(Supplier supplier, boolean updateToDb) {
        this.supplier = supplier;
        if (updateToDb) return this.updateToDb("supplier");
        return false;
    }

    public void setItem(Item item) { this.item = item; }

    public boolean setItem(Item item, boolean updateToDb) {
        this.item = item;
        if (updateToDb) return this.updateToDb("item");
        return false;
    }

    public void setDelta(int delta) { this.delta = delta; }

    public boolean setDelta(int delta, boolean updateToDb) {
        this.delta = delta;
        if (updateToDb) return this.updateToDb("delta");
        return false;
    }

    public void setUpdateTime(LocalDate updateTime) { this.updateTime = updateTime; }

    public boolean setUpdateTime(LocalDate updateTime, boolean updateToDb) {
        this.updateTime = updateTime;
        if (updateToDb) return this.updateToDb("updateTime");
        return false;
    }

    // getters
    public Supplier getSupplier(){ return this.supplier;}
    public Item getItem() { return this.item; }
    public int getDelta() { return this.delta;}
    public LocalDate getUpdateTime() { return this.updateTime;}
    public static CollectionReference getItemFlowCategory() { return ItemFlow.itemFlowCategory; }

    // database operations implementation
    public boolean addToDb() {
        try {
            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("name", this.name);
            docValues.put("description", this.description);

            this.id = DbMethods.dbCreateNewId("ItemCategory","CAT",4);
            itemCategoryCollection.document(this.id).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static ItemCategory getFromDb(String id) {
        try {
            // get from cache if available
            ItemCategory cachedItemCategoryObject = (ItemCategory) FirebaseConnection.getCachedDocument(itemCategoryCollection, itemCategoryCollection.document(id));
            if (cachedItemCategoryObject != null) return cachedItemCategoryObject;

            // fetch from db
            DocumentSnapshot doc = itemCategoryCollection.document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> docData = doc.getData();
            if (docData == null) return null;

            // create new instance and assign values
            ItemCategory fetchedItemCategory = new ItemCategory();
            fetchedItemCategory.setId(doc.getId());
            fetchedItemCategory.setName(Optional.ofNullable(docData.get("name")).orElse("").toString());
            fetchedItemCategory.setDescription(Optional.ofNullable(docData.get("description")).orElse("").toString());

            // cache and return
            FirebaseConnection.cacheNewDocument(itemCategoryCollection, itemCategoryCollection.document(id), fetchedItemCategory);
            return fetchedItemCategory;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
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
            FirebaseConnection.deleteCachedDocument(itemCategoryCollection, itemCategoryCollection.document(this.id));
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static List<ItemCategory> getAllItemCategory() {
        try {
            List<QueryDocumentSnapshot> documents = ItemCategory.itemCategoryCollection.get().get().getDocuments();

            List<ItemCategory> itemCategories = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                itemCategories.add(ItemCategory.getFromDb(doc.getId()));
            }

            return itemCategories;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String toString() {
        StringBuilder returnString = new StringBuilder();

        returnString
                .append("Id         : ").append(ansi().fgYellow().a(this.getId()).reset().toString()).append("\n")
                .append("Name       : ").append(ansi().fgBlue().a(this.getName()).reset().toString()).append("\n")
                .append("Description: ").append(ansi().fgBrightMagenta());

        // do text wrap for description with alignment
        String[] tokenizedDescription = this.getDescription().split(" ");
        int currentLineLength = 0;

        for (String word : tokenizedDescription) {
            if (currentLineLength > 50) {
                returnString.append("\n             ");
                currentLineLength = 0;
            }

            returnString.append(word).append(" ");
            currentLineLength += word.length() + 1 ;
        }
        returnString.append(ansi().reset()).append("\n");
        return returnString.toString();
    }
}
