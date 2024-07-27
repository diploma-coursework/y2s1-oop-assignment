package org.assignment.util;

public class Category {
    private String id;
    private String name;
    private String description;
    private Category parentCategory;
    private static int noOfCategory = 1;

    public Category(){
        noOfCategory++;
    }

    public Category(String id, String name, String description, Category parentCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategory = parentCategory;
        noOfCategory++;
    }
}
