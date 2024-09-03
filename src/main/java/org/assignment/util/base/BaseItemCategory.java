package org.assignment.util.base;

abstract public class BaseItemCategory {
    protected String id;
    protected String name;
    protected String description;

    public BaseItemCategory(){
        this(null, null);
    }

    public BaseItemCategory(String name, String description) {
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

}
