package org.assignment.util.extended;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseStaff;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.extended.Staff;

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
            // Collect staff details with validation
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }

            System.out.print("Enter Gender (M/F): ");
            char gender = scanner.next().toUpperCase().charAt(0);
            if (gender != 'M' && gender != 'F') {
                throw new IllegalArgumentException("Invalid gender");
            }

            System.out.print("Enter Position (e.g., INVENTORY_CLERK): ");
            StaffPosition position = StaffPosition.valueOf(scanner.next().toUpperCase());

            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.next();

            System.out.print("Enter Email: ");
            String email = scanner.next();
            if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
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

            LocalDate dateJoined = LocalDate.now(); // Set the date joined to the current date

            // Create a Staff object
            BaseStaff newStaff = new Staff(null, name, gender, position, contactNumber, email, dateJoined, salary, address);

            // Validate staff object
            if (newStaff.isValid()) {
                // Convert to a map and write to Firestore
                Map<String, Object> staffData = convertStaffToMap(newStaff);
                DbMethods.writeToDb(staffData, "staff", "STF", 5);
                System.out.println("Staff successfully signed up and saved to the database.");
            } else {
                System.out.println("Failed to sign up: Invalid staff data.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void Login() {
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
