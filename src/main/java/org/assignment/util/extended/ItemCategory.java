package org.assignment.util.extended;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ItemCategory {
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
}
