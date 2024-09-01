package org.assignment.util.base;

import org.assignment.util.enums.StaffPosition;
import java.time.LocalDate;

abstract public class BaseStaff {
    protected String id;
    protected String name;
    protected char gender;
    protected StaffPosition position;
    protected String contactNumber;
    protected String email;
    protected LocalDate dateJoined;
    protected int salary;
    protected String address;

    // constructors
    public BaseStaff() {
        this(null, null, '\u0000', null, null, null, null, 0, null);
    }

    public BaseStaff(String id, String name, char gender, StaffPosition position, String contactNumber, String email, LocalDate dateJoined, int salary, String address) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.contactNumber = contactNumber;
        this.email = email;
        this.dateJoined = dateJoined;
        this.salary = salary;
        this.address = address;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGender(char gender) {
        this.gender = gender;
    }
    public void setPosition(StaffPosition position) {
        this.position = position;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setEmail(String email) {
        if(email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }
    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }
    public void setSalary(int salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public char getGender() {
        return this.gender;
    }
    public StaffPosition getPosition() {
        return this.position;
    }
    public String getContactNumber() {
        return this.contactNumber;
    }
    public String getEmail() {
        return this.email;
    }
    public LocalDate getDateJoined() {
        return this.dateJoined;
    }
    public int getSalary() {
        return this.salary;
    }
    public String getAddress() {
        return this.address;
    }

    // REQUIRED operation to make sure the object have important data that represent them BEFORE storing into firestore
    public boolean isValid() {
        return name != null && !name.isEmpty() &&
                email != null && !email.isEmpty() &&
                position != null;
    }

    // make the class more functional and easier to work with
    public void increaseSalary(int increment) {
        if (increment > 0) {
            this.salary += increment;
        }
    }

    public void updateContactDetails(String newContactNumber, String newEmail) {
        setContactNumber(newContactNumber);
        setEmail(newEmail);
    }

    @Override
    public String toString() {
        return String.format("Staff[id=%s, name=%s, gender=%c, position=%s, contactNumber=%s, email=%s, dateJoined=%s, salary=%d, address=%s]", id, name, gender, position, contactNumber, email, dateJoined, salary, address);
    }
}