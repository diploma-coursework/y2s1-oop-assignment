package org.assignment.util;

import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDate;
import org.assignment.util.enums.StaffPosition;

// Staff data field validations utility

public class validationCheck {

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        name = name.trim(); // Trim leading/trailing spaces

        // Check length constraints
        if (name.length() < 2 || name.length() > 50) {
            return false;
        }

        // Check for valid characters
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            return false;
        }

        // Ensure no consecutive spaces, hyphens, or apostrophes
        if (name.matches(".*[\\s'-]{2,}.*")) {
            return false;
        }

        // Ensure no digits are in the name
        if (name.matches(".*\\d.*")) {
            return false;
        }

        return true;
    }

    public static String checkAndFormatName(String name) {


        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        name = name.trim(); // Trim leading/trailing spaces

        if (name.length() < 2 || name.length() > 50) {
            throw new IllegalArgumentException("Name must be between 2 and 50 characters.");
        }

        // Check for valid characters (letters, spaces, hyphens, and apostrophes)
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException("Name can only contain letters, spaces, hyphens, or apostrophes.");
        }

        // Ensure no consecutive spaces, hyphens, or apostrophes
        if (name.matches(".*[\\s'-]{2,}.*")) {
            throw new IllegalArgumentException("Name cannot contain consecutive spaces, hyphens, or apostrophes.");
        }

        // Ensure no digits are in the name
        if (name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Name cannot contain digits.");
        }

        // Format name: Capitalize the first letter of each word

        String[] words = name.split("\\s+");  // Split by spaces
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0))) // Capitalize the first letterch
                        .append(word.substring(1).toLowerCase())        // Make the rest of the word lowercase
                        .append(" ");                                   // Add a space
            }
        }


        return formattedName.toString().trim();  // Remove trailing space
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
        // Example of Valid Code
//        0123456789 +60123456789 01112345678 +601112345678 010-2345678
        if (!contactNumber.matches("^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)1-*[0-9]{8}$")) {
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
        if (address == null) {
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
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty!");
        }

        // Check for minimum length
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        // Check if it contains at least one digit
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit (0-9).");
        }

        // Check if it contains at least one special character
        if (!password.matches(".*[@$!%*#?&].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character from the set @$!%*#?&.");
        }

        // Hash the password using BCrypt
        //return BCrypt.hashpw(password, BCrypt.gensalt());
        return password;
    }

    public static void checkDateJoined(LocalDate dateJoined) {
        if (dateJoined == null || dateJoined.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date joined cannot be null or a future date.");
        }
    }

    public static boolean isValidPosition(String position) {
        for (StaffPosition pos : StaffPosition.values()) {
            if (pos.name().equalsIgnoreCase(position)) {
                return true;
            }
        }
        return false;
    }
}
