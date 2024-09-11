package org.assignment.util.extended;

import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;
import org.assignment.util.enums.StaffPosition;
import java.time.LocalDate;
import java.util.HashMap;

public class Staff extends Person implements DatabaseOperations {
    protected char gender;
    protected StaffPosition position;
    protected LocalDate dateJoined;
    protected double salary;
    protected String password;

    public Staff() {
        this("", "", "", "", "", '\u0000', null, null, 0, "");
    }

    public Staff(String id, String name, String contactNumber, String email, String address, char gender, StaffPosition position, LocalDate dateJoined, double salary, String password) {
        super(id, name, contactNumber, email, address);
        this.gender = gender;
        this.position = position;
        this.dateJoined = dateJoined;
        this.salary = salary;
        this.password = password;
    }

    // setters
    public void setGender(char gender) { this.gender = gender; }
    public void setPosition(StaffPosition position) { this.position = position; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setPassword(String password) { this.password = password; }

    // getters
    public char getGender() { return this.gender; }
    public StaffPosition getPosition() { return this.position; }
    public LocalDate getDateJoined() { return this.dateJoined; }
    public double getSalary() { return this.salary; }
    public String getPassword() { return this.password; }


    // database operations implementation
    public boolean addToDb() {
        // add self to db
        return true;
    }

    public boolean getFromDb(String id) {
        // fetch using id and store in self
        return true;
    }

    public boolean updateToDb(String fieldName) {
        // update self to db
        return true;
    }

    public boolean deleteFromDb() {
        // delete from db where id is matching
        return true;
    }
}
