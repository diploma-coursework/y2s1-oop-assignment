package org.assignment.util.menu;

import org.assignment.util.DbMethods;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;
import org.assignment.util.validationCheck;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Field;
import java.util.*;

import static com.google.api.client.util.FieldInfo.setFieldValue;
import static org.fusesource.jansi.Ansi.ansi;

public class ItemMenu extends BaseMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Option> menuOptions = new HashMap<>();

    public ItemMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Add Item", this::addItem));
        menuOptions.put("2", new Option("Remove Item", this::removeItem));
        menuOptions.put("3", new Option("Modify Item", this::modifyItem));
        menuOptions.put("4", new Option("View Items", this::viewItems));
        menuOptions.put("0", new Option("Quit", this::quit));

        exitFlag = false;
        actionExit = false;
    }

    @Override
    public void start() {
        while (!exitFlag) {
            clearScreen();
            displayOptions(menuOptions);
            getUserOptions(menuOptions);
        }
    }

    private void addItem() {
        boolean repeatAction = false;
        timedPause(300);
        clearScreen();
        Item item = new Item();
        // TODO: request for fill up of constructor parameters
        // TODO: Validation checks
        // TODO: print Placeholder value
        System.out.println("Currently Adding an Item (use Exit or -1 to return)");
        StringBuilder scannerValue = new StringBuilder();

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Name           :");
            if (scanner.hasNextLine()) {
                scannerValue.append(scanner.nextLine().trim());
                if (!validationCheck.isValidName(scannerValue.toString())){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(scannerValue.toString()).isEmpty()){
                    return;
                }
                item.setName(scannerValue.toString());
                scannerValue.setLength(0);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Unit Price     :");
            // FIXME: NaN would be valid
            if (scanner.hasNextDouble()) {
                scannerValue.append(scanner.nextDouble());
                scanner.nextLine();
                if (acceptExitAction(Double.valueOf(scannerValue.toString())).isEmpty()){
                    return;
                }
                item.setUnitPrice(Double.parseDouble(scannerValue.toString()));
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Quantity       :");
            if (scanner.hasNextInt()) {
                scannerValue.append(scanner.nextInt());
                scanner.nextLine();
                if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()){
                    return;
                }
                item.setQuantity(Integer.parseInt(scannerValue.toString()));
                scannerValue.setLength(0);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Supplier ID         :");
            if (scanner.hasNext()) {
                scannerValue.append(scanner.next());
                if (acceptExitAction(scannerValue.toString()).isEmpty()){
                    return;
                }
                if (!DbMethods.isValidCode("SUP",scannerValue.toString())){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                item.setSupplier(Supplier.getFromDb(scannerValue.toString()));
                scannerValue.setLength(0);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("ItemCategory ID     :");
            if (scanner.hasNext()) {
                scannerValue.append(scanner.next());
                if (acceptExitAction(scannerValue.toString()).isEmpty()){
                    return;
                }
                if (!DbMethods.isValidCode("CAT",scannerValue.toString())){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                item.setItemCategory(ItemCategory.getFromDb(scannerValue.toString()));
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        // Confirmation Section:
        clearScreen();
        System.out.println(item);
        System.out.print("To Confirm Adding [Enter 1] else [Enter 0]: ");
        while (true) {
            if (scanner.hasNextInt()) {
                int i = scanner.nextInt();
                if (i != 1 && i != 0) {
                    System.out.println("Invalid input. Please enter 1 to confirm, or 0 to cancel.");
                    System.out.print("> ");
                    continue; // No need to clear the screen again
                }
                if (i == 0) {
                    System.out.println("Action Terminated..");
                    pause();
                    clearScreen();
                    break;
                }
                item.addToDb();
                pause();
                clearScreen();
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private void removeItem() {
        clearScreen();
        // TODO: choose by id
        List<Item> removeList = new ArrayList<>();

        System.out.println("Enter the IDs' of the Items to Remove: ");
        System.out.println("Eg.(ITEM0001 ITEM0002...)");
        System.out.println("Enter Exit to exit");

        System.out.print("> ");
        while (scanner.hasNextLine()) {
            boolean containCorrectSequence = false;
            String input = scanner.nextLine();
            if (input.isEmpty()){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
                continue;
            }
            for (String word : input.split("\\s+")){
                if ("exit".equalsIgnoreCase(word)) {
                    break;
                }
                if (DbMethods.isValidCode("ITEM",word) && Item.getFromDb(word)!= null){
                    containCorrectSequence =true;
                    if (!removeList.contains(Item.getFromDb(word))){
                        removeList.add(Item.getFromDb(word));
                    }
                }
            }
            if (!containCorrectSequence){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
            }
            if (containCorrectSequence){
                System.out.print("> ");
            }
        }

        int option = -1;
         do {
             // accept IDs'
             clearScreen();
             System.out.println("These are the Items that will be Deleting");

             if (removeList.isEmpty()){
                 return;
             }

             for (int i =1;i <= removeList.size();i++){
                 System.out.println(i + ") " + removeList.get(i-1).getId());
             }

             System.out.println("Enter the Indexes to remove from List, Enter 0 to Confirm, Enter -1 to Exit");
             System.out.print("> ");
             // ----
             String input = scanner.nextLine().trim();

             // Check if input is empty or contains only whitespace
             if (input.isEmpty()) {
                 // Clear the current line and reprint the prompt
                 System.out.print(ansi().cursorUp(1).eraseLine());
                 System.out.print("> ");
                 continue;
             }

             // Check if input is a valid integer using a regular expression
             if (input.matches("-?\\d+")) {
                 option = Integer.parseInt(input);

                 if (option > 0 && option <= removeList.size()) {
                     removeList.remove(option - 1);
                 } else if (option == 0) {
                     // Confirm and delete all remaining items in the list
                     for (Item item : removeList) {
                         item.deleteFromDb();
                     }
                     // Clear the list if you want to remove all items
                     removeList.clear();
                 } else if (option == -1) {
                     break;
                 } else {
                     System.out.print(ansi().cursorUp(1).eraseLine());
                     System.out.print("> ");
                 }
             } else {
                 // Handle invalid input (not a number)
                 System.out.print(ansi().cursorUp(1).eraseLine());
                 System.out.print("> ");
             }
        } while(option != 0);
    }

    private void modifyItem() {
        // get instance of Item from db
        // change its value
        // update with updateToDb(String fieldName)
        clearScreen();
        // TODO: choose by id
        System.out.println("Enter the ID of the ITEM to modify: ");
        System.out.println("Enter Exit to exit");
        System.out.print("> ");
        StringBuilder scannerValue = new StringBuilder();
        // Method Segments for Getting Item instance through ITEMID and prompting with Validation for ID
        Item item = new Item();
        while (true) {
            // FIXME: Any clear,Line Reprint, Exit doesnt work
            scannerValue.append(scanner.nextLine());
            if (scannerValue.isEmpty()){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
                continue;
            }
            if (acceptExitAction(scannerValue.toString()).isEmpty()){
                return;
            }
            if (!DbMethods.isValidCode("ITEM",scannerValue.toString()) || Item.getFromDb(scannerValue.toString())== null){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
                continue;
            } else{
                item = Item.getFromDb(scannerValue.toString());
                scannerValue.setLength(0);
                break;
            }
        }
//        item
        boolean validFieldName = false;
        String chosenField = "";
        Field[] fields = Item.class.getDeclaredFields();
        System.out.println("Which Field would you Like to Modify");
        System.out.println(item);
        System.out.println("Enter Exit to exit");
        System.out.print("> ");
        // ---
        while (true) {
            scannerValue.append(scanner.next());
            if (scannerValue.isEmpty()){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
                continue;
            }
            if (acceptExitAction(scannerValue.toString()).isEmpty()){
                return;
            }

            for (Field field : fields) {
                if (field.toString().contentEquals(scannerValue)){
                    chosenField = scannerValue.toString();
                    // TODO: find getters for this field
                    switch (field.toString()){
                        case "name":
                            // CHOOSE STRING
                            while (true) {
                                scannerValue.setLength(0);
                                System.out.print("Item Name           :");
                                if (scanner.hasNextLine()) {
                                    scannerValue.append(scanner.nextLine().trim());
                                    if (!validationCheck.isValidName(scannerValue.toString())){
                                        System.out.print(ansi().cursorUp(1).eraseLine());
                                        continue;
                                    }

                                    if (acceptExitAction(scannerValue.toString()).isEmpty()){
                                        return;
                                    }
                                    assert item != null;
                                    item.setName(scannerValue.toString());
                                    scannerValue.setLength(0);
                                    break;
                                } else{
                                    System.out.print(ansi().cursorUp(1).eraseLine());
                                    scanner.nextLine();
                                }
                            }
                            break;
                        case "unitPrice":
                            // CHOOSE DOUBLE
                            while (true) {
                                scannerValue.setLength(0);
                                System.out.print("Item Unit Price     :");
                                // FIXME: NaN would be valid
                                if (scanner.hasNextDouble()) {
                                    scannerValue.append(scanner.nextDouble());
                                    scanner.nextLine();
                                    if (acceptExitAction(Double.valueOf(scannerValue.toString())).isEmpty()){
                                        return;
                                    }
                                    assert item != null;
                                    item.setUnitPrice(Double.parseDouble(scannerValue.toString()));
                                    break;
                                } else{
                                    System.out.print(ansi().cursorUp(1).eraseLine());
                                    scanner.nextLine();
                                }
                            }
                            break;
                        case "quantity":
                            // CHOOSE INT
                            while (true) {
                                scannerValue.setLength(0);
                                System.out.print("Item Quantity       :");
                                if (scanner.hasNextInt()) {
                                    scannerValue.append(scanner.nextInt());
                                    scanner.nextLine();
                                    if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()){
                                        return;
                                    }
                                    assert item != null;
                                    item.setQuantity(Integer.parseInt(scannerValue.toString()));
                                    scannerValue.setLength(0);
                                    break;
                                } else{
                                    System.out.print(ansi().cursorUp(1).eraseLine());
                                    scanner.nextLine();
                                }
                            }
                            break;
                        case "Supplier":
                            // CHOOSE SUP ID
                            while (true) {
                                scannerValue.setLength(0);
                                System.out.print("Supplier ID         :");
                                if (scanner.hasNext()) {
                                    scannerValue.append(scanner.next());
                                    if (acceptExitAction(scannerValue.toString()).isEmpty()){
                                        return;
                                    }
                                    if (!DbMethods.isValidCode("SUP",scannerValue.toString())){
                                        System.out.print(ansi().cursorUp(1).eraseLine());
                                        continue;
                                    }
                                    assert item != null;
                                    item.setSupplier(Supplier.getFromDb(scannerValue.toString()));
                                    scannerValue.setLength(0);
                                    break;
                                } else{
                                    System.out.print(ansi().cursorUp(1).eraseLine());
                                    scanner.nextLine();
                                }
                            }
                            // TODO: get instance from id
                            break;
                        case "ItemCategory":
                            // CHOSE CAT ID
                            while (true) {
                                scannerValue.setLength(0);
                                System.out.print("ItemCategory ID     :");
                                if (scanner.hasNext()) {
                                    scannerValue.append(scanner.next());
                                    if (acceptExitAction(scannerValue.toString()).isEmpty()){
                                        return;
                                    }
                                    if (!DbMethods.isValidCode("CAT",scannerValue.toString())){
                                        System.out.print(ansi().cursorUp(1).eraseLine());
                                        continue;
                                    }
                                    assert item != null;
                                    item.setItemCategory(ItemCategory.getFromDb(scannerValue.toString()));
                                    break;
                                } else{
                                    System.out.print(ansi().cursorUp(1).eraseLine());
                                    scanner.nextLine();
                                }
                            }
                            // TODO: get instance from id
                            break;
                        default:
                            break;
                    }
                    validFieldName = true;
                    break;
                }
            }

            if (!validFieldName){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
            }
            break;
        }



        clearScreen();
        assert item != null;
        System.out.println(item.RefIdPrint());
        System.out.print("To Confirm Modifying [Enter 1] else [Enter 0]: ");
        while (true) {
            if (scanner.hasNextInt()) {
                int i = scanner.nextInt();
                if (i != 1 && i != 0) {
                    System.out.println("Invalid input. Please enter 1 to confirm, or 0 to cancel.");
                    continue;
                }
                if (i == 0) {
                    System.out.println("Action Terminated..");
                    pause();
                    clearScreen();
                    break;
                }

                assert item != null;
                item.updateToDb(chosenField);
                pause();
                clearScreen();
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
        // ---

    }

    private void viewItems() {
        System.out.println("Viewing all items...");
        // Logic to view items
    }

    private void quit() {
        System.out.println("Exiting the menu...");
        System.exit(0);
    }

//    public static boolean confirmSave(Item item){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter 1 to confirm...");
//        if(scanner.nextInt()==1){
//            return true;
//        }
//        return false;
//    }

}