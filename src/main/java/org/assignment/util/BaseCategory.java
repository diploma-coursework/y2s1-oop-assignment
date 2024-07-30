package org.assignment.util;


import com.google.firebase.database.annotations.NotNull;

public class BaseCategory {
    private String id;
    private String name;
    private String description;
    private BaseCategory parentCategory;
    private static int noOfCategory = 1;

    // Constructors

    public BaseCategory(){
        this.id = StringId("CAT");
        this.name = "0";
        this.description = "0";
        noOfCategory++;
    }

    public BaseCategory(String id, String name, String description) {
        this.id = StringId("CAT");
        this.name = name;
        this.description = description;
        noOfCategory++;
    }

    public BaseCategory(String id, String name, String description, BaseCategory parentCategory) {
        this.id = StringId("CAT");
        this.name = name;
        this.description = description;
        this.parentCategory = parentCategory;
        noOfCategory++;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BaseCategory getParentCategory() {
        return parentCategory;
    }

    public static int getNoOfCategory() {
        return noOfCategory;
    }

    // Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentCategory(BaseCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    // Annotations

    @Override
    public String toString(){
        if(getParentCategory() == null){
            return "Category ID : "+getId()+"\nCategory Name : "+getName()+"\nCategory Description : "+getDescription()+"\n";
        }
        else{
            return "Category ID : "+getId()+"\nCategory Name : "+getName()+"\nCategory Description : "+getDescription()+"\nParent Category Name"+getParentCategory().getName()+"\n";
        }
    }

    @NotNull
    protected String StringId(String identifier){
        return identifier + String.format("%04d",getNoOfCategory());
    }

    // Methods

    // Menu Stuff

}
