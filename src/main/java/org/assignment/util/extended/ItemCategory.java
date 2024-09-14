package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.assignment.util.DbMethods;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;

import java.lang.reflect.Field;
import java.util.*;

public class ItemCategory implements DatabaseOperations {
    private String id;
    private String name;
    private String description;
    private static final CollectionReference itemCategoryCollection = FirebaseConnection.getDb().collection("ItemCategory");

    public ItemCategory() {
        this("", "");
    }

    public ItemCategory(String name, String description) {
//        this.id = id;
        this.name = name;
        this.description = description;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean setName(String name, boolean updateToDb) {
        this.name = name;
        if (updateToDb) return this.updateToDb("name");
        return false;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean setDescription(String description, boolean updateToDb) {
        this.description = description;
        if (updateToDb) return this.updateToDb("description");
        return false;
    }

    // getters
    public String getId(){ return this.id;}
    public String getName() { return this.name; }
    public String getDescription() { return this.description;}
    public static CollectionReference getItemCategoryCollection() { return ItemCategory.itemCategoryCollection; }

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

    public String toString(){
        return String.format("""
                ITEMCATEGORY ID: %s
                ITEMCATEGORY NAME: %s
                ITEMCATEGORY DESCRIPTION: %s
                """, this.getId(), this.getName(), this.getDescription());
    }
}
