package org.assignment.util;

class BaseCategory {
    private String id;
    private String name;
    private String description;
    private BaseCategory parentCategory;
    private static int noOfCategory = 1;

    // Constructors

    public BaseCategory(){
        // ID is auto generated
        this.name = "0";
        this.description = "0";
        noOfCategory++;
    }

    public BaseCategory( String name, String description) {
        // ID is auto generated
        this.name = name;
        this.description = description;
        noOfCategory++;
    }

    public BaseCategory( String name, String description, BaseCategory parentCategory) {
        // ID is auto generated
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

}
