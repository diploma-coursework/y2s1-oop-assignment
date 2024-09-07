package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.base.BaseStaff;
import org.assignment.util.extended.Staff;
import org.assignment.util.Utility.validationCheck;

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
            validationCheck.checkEmail(name);


            System.out.print("Enter Gender (M/F): ");
            char genderInput = scanner.next().toUpperCase().charAt(0);
            String gender = validationCheck.checkGender(genderInput);


            System.out.print("Enter Position (e.g., INVENTORY_CLERK): ");
            StaffPosition position = StaffPosition.valueOf(scanner.next().toUpperCase());


            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.next();
            validationCheck.checkContactNumber(contactNumber);


            System.out.print("Enter Email: ");
            String email = scanner.next();
            validationCheck.checkEmail(email);


            scanner.nextLine(); // Consume newline


            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            validationCheck.checkAddress(address);

            System.out.print("Enter Salary: ");
            int inputSalary = scanner.nextInt();
            int salary = validationCheck.checkSalary(inputSalary);


            System.out.print("Enter a password: ");
            String inputPassword = scanner.nextLine();
            String password = validationCheck.checkPassword(inputPassword);


            System.out.print("enter age");
            int inputAge = scanner.nextInt();
            int age = validationCheck.checkAge(inputAge);


            LocalDate dateJoined = LocalDate.now(); // Set the date joined to the current date

            // Create a Staff object
            Staff newStaff = new Staff(name, gender, position, contactNumber, email, dateJoined, salary, address, password, age);

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
