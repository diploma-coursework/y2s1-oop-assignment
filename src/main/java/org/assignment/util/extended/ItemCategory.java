package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemCategory implements DatabaseOperations {
    private String id;
    private String name;
    private String description;
    private static final CollectionReference itemCategoryCollection = FirebaseConnection.getDb().collection("ItemCategory");

    public ItemCategory() {
        this("", "", "");
    }

    public ItemCategory(String id, String name, String description) {
        this.id = id;
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
    public String getName() {
        return this.name;
    }
    public String getDescription() { return this.description;}

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

    // features
    public ArrayList<Item> getItems() {
        return new ArrayList<>();
    }
}
