package org.assignment.util.base;

import org.assignment.util.validationCheck;

public abstract class Person {
    protected String id;
    protected String name;
    protected String contactNumber;
    protected String email;
    protected String address;

    public Person() { this("", "", "", "");}

    public Person(String name, String contactNumber, String email, String address) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    @Override
    public String toString(){
        // TODO: toString for Person
        // return "PRINT PERSON STUFF\n";
        return String.format("ID: %s\nName: %s\nContact Number: %s\nEmail: %s\nAddress: %s",
                this.id, this.name, this.contactNumber, this.email, this.address);
    }
}
