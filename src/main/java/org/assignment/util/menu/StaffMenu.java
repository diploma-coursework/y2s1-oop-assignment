package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.extended.Staff;
import org.assignment.util.validationCheck;


import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;


public class StaffMenu extends BaseMenu {

    private final HashMap<String, BaseMenu.Option> menuOptions = new HashMap<>();
    private Staff loggedInStaff;

    public StaffMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Signup", () -> restrictedAction(this::signUp, AccessLevel.ADMIN)));
        menuOptions.put("2", new Option("Login", this::Login));
        menuOptions.put("3", new Option("Delete", () -> restrictedAction(this::delete, AccessLevel.MANAGER)));
        menuOptions.put("4", new Option("Modify", () -> restrictedAction(this::modify, AccessLevel.MANAGER)));
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

            pause();
            clearScreen();

            System.out.println("Please enter ALL necessary informations");
            System.out.println("=======================================");

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            validationCheck.checkAndFormatName(name);


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
            String inputAddress = scanner.nextLine();
            String address = validationCheck.checkAddress(inputAddress);

            System.out.print("Enter Salary: ");
            int inputSalary = scanner.nextInt();
            int salary = validationCheck.checkSalary(inputSalary);


            System.out.print("Enter a password: ");
            String inputPassword = scanner.nextLine();
            String password = validationCheck.checkPassword(inputPassword);

            LocalDate dateJoined = LocalDate.now(); // Set the date joined to the current date

            Staff staff = new Staff("",name, contactNumber, email, address, gender, position, dateJoined, salary, password);


        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void Login() {
        // Logic to handle login
        // For demonstration, after login, we store the staff in loggedInStaff variable
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Id: ");
        String id = scanner.next();

        System.out.print("Enter Password: ");
        String password = scanner.next();


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

    private Map<String, Object> convertStaffToMap(Staff staff) {

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

    private void restrictedAction(Runnable action, AccessLevel requiredAccessLevel) {
        if (loggedInStaff != null) {
            // Check if the logged-in staff's access level is equal to or higher than the required access level
            if (loggedInStaff.getPosition().getAccessLevel().ordinal() >= requiredAccessLevel.ordinal()) {
                action.run();
            } else {
                System.out.println("Access Denied: Insufficient privileges.");
            }
        } else {
            System.out.println("No staff is currently logged in. Please log in first.");
        }
    }


}
