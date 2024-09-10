package org.assignment.util.extended;

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
}
