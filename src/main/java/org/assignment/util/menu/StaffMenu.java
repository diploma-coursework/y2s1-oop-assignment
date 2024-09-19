package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.extended.Staff;
import org.assignment.util.ValidationCheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;


public class StaffMenu extends BaseMenu {

    private final Staff loggedInStaff;
    private final HashMap<String, BaseMenu.Option> MenuOptions = new HashMap<>(),
            AdminMenuOptions = new HashMap<>(),
            ManagerMenuOption = new HashMap<>(),
            StaffMenuOptions = new HashMap<>();

    public StaffMenu(Staff loggedInStaff) {

        this.loggedInStaff = loggedInStaff;

        // Initialize menu options using createOption
        MenuOptions.put("1", new Option("Admin Menu", this::adminMenu));
        MenuOptions.put("2", new Option("Manager menu", this::managerMenu));
        MenuOptions.put("3", new Option("Staff relevant menu", this::staffRelevantMenu));
        MenuOptions.put("0", new Option(ansi().fgRed().a("Quit").reset().toString(), this::quitMenu));

        //admin functionality
        AdminMenuOptions.put("1", new Option("Signup staff", () -> restrictedAction(this::signUpStaff, AccessLevel.ADMIN)));
        AdminMenuOptions.put("2", new Option("Delete staff document", () -> restrictedAction(this::deleteStaff, AccessLevel.ADMIN)));
        AdminMenuOptions.put("3", new Option("Modify staff document", () -> restrictedAction(this::modifyStaff, AccessLevel.ADMIN)));
        AdminMenuOptions.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), (this::exit)));

        //Manager or a supervisor statistical view of employee
        ManagerMenuOption.put("4", new Option("View staffs", () -> restrictedAction(this::viewStaff, AccessLevel.MANAGER)));
        ManagerMenuOption.put("5", new Option("Search staff document", () -> restrictedAction(this::searchStaff, AccessLevel.MANAGER)));
        ManagerMenuOption.put("6", new Option("view Employee salary distribution", () -> restrictedAction(this::viewSalaryDistribution, AccessLevel.MANAGER)));
        ManagerMenuOption.put("7", new Option("View Employee genders distribution", () -> restrictedAction(this::viewGenderDistribution, AccessLevel.MANAGER)));
        ManagerMenuOption.put("8", new Option("View Employee position distribution", () -> restrictedAction(this::viewPositionDistribution, AccessLevel.MANAGER)));
        ManagerMenuOption.put("9", new Option("View Tenure analysis", () -> restrictedAction(this::viewTenureAnalysis, AccessLevel.MANAGER)));
        ManagerMenuOption.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), (this::exit)));

        //Staff or employee functionality
        StaffMenuOptions.put("1", new Option("Item category Menu", () -> restrictedAction(this::viewStaff, AccessLevel.STAFF)));
        StaffMenuOptions.put("2", new Option("Item Menu", () -> restrictedAction(this::viewStaff, AccessLevel.STAFF)));
        StaffMenuOptions.put("3", new Option("Supplier Menu", () -> restrictedAction(this::viewStaff, AccessLevel.STAFF)));
        StaffMenuOptions.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), (this::exit)));

        exitFlag = false;
    }

    @Override
    public void start() {

        String staffMenuGreet = "Welcome to Inventory Menu " + loggedInStaff.getName() + "!\n=================================";

        while (!exitFlag) {
            clearScreen();
            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(staffMenuGreet).reset());
            displayOptions(MenuOptions);
            getUserOptions(MenuOptions);
        }
    }

    private void adminMenu(){
        subMenuExitAction = false;
        String adminMenuGreet = "Welcome to Admin Menu " + loggedInStaff.getName() + "!\n=================================";
        while (!subMenuExitAction) {
            clearScreen();
            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(adminMenuGreet).reset());
            displayOptions(AdminMenuOptions);
            getUserOptions(AdminMenuOptions);
        }
    }

    private void managerMenu(){
        subMenuExitAction = false;
        String managerMenuGreet = "Welcome to Manager and Supervisor Menu " + loggedInStaff.getName() + "!\n=================================";
        while (!subMenuExitAction) {
            clearScreen();
            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(managerMenuGreet).reset());
            displayOptions(ManagerMenuOption);
            getUserOptions(ManagerMenuOption);
        }
    }

    private void staffRelevantMenu(){
        subMenuExitAction = false;
        String staffMenuGreet = "Welcome to Manager and Supervisor Menu " + loggedInStaff.getName() + "!\n=================================";
            while (!subMenuExitAction) {
                clearScreen();
                System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(staffMenuGreet).reset());
                displayOptions(StaffMenuOptions);
                getUserOptions(StaffMenuOptions);
            }
    }

    private void signUpStaff() {

        Scanner scanner = new Scanner(System.in);

        try {
            clearScreen();

            Staff staff = new Staff();

            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a("Please enter ALL necessary info\n===============================\n").reset());



            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Name: "));
                String name = scanner.nextLine();
                try {
                    if (staff.setName(name, false)) {
                        break; // Exit loop if name is valid
                    }
                    break; // Exit loop if name is valid
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() +"\n").reset());
                }
            }


            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("\nEnter Gender (M/F): "));
                char gender = scanner.next().toUpperCase().charAt(0);
                try {
                    if (staff.setGender(ValidationCheck.checkGender(gender), false)) {
                        break; // Exit loop if gender is valid
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            int positionNumber;
            StaffPosition.printPosition();
            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter the number corresponding to the position: "));
                positionNumber = scanner.nextInt();
                try {
                    if (staff.setPosition(StaffPosition.fromNumber(positionNumber), false)) {
                        break; // Exit loop if position is valid
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("\nEnter Contact Number: "));
                String contact = scanner.next();
                try {
                    if (staff.setContactNumber(contact, false)) {
                        break;
                    }
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("\nEnter Email: "));
                String email = scanner.next();
                try {
                    if (staff.setEmail(email, false)) {
                        break;
                    }
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("\nEnter Address: "));
                String address = scanner.next();
                try{
                    if (staff.setAddress(address, false)) {
                        break;
                    }
                }catch(IllegalArgumentException e){
                    System.out.println(ansi().bgBrightRed().a(e.getMessage()).reset());
                    System.out.println("\n");
                }
            }

            scanner.nextLine();

            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("\nEnter Salary: "));

                try {
                    // Handle non-integer inputs
                    if (!scanner.hasNextInt()) {
                        System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a valid number.\n").reset());
                        scanner.next(); // Clear the invalid input
                        continue;
                    }

                    int salary = scanner.nextInt();

                    // Validation check
                    try {
                        if (staff.setSalary(salary, false)) {
                            break;
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a numeric value.\n").reset());
                    scanner.next(); // Clear the invalid input
                }
            }

            scanner.nextLine();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("\nEnter a password: "));
                String password = scanner.nextLine();
                try{
                    if (staff.setPassword(password, false, true)) {
                        break;
                    }
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            System.out.print(ansi().reset());

            staff.setDateJoined(LocalDate.now()); // Set the date joined to the current date

            while (true) {
                System.out.print(ansi().bgBrightGreen().a("To Confirm Adding (Enter 1) else (Enter 0): ").reset());
                int i = scanner.nextInt();

                if (i == 1) {
                    // Confirm and add to the database
                    boolean isAdded = staff.addToDb();

                    if (isAdded) {
                        System.out.println(ansi().bgBrightGreen().a("\nStaff added successfully!\n").reset());
                        pause();
                    } else {
                        System.out.println(ansi().bgBrightRed().a("\nFailed to add staff.\n").reset());
                    }
                    break; // Break out of the loop after adding the staff
                } else if (i == 0) {
                    System.out.println(ansi().bgBrightRed().a("\nAction terminated.\n").reset());
                    pause();
                    break; // Break out of the loop if action is terminated
                } else {
                    System.out.println(ansi().bgBrightRed().a("\nInvalid input. Please enter 1 to confirm or 0 to cancel.\n").reset());
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void deleteStaff() {

        Scanner scanner = new Scanner(System.in);

        try {
            clearScreen();

            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a("Delete a Staff Record\n=======================\n").reset());

            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Staff ID to delete: "));
                String staffId = scanner.nextLine();

                try {
                    // Retrieve staff object from the database
                    Staff staff = Staff.getFromDb(staffId);

                    if (staff == null) {
                        // If the staff is not found, print error and prompt again
                        System.out.println(ansi().bgBrightRed().a("Staff ID not found. Please try again.\n").reset());
                    } else {
                        // Confirm deletion
                        System.out.println(ansi().fgBrightYellow().a("Staff found: " + staff.getName() + "\n").reset());

                        while (true) {
                            System.out.print(ansi().bgBrightGreen().a("Confirm deletion (Enter 1 to delete, 0 to cancel): ").reset());
                            int confirmation = scanner.nextInt();

                            if (confirmation == 1) {
                                // Proceed with deletion
                                if (staff.deleteFromDb()) {
                                    System.out.println(ansi().bgBrightGreen().a("Staff record deleted successfully!").reset());
                                } else {
                                    System.out.println(ansi().bgBrightRed().a("Failed to delete staff record.").reset());
                                }
                                pause();
                                return;
                            } else if (confirmation == 0) {
                                System.out.println(ansi().bgBrightRed().a("Action terminated.").reset());
                                pause();
                                return;
                            } else {
                                // Invalid input for confirmation
                                System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter 1 to confirm or 0 to cancel.\n").reset());
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // Handle any unexpected validation issues
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("An unexpected error occurred: " + e.getMessage()).reset());
        }
    }

    private void modifyStaff() {

        Scanner scanner = new Scanner(System.in);

        try {
            clearScreen();

            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a("Modify staff document\n=====================").reset());

            System.out.print(ansi().fgRgb(8552916).a("\nEnter Staff ID to modify: "));
            String staffId = scanner.nextLine();
            Staff staff = Staff.getFromDb(staffId);

            if (staff == null) {
                System.out.println(ansi().bgBrightRed().a("Staff ID not found.").reset());
                return;
            }

            System.out.println();
            System.out.println("""
                                Which field would you like to modify?
                                =====================================
                                1.  Name
                                2.  Gender
                                3.  Contact Number
                                4.  Email
                                5.  Address
                                6.  Salary
                                7.  Password
                                8.  Position
                                =====================================
                                """);
            System.out.print("Choice (1-7): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character (prevent index out of bounds error)

            boolean updateResult;

            switch (choice) {
                case 1:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new name: "));
                        String newName = scanner.nextLine();

                        try {
                            updateResult = staff.setName(newName, true);
                            break; // Exit loop if name is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 2:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new gender (M/F): "));
                        char newGender = scanner.next().toUpperCase().charAt(0);
                        try {
                            updateResult = staff.setGender(ValidationCheck.checkGender(newGender), true);
                            break; // Exit loop if gender is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 3:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new password: "));
                        String newPassword = scanner.nextLine();
                        try {
                            updateResult = staff.setPassword(newPassword, true, false);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 4:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new contact number: "));
                        String newContact = scanner.nextLine();
                        try {
                            updateResult = staff.setContactNumber(newContact, true);
                            break; // Exit loop if contact number is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 5:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new email: "));
                        String newEmail = scanner.nextLine();
                        try {
                            updateResult = staff.setEmail(newEmail, true);
                            break; // Exit loop if email is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 6:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new address: "));
                        String newAddress = scanner.nextLine();
                        try {
                            updateResult = staff.setAddress(newAddress, true);
                            break; // Exit loop if address is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 7:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("\nEnter new salary: "));
                        try {
                            if (!scanner.hasNextDouble()) {
                                System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a valid number.\n").reset());
                                scanner.next(); // Clear the invalid input
                                continue;
                            }
                            double newSalary = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                            try {
                                updateResult = staff.setSalary(newSalary, true);
                                break; // Exit loop if salary is valid
                            } catch (IllegalArgumentException e) {
                                System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a numeric value.\n").reset());
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    break;
                case 8:
                    StaffPosition.printPosition();
                    int positionNumber;
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter the number corresponding to the new position: "));
                        positionNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        try {
                            StaffPosition newPosition = StaffPosition.fromNumber(positionNumber);
                            if (newPosition == null) {
                                throw new IllegalArgumentException("Invalid position number.");
                            }
                            updateResult = staff.setPosition(newPosition, true);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                default:
                    System.out.println(ansi().bgBrightRed().a("\n\nInvalid choice.").reset());
                    return;
            }
            if (updateResult) {
                System.out.println(ansi().bgBrightGreen().a("\n\nStaff details updated successfully!").reset());
                pause();
            } else {
                System.out.println(ansi().bgBrightRed().a("\n\nFailed to update staff details.").reset());
                pause();
            }

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("\n\nAn unexpected error occurred: " + e.getMessage()).reset());
            pause();
        }
    }

    public void searchStaff() {
        Scanner scanner = new Scanner(System.in);

        try {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("Staff Login\n=============").reset());

            System.out.print(ansi().fgRgb(8552916).a("Staff Id: "));
            String inputStaffId = scanner.nextLine();


            if (inputStaffId.trim().isEmpty()) {
                System.out.println(ansi().bgBrightRed().a("\nStaff ID cannot be empty.").reset());
                return;
            }

            Staff staff = Staff.getFromDb(inputStaffId);

            if (staff == null) {
                System.out.println(ansi().bgBrightRed().a("No staff record found with ID: " + inputStaffId).reset());
                return;
            } else{
                System.out.println(ansi().bgBrightGreen().a("Staff is found! ").reset());
            }

            // Print details of the found staff member
            System.out.println(ansi().fgRgb(8552916).a("\n=========================================="));
            System.out.println("Staff ID: " + staff.getId());
            System.out.println("Name: " + staff.getName());
            System.out.println("Gender: " + staff.getGender());
            System.out.println("Position: " + staff.getPosition());
            System.out.println("Contact Number: " + staff.getContactNumber());
            System.out.println("Email: " + staff.getEmail());
            System.out.println("Address: " + staff.getAddress());
            System.out.println("Salary: RM" + staff.getSalary());
            System.out.println("Date Joined: " + staff.getDateJoined());
            System.out.println("==========================================\n");
            System.out.print(ansi().reset());
            pause();

        }catch(Exception e) {
            System.out.println(ansi().bgBrightRed().a("An unexpected error occurred: " + e.getMessage()).reset());
        }
    }

    private void viewStaff() {
        try {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("View All Staff Records\n=======================\n").reset());

            // Retrieve all staff records from the database
            List<Staff> allStaff = Staff.getAllStaffInfo();

            int totalStaffCount = allStaff.size();
            int staffCount = 0;
            int managerCount = 0;
            int adminCount = 0;

            for (Staff staff : allStaff) {
                switch (staff.getPosition()) {
                    case INVENTORY_CLERK, STOCK_CONTROLLER, SUPPLIER -> staffCount++;
                    case WAREHOUSE_MANAGER, INVENTORY_SUPERVISOR -> managerCount++;
                    case ADMIN -> adminCount++;
                }
            }

            // Display counts
            System.out.println("==================================================");
            System.out.println(ansi().fgRgb(8552916).a("Total number of staff records: " + totalStaffCount).reset());
            System.out.println(ansi().fgRgb(8552916).a("Number of Staff: " + staffCount).reset());
            System.out.println(ansi().fgRgb(8552916).a("Number of Managers: " + managerCount).reset());
            System.out.println(ansi().fgRgb(8552916).a("Number of Admins: " + adminCount).reset());
            System.out.println("===================================================");

            if (totalStaffCount == 0) {
                System.out.println(ansi().bgBrightRed().a("No staff records found in the database.").reset());
                return;
            }

            // Print details of each staff member
            for (Staff staff : allStaff) {
                System.out.println(ansi().fgRgb(8552916).a("\n=========================================="));
                System.out.println("Staff ID: " + staff.getId());
                System.out.println("Name: " + staff.getName());
                System.out.println("Gender: " + staff.getGender());
                System.out.println("Position: " + staff.getPosition());
                System.out.println("Contact Number: " + staff.getContactNumber());
                System.out.println("Email: " + staff.getEmail());
                System.out.println("Address: " + staff.getAddress());
                System.out.println("Salary: RM" + staff.getSalary());
                System.out.println("Date Joined: " + staff.getDateJoined());
                System.out.println("===========================================\n");
            }

            pause();

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("An error occurred while retrieving staff records: " + e.getMessage()).reset());
        }
    }

    private void viewSalaryDistribution() {
        try {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("Salary Distribution\n====================\n").reset());

            List<Staff> allStaff = Staff.getAllStaffInfo();

            if (allStaff.isEmpty()) {
                System.out.println(ansi().bgBrightRed().a("No staff records found.").reset());
                return;
            }

            double totalSalary = 0;
            double minSalary = Double.MAX_VALUE;
            double maxSalary = Double.MIN_VALUE;

            for (Staff staff : allStaff) {
                double salary = staff.getSalary();
                totalSalary += salary;
                if (salary < minSalary) minSalary = salary;
                if (salary > maxSalary) maxSalary = salary;
            }

            double averageSalary = totalSalary / allStaff.size();

            // Display salary distribution
            System.out.println(ansi().fgRgb(8552916).a("Average Salary: RM" + String.format("%.2f", averageSalary)).reset());
            System.out.println(ansi().fgRgb(8552916).a("Minimum Salary: RM" + String.format("%.2f", minSalary)).reset());
            System.out.println(ansi().fgRgb(8552916).a("Maximum Salary: RM" + String.format("%.2f", maxSalary)).reset());

            // Define salary ranges
            int[] ranges = {0, 5000, 10000, 15000, 20000, 50000};
            int[] rangeCounts = new int[ranges.length - 1];

            for (Staff staff : allStaff) {
                double salary = staff.getSalary();
                for (int i = 0; i < ranges.length - 1; i++) {
                    if (salary >= ranges[i] && salary < ranges[i + 1]) {
                        rangeCounts[i]++;
                        break;
                    }
                }
            }

            System.out.println("\nSalary Ranges:");
            System.out.println(ansi().fgRgb(8552916).a("======================================="));
            for (int i = 0; i < ranges.length - 1; i++) {
                System.out.println("RM" + ranges[i] + " - RM" + (ranges[i + 1] - 1) + ": " + rangeCounts[i]);
            }
            System.out.println("=======================================\n");
            System.out.println(ansi().reset());
            pause();

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("Error retrieving salary distribution: " + e.getMessage()).reset());
        }
    }

    private void viewGenderDistribution() {
        try {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("Gender Distribution\n====================").reset());

            List<Staff> allStaff = Staff.getAllStaffInfo();

            if (allStaff.isEmpty()) {
                System.out.println(ansi().bgBrightRed().a("No staff records found.").reset());
                return;
            }

            int maleCount = 0;
            int femaleCount = 0;

            for (Staff staff : allStaff) {
                if ("Male".equalsIgnoreCase(staff.getGender())) {
                    maleCount++;
                } else if ("Female".equalsIgnoreCase(staff.getGender())) {
                    femaleCount++;
                }
            }

            int totalStaffCount = allStaff.size();
            double malePercentage = ((double) maleCount / totalStaffCount) * 100;
            double femalePercentage = ((double) femaleCount / totalStaffCount) * 100;

            System.out.println(ansi().fgRgb(8552916).a("\n===================="));
            System.out.println("Total number of employee: " + totalStaffCount);
            System.out.println("Male: " + maleCount + " (" + String.format("%.2f", malePercentage) + "%)");
            System.out.println("Female: " + femaleCount + " (" + String.format("%.2f", femalePercentage) + "%)");
            System.out.println("====================\n");
            System.out.println(ansi().reset());
            pause();

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("Error retrieving gender distribution: " + e.getMessage()).reset());
        }
    }

    private void viewPositionDistribution() {
        try {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("Position Distribution\n=====================\n").reset());

            List<Staff> allStaff = Staff.getAllStaffInfo();

            if (allStaff.isEmpty()) {
                System.out.println(ansi().bgBrightRed().a("No staff records found.").reset());
                return;
            }

            Map<StaffPosition, Integer> positionCounts = new HashMap<>();

            for (Staff staff : allStaff) {
                StaffPosition position = staff.getPosition();
                positionCounts.put(position, positionCounts.getOrDefault(position, 0) + 1);
            }

            System.out.println("\n=====================");
            System.out.println(ansi().fgRgb(8552916).a("Position Distribution:").reset());
            for (Map.Entry<StaffPosition, Integer> entry : positionCounts.entrySet()) {
                System.out.println(ansi().fgRgb(8552916).a(entry.getKey() + ": " + entry.getValue()).reset());
            }
            System.out.println("=====================\n");

            pause();

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("Error retrieving position distribution: " + e.getMessage()).reset());
        }
    }

    private void viewTenureAnalysis() {
        try {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("Tenure Analysis\n================\n").reset());

            List<Staff> allStaff = Staff.getAllStaffInfo();

            if (allStaff.isEmpty()) {
                System.out.println(ansi().bgBrightRed().a("No staff records found.").reset());
                return;
            }

            Map<String, Integer> tenureCounts = new HashMap<>();
            LocalDate now = LocalDate.now();

            for (Staff staff : allStaff) {
                LocalDate dateJoined = staff.getDateJoined();
                if (dateJoined != null) {
                    int yearsOfService = now.getYear() - dateJoined.getYear();
                    if (now.getMonthValue() < dateJoined.getMonthValue() || (now.getMonthValue() == dateJoined.getMonthValue() && now.getDayOfMonth() < dateJoined.getDayOfMonth())) {
                        yearsOfService--;
                    }
                    String range = switch (yearsOfService) {
                        case 0 -> "Less than 1 year";
                        case 1 -> "1 year";
                        case 2 -> "2 years";
                        case 3 -> "3 years";
                        case 4 -> "4 years";
                        default -> "5+ years";
                    };
                    tenureCounts.put(range, tenureCounts.getOrDefault(range, 0) + 1);
                }
            }

            System.out.println("\n=====================");
            System.out.println(ansi().fgRgb(8552916).a("Tenure Distribution:").reset());
            for (Map.Entry<String, Integer> entry : tenureCounts.entrySet()) {
                System.out.println(ansi().fgRgb(8552916).a(entry.getKey() + ": " + entry.getValue()).reset());
            }
            System.out.println("=====================\n");

            pause();

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("Error retrieving tenure analysis: " + e.getMessage()).reset());
        }
    }

    private void quitMenu() {
        System.out.println("\nSee you again " + loggedInStaff.getName() +"! >:)");
        pause();
        exitFlag = true;
    }

    private void exit() {
        subMenuExitAction = true;
    }

    private void restrictedAction(Runnable action, AccessLevel requiredAccessLevel) {
        if (loggedInStaff != null) {
            // Check if the logged-in staff's access level is equal to or higher than the required access level
            if (loggedInStaff.getPosition().getAccessLevel().ordinal() >= requiredAccessLevel.ordinal()) {
                action.run();
            } else {
                System.out.println("Access Denied: Insufficient privileges.");
                pause();
            }
        } else {
            System.out.println("No staff is currently logged in. Please log in first.");
            pause();
        }
    }
}
