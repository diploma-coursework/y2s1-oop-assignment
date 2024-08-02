package org.assignment.util.base;

public class BaseCategory {
    private String id;
    private String name;
    private String description;

    public BaseCategory(){
        this.id = null;
        this.name = null;
        this.description = null;
    }

    public BaseCategory(String id, String name, String description) {
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
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
}
