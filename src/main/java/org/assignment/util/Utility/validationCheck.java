package org.assignment.util.Utility;

import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

// Don't delete , I try to integrate the validation function here so the StaffMenu will be more easy to maintain

public class validationCheck {

    public void checkName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    public String checkGender(char genderInput) {
        if (genderInput == 'M') {
            return "Male";
        } else if (genderInput == 'F') {
            return "Female";
        } else {
            throw new IllegalArgumentException("Invalid gender input. Please enter 'M' or 'F'.");
        }
    }
    public void checkContactNumber(String contactNumber) {
        if (!contactNumber.matches("^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$")) {
            throw new IllegalArgumentException("Invalid contact number format");
        }
    }

    // Validate the email
    public void checkEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    // Validate the address
    public String checkAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        return address;
    }

    // Validate the salary
    public int checkSalary(int salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        return salary;
    }

    // Validate and hash the password
    public String checkPassword(String password) {
        if (password.length() < 5) {
            throw new IllegalArgumentException("The password length must be 5 characters or more!");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new IllegalArgumentException("The password is too weak! Consider adding the following requirement: " +
                    "At least one alphabetic character (A-Z or a-z).\n" +
                    "At least one digit (0-9).\n" +
                    "At least one special character from the set @$!%*#?&.\n" +
                    "Minimum length of 8 characters.");
        }
        // Hash the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
