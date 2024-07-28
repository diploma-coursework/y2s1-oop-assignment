package org.assignment.util;

public class Staff {
    private String id;
    private String name;
    private char gender;
    private String position;
    private String contactNumber;
    private String email;
    private String dateJoined;
    private int salary;
    private String address;

    // Constructor
    public Staff(String id, String name, char gender, String position, String contactNumber, String email, String dateJoined, int salary, String address) {
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

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
/*  @Override
    public String toString() {
        return "Staff{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", position='" + position + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                ", dateJoined='" + dateJoined + '\'' +
                ", salary=" + salary +
                ", address='" + address + '\'' +
                '}';
    }
*/
    @Override
    public String toString() {
        // Initialize the base string
        StringBuilder sb = new StringBuilder("Staff{");

        // Add ID, name, and other fields
        sb.append("id='").append(id).append('\'').append(", name='").append(name).append('\'');

        // Conditional formatting for gender
        if (gender == 'M') {
            sb.append(", gender=Male");
        } else if (gender == 'F') {
            sb.append(", gender=Female");
        } else {
            sb.append(", gender=Not specified");
        }

        // Add position and contact number
        sb.append(", position='").append(position).append('\'').append(", contactNumber= Not provided").append(contactNumber).append('\'');

        // Conditional formatting for email
        if (email != null && !email.isEmpty()) {
            sb.append(", email='").append(email).append('\'');
        } else {
            sb.append(", email=Not provided");
        }

        // Add dateJoined
        sb.append(", dateJoined='").append(dateJoined).append('\'');

        // Conditional formatting for salary
        if (salary > 0) {
            sb.append(", salary=").append(salary);
        } else {
            sb.append(", salary=0");
        }

        // Add address
        sb.append(", address='").append(address).append('\'');

        // Close the string
        sb.append('}');

        return sb.toString();
    }
}