package org.assignment.util;


public class Category {
    private String id;
    private String name;
    private String description;
    private Category parentCategory;
    private static int noOfCategory = 1;

    // Constructors

    public Category(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        noOfCategory++;
    }

    public Category(String id, String name, String description, Category parentCategory) {
        this.id = id;
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

    public Category getParentCategory() {
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

    public void setParentCategory(Category parentCategory) {
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

}
