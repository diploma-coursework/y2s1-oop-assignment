package org.assignment.util.extended;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseStaff;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.extended.Staff;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;


public class StaffMenu extends BaseMenu {

    private final HashMap<String, BaseMenu.Option> menuOptions = new HashMap<>();

    public StaffMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Signup", this::signUp));
        menuOptions.put("2", new Option("Login", this::Login));
        menuOptions.put("3", new Option("delete", this::delete));
        menuOptions.put("4", new Option("Quit", this::modify));
        menuOptions.put("5", new Option("Display List of staffs", this::view));
        menuOptions.put("q", new Option("Quit", this::quit));
    }

    @Override
    public void start() {
        while (true) {
            displayOptions(menuOptions);
            getUserOptions(menuOptions);
        }
    }

    private void signUp() {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }

            System.out.print("Enter Gender (M/F): ");
            char genderInput = scanner.next().toUpperCase().charAt(0);
            // Validate and map gender input to a string
            String gender;
            if (genderInput == 'M') {
                gender = "Male";
            } else if (genderInput == 'F') {
                gender = "Female";
            } else {
                throw new IllegalArgumentException("Invalid gender input. Please enter 'M' or 'F'.");
            }

            System.out.print("Enter Position (e.g., INVENTORY_CLERK): ");
            StaffPosition position = StaffPosition.valueOf(scanner.next().toUpperCase());

            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.next();

            if (!contactNumber.matches("^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$")) {
                throw new IllegalArgumentException("Invalid email format");
            }

            System.out.print("Enter Email: ");
            String email = scanner.next();

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid email format");
            }

            scanner.nextLine(); // Consume newline

            System.out.print("Enter Address: ");
            String address = scanner.nextLine();

            System.out.print("Enter Salary: ");
            int salary = scanner.nextInt();

            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }

            System.out.print("Enter a password: ");
            String inputPassword = scanner.nextLine();

            if (inputPassword.length() < 5) {
                throw new IllegalArgumentException("The password length must be 4 above!");
            } else if(!inputPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
                throw new IllegalArgumentException("The password is too weak! Consider adding the following requirement: " +
                        "At least one alphabetic character (A-Z or a-z).\n" +
                        "At least one digit (0-9).\n" +
                        "At least one special character from the set @$!%*#?&.\n" +
                        "Minimum length of 8 characters.");
            }
            String password = BCrypt.hashpw(inputPassword, BCrypt.gensalt());


            LocalDate dateJoined = LocalDate.now(); // Set the date joined to the current date

            // Create a Staff object
            Staff newStaff = new Staff(name, gender, position, contactNumber, email, dateJoined, salary, address, password);

            // Validate staff object
            if (newStaff.isValid()) {
                // Write to Firestore
                newStaff.writeToDb();
                System.out.println("Staff successfully signed up and saved to the database.");
            } else {
                System.out.println("Failed to sign up: Invalid staff data.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void Login() {
        System.out.print("Enter Id: ");
    }

    private void delete() {
    }

    private void modify() {
    }

    private void view() {
    }

    private void quit() {
        System.out.println("Quit");
        System.exit(0);
    }

    private static boolean validAccount(String iDString, String passString) {
        // Check from database
        // Placeholder logic
        return iDString.equalsIgnoreCase("AAA") && passString.equals("AAA");
    }

    private Map<String, Object> convertStaffToMap(BaseStaff staff) {

        Map<String, Object> staffData = new HashMap<>();

        staffData.put("name", staff.getName());
        staffData.put("gender", String.valueOf(staff.getGender())); // Convert char to string
        staffData.put("position", staff.getPosition().name()); // Enum to string
        staffData.put("contactNumber", staff.getContactNumber());
        staffData.put("email", staff.getEmail());
        staffData.put("dateJoined", staff.getDateJoined().toString()); // LocalDate to string
        staffData.put("salary", staff.getSalary());
        staffData.put("address", staff.getAddress());

        return staffData;
    }
}
