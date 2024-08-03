package org.assignment.util.base;

import java.util.ArrayList;

public class BaseCategory {
    private String id;
    private String name;
    private String description;
    private ArrayList<BaseItem> items;

    public BaseCategory(){
        this(null, null, null);
    }

    public BaseCategory(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
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
    public void setItems(ArrayList<BaseItem> items) {
        this.items = items;
    }
    // getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public ArrayList<BaseItem> getItems() {
        return this.items;
    }
}
