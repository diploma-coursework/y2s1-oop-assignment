package org.assignment.util.base;

import java.util.ArrayList;

abstract public class BaseCategory {
    protected String id;
    protected String name;
    protected String description;
    protected ArrayList<BaseItem> items;
    protected ArrayList<BaseCategory> categories;
    protected boolean isParent = false;

    public BaseCategory(){
        this(null, null, null,false);
    }

    public BaseCategory(String id, String name, String description,boolean isParent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isParent = isParent;
        this.items = new ArrayList<>();
        this.categories = new ArrayList<>();
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
        if (!isParent) {
            this.items = items;
        }
    }
    public void setParentCategory(ArrayList<BaseCategory> category) {
        if (isParent){
            this.categories = category;
        }
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
        if(!isParent){
        return this.items;
        }
        else{
            return null;
        }
    }
    public ArrayList<BaseCategory> getCategories(){
        if(isParent) {
            return this.categories;
        }
        else{
            return null;
        }
    }
}
