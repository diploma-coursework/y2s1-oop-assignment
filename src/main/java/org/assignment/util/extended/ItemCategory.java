package org.assignment.util.extended;

import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.base.DatabaseOperations;

import java.util.ArrayList;

public class ItemCategory implements DatabaseOperations {
    protected String id;
    protected String name;
    protected String description;

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
    public void setDescription(String description) {
        this.description = description;
    }

    // getters
    public String getId(){ return this.id;}
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }

    public ArrayList<Item> getItems() {
        return new ArrayList<>();
    }

    public static ItemCategory fromDocumentSnapshot(DocumentSnapshot docSnapshot) {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setId(docSnapshot.getId());
        itemCategory.setName(docSnapshot.get("name").toString());
        itemCategory.setDescription(docSnapshot.get("description").toString());
        return itemCategory;
    }

    // Database Operation Methods
    public boolean addToDb() {
        // add self to db
        return true;
    }

    public boolean getFromDb(String id) {
        // fetch using id and store in self
        return true;
    }

    public boolean updateToDb() {
        // update self to db
        return true;
    }

    public boolean deleteFromDb(String id) {
        // delete from db where id is matching
        return true;
    }
}
