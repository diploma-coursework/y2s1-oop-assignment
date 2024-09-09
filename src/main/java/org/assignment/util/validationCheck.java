package org.assignment.util;

import org.mindrot.jbcrypt.BCrypt;

// Staff data field validations utility

public class validationCheck {

    public static void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        if (name.length() < 2 || name.length() > 50) {
            throw new IllegalArgumentException("Name cannot be too short or too long!");
        }

        // Check for valid characters (only letters, spaces, hyphens, and apostrophes)
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException("Name contains invalid characters!");
        }

        // Ensure first letter is uppercase
        if (!Character.isUpperCase(name.charAt(0))) {
            throw new IllegalArgumentException("First letter must be uppercase!");
        }

        // Check for consecutive spaces, hyphens, or apostrophes
        if (name.matches(".*[\\s'-]{2,}.*")) {
            throw new IllegalArgumentException("Name contains consecutive special characters (spaces, hyphens, or apostrophes)!");
        }

        // Ensure no digits are in the name
        if (name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Name cannot contain digits!");
        }
    }

    public static String checkGender(char genderInput) {
        if (genderInput == 'M') {
            return "Male";
        } else if (genderInput == 'F') {
            return "Female";
        } else {
            throw new IllegalArgumentException("Invalid gender input. Please enter 'M' or 'F'.");
        }
    }

    public static void checkContactNumber(String contactNumber) {
        if (!contactNumber.matches("^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$")) {
            throw new IllegalArgumentException("Invalid contact number format");
        }
    }

    // Validate the email
    public static void checkEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    // Validate the address
    public static String checkAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        return address;
    }

    // Validate the salary
    public static int checkSalary(int salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        return salary;
    }

    // Validate and hash the password
    public static String checkPassword(String password) {
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

   public static int checkAge(int age) {
        if (age < 18) {
            throw new IllegalArgumentException("Age cannot be under 18");
        }
        return age;
   }
}
