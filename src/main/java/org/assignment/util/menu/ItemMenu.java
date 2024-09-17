package org.assignment.util.menu;

import org.assignment.util.DbMethods;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;
import org.assignment.util.validationCheck;
import org.checkerframework.checker.units.qual.A;
import org.fusesource.jansi.Ansi;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.api.client.util.FieldInfo.setFieldValue;
import static org.fusesource.jansi.Ansi.ansi;

public class ItemMenu extends BaseMenu {
    private List<Item> items = Item.getAllItem(); // FIXME: testing
    private static final Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Option> menuOptions = new HashMap<>()
            , modifyOptions = new HashMap<>(), queryOptions = new HashMap<>()
            , queryOptionsUnitPrice = new HashMap<>(), queryOptionsQuantity = new HashMap<>();

    public ItemMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Add Item", this::addItem));
        menuOptions.put("2", new Option("Remove Item", this::removeItem));
        menuOptions.put("3", new Option("Modify Item", this::modifyItem));
        menuOptions.put("4", new Option("View All Items", this::viewItems));
        menuOptions.put("5", new Option("Filter Items", this::menuFilter));
        menuOptions.put("0", new Option(ansi().fgRed().a("Return to staff menu...").reset().toString(), this::quit));

        modifyOptions.put("1", new Option("Edit Name", () -> {}));
        modifyOptions.put("2", new Option("Edit Unit Price", () -> {}));
        modifyOptions.put("3", new Option("Edit Quantity", () -> {}));
        modifyOptions.put("4", new Option("Edit Supplier ID", () -> {}));
        modifyOptions.put("5", new Option("Edit ItemCategory ID", () -> {}));
        modifyOptions.put("0",new Option(ansi().fgBrightGreen().a("Save").reset().toString(), () -> {}));

        queryOptions.put("1", new Option("Filter By Name", this::filterByName));
        queryOptions.put("2", new Option("Filter By Unit Price", this::menuUnitPrice));
        queryOptions.put("3", new Option("Filter By Quantity", this::menuQuantity));
        queryOptions.put("4", new Option("Search By Supplier ID", this::filterBySupplierID));
        queryOptions.put("5", new Option("Search By ItemCategory ID", this::filterByItemCategoryID));
        queryOptions.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), this::quitSearch));

//        queryOptionsUnitPrice.put("1", new Option("Filter Unit Price By Value", () -> {filterByUnitPrice(() -> {System.out.println("Hello");});}));
//        queryOptionsUnitPrice.put("2", new Option("Filter Unit Price By Range", () -> {filterByUnitPrice(() -> {System.out.println("Hello");});}));
        queryOptionsUnitPrice.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), () -> {}));

        queryOptionsQuantity.put("1", new Option("Filter Quantity by Value", () -> {}));
        queryOptionsQuantity.put("2", new Option("Filter Quantity by Range", () -> {}));
        queryOptionsQuantity.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), () -> {}));

        exitFlag = false;
    }

    @Override
    public void start() {
        while (!exitFlag) {
            clearScreen();
            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(" Item Menu\n====================").reset());
            displayOptions(menuOptions);
            getUserOptions(menuOptions);
        }
    }

    private void menuUnitPrice(){
        clearScreen();
        displayOptions(queryOptionsUnitPrice);
        getUserOptions(queryOptionsUnitPrice);
    }

    private void menuQuantity(){
        clearScreen();
        displayOptions(queryOptionsQuantity);
        getUserOptions(queryOptionsQuantity);
    }

    private void menuFilter(){
        exitAction = false;
        clearScreen();
        while (!exitAction){
            displayOptions(queryOptions);
            getUserOptions(queryOptions);
        }
    }

    private void addItem() {
        clearScreen();
        Item item = new Item();
        System.out.println("Currently Adding an Item (use Exit or -1 to return)");
        StringBuilder scannerValue = new StringBuilder();

        // ITEM NAME
        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Name           :");
            if (scanner.hasNextLine()) {
                scannerValue.append(scanner.nextLine().trim());
                if (!validationCheck.isValidName(scannerValue.toString())){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(scannerValue.toString()).isEmpty())return;
                item.setName(scannerValue.toString());
                scannerValue.setLength(0);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        // ITEM UNIT PRICE
        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Unit Price     :");
            // FIXME: NaN would be valid
            if (scanner.hasNextLine()){
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.toString().isEmpty() || !scannerValue.toString().matches("^[+-]?([0-9]*[.])?[0-9]+([eE][+-]?[0-9]+)?$")) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(Double.valueOf(scannerValue.toString())).isEmpty()) {return;}

                if (Double.parseDouble(scannerValue.toString()) <0){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                item.setUnitPrice(Double.parseDouble(scannerValue.toString()));
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        // ITEM QUANTITY
        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Quantity       :");
            if (scanner.hasNextLine()){
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.isEmpty() || !scannerValue.toString().matches("\\d+")) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()) {return;}

                if (Integer.parseInt(scannerValue.toString()) <0){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                item.setQuantity(Integer.parseInt(scannerValue.toString()));
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        // ITEM SUPPLIER
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

        // ITEM CATEGORY
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
        while (true) {
            scannerValue.setLength(0);
            clearScreen();
            System.out.println(item);
            System.out.print("To Confirm Adding [Enter 1] else [Enter 0]: ");
            if (scanner.hasNextLine()){
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.isEmpty() || (Integer.parseInt(scannerValue.toString()) != 1 && (Integer.parseInt(scannerValue.toString()) != 0))) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                if (Integer.parseInt(scannerValue.toString()) == 1) item.addToDb(); else System.out.println("Action Terminated..");

                pause();
                clearScreen();
                break;

            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }
    }

    private void removeItem() {
        clearScreen();
        List<Item> removeList = new ArrayList<>();

        System.out.println("Enter the IDs' of the Items to Remove: ");
        System.out.println("Eg.(ITEM0001 ITEM0002...)");
        System.out.println("Enter Exit to exit");

        System.out.print("> ");
        while (scanner.hasNextLine()) {
            boolean containCorrectSequence = false;
            String input = scanner.nextLine().trim();
            if (input.isEmpty()){
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
                continue;
            }

            boolean exitForLoop = false;
            for (String word : input.split("\\s+")){
                if ("exit".equalsIgnoreCase(word)) {
                    exitForLoop = true;
                    break;
                }
                if (DbMethods.isValidCode("ITEM",word) && Item.getFromDb(word)!= null){
                    containCorrectSequence =true;
                    if (!removeList.contains(Item.getFromDb(word))){
                        removeList.add(Item.getFromDb(word));
                    }
                }
            }

            if (exitForLoop) break;

            if (!containCorrectSequence) System.out.print(ansi().cursorUp(1).eraseLine());
            System.out.print("> ");
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

             System.out.println("Enter the Indexes to remove from List, Enter 0 to Confirm, Enter -1 to Exit"); // TODO: print below cursor
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
             if (acceptExitAction(input).isEmpty())return;

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
        clearScreen();
        System.out.println("Enter the ID of the ITEM to modify: ");
        System.out.println("Enter Exit to exit");
        System.out.print("> ");
        StringBuilder scannerValue = new StringBuilder();

        Item item;
        // VALID ITEM ID
        while (true) {
            scannerValue.setLength(0);
            if (scanner.hasNextLine()) {
                scannerValue.append(scanner.nextLine().trim());
                if (acceptExitAction(scannerValue.toString()).isEmpty()) {return;}

                if (scannerValue.isEmpty()){System.out.print(ansi().cursorUp(1).eraseLine());}

                item = Item.getFromDb(scannerValue.toString());
                if (DbMethods.isValidCode("ITEM", scannerValue.toString()) && Item.getFromDb(scannerValue.toString()) != null) {break;}

                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                System.out.print("> ");
            }
        }

        clearScreen();
        System.out.println("\n"+item);
        // ---
        while (true){
            displayOptions(modifyOptions);
            Option selectedOption = getUserOptions(modifyOptions);

            // "0" for exit
            if (selectedOption == modifyOptions.get("0")) return;

            // "1" for edit name
            if (selectedOption == modifyOptions.get("1")) { // TODO: When invalidName, it doesnt go back to starting position
                String newName = "";
                System.out.print("Enter new Name: ");

                if (newName.compareToIgnoreCase("exit") == 0) {return;}

                while (newName.isEmpty() || !validationCheck.isValidName(newName) || newName.equals(item.getName())) {
                    newName = scanner.nextLine().trim();
                    if (newName.isEmpty()) System.out.print(ansi().cursorUp(1).eraseLine());
                }

                System.out.println("\nAre you sure about the change? (y / n)");
                if (getYesNoOption()) item.setName(newName, true);
            } else if (selectedOption == modifyOptions.get("2")) { //

                while (true) {
                    scannerValue.setLength(0);
                    System.out.print("Enter new Unit Price: ");
                    // FIXME: NaN would be valid
                    if (scanner.hasNextLine()){
                        scannerValue.append(scanner.nextLine().trim());

                        if (scannerValue.toString().isEmpty() || !scannerValue.toString().matches("^[+-]?([0-9]*[.])?[0-9]+([eE][+-]?[0-9]+)?$")) {
                            System.out.print(ansi().cursorUp(1).eraseLine());
                            continue;
                        }

                        if (acceptExitAction(Double.valueOf(scannerValue.toString())).isEmpty()) {return;}

                        if ((Double.parseDouble(scannerValue.toString()) <0) || (Double.parseDouble(scannerValue.toString()) == item.getUnitPrice())){
                            System.out.print(ansi().cursorUp(1).eraseLine());
                            continue;
                        }
                        item.setUnitPrice(Double.parseDouble(scannerValue.toString()));
                        break;
                    } else{
                        System.out.print(ansi().cursorUp(1).eraseLine());
                        scanner.nextLine();
                    }
                }

                System.out.println("\nAre you sure about the change? (y / n)");
                if (getYesNoOption()) item.setUnitPrice(Double.parseDouble(scannerValue.toString()), true);
            } else if (selectedOption == modifyOptions.get("3")) {

                while (true) {
                    scannerValue.setLength(0);
                    System.out.print("Enter new Quantity: ");
                    if (scanner.hasNextLine()){
                        scannerValue.append(scanner.nextLine().trim());

                        if (scannerValue.isEmpty() || !scannerValue.toString().matches("\\d+")) {
                            System.out.print(ansi().cursorUp(1).eraseLine());
                            continue;
                        }

                        if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()) {return;}

                        if (Integer.parseInt(scannerValue.toString()) <0){
                            System.out.print(ansi().cursorUp(1).eraseLine());
                            continue;
                        }
                        item.setQuantity(Integer.parseInt(scannerValue.toString()));
                        break;
                    } else{
                        System.out.print(ansi().cursorUp(1).eraseLine());
                        scanner.nextLine();
                    }
                }

                System.out.println("\nAre you sure about the change? (y / n)");
                if (getYesNoOption()) item.setQuantity(Integer.parseInt(scannerValue.toString()), true);
            } else if (selectedOption == modifyOptions.get("4")){
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

                System.out.println("\nAre you sure about the change? (y / n)");
                if (getYesNoOption()) item.setSupplier(Supplier.getFromDb(scannerValue.toString()), true);
            } else {
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

                System.out.println("\nAre you sure about the change? (y / n)");
                if (getYesNoOption()) item.setItemCategory(ItemCategory.getFromDb(scannerValue.toString()), true);
            }

            clearScreen();
            System.out.println(ansi().fgBrightGreen().a(" Update an Item :\n=========================\n").reset());
            System.out.println(item);
        }
    }

    private void viewItems() {
        clearScreen();
        List<Item> itemList = Item.getAllItem();
        if (itemList == null) System.out.println("No item category is present.");
        else {
            for (Item item : itemList) {
                System.out.println(item);
            }
        }
        pause();
    }

    // FIXME TESTING
    // Filter by Name // FIXME: idk wat or not
    private void filterByName() {
        System.out.print("Enter item name: ");
        String query = scanner.nextLine().trim();

        List<Item> filteredItems = items.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

        displayFilteredItems(filteredItems);
    }

    // Filter by Unit Price TODO: modify
    // TODO: idea -> can i have 2 printstatement param, if only needed 1, printstatement is empty
    // TODO: ifLogic is where the if(code) will be in , 1 Runnable for the filteredItems .filter
    // FIXME: starting to think if its worth the effort.. or should just use overloading methods
    private void filterByUnitPrice(Runnable printStatement ,Runnable ifLogic) {
        StringBuilder price = new StringBuilder();
        while (true) {
            price.setLength(0);
            System.out.print("Enter Unit Price     :");
            printStatement.run();
            // FIXME: NaN would be valid
            if (scanner.hasNextLine()){
                price.append(scanner.nextLine().trim());

                if (price.toString().isEmpty() || !price.toString().matches("^[+-]?([0-9]*[.])?[0-9]+([eE][+-]?[0-9]+)?$")) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(Double.valueOf(price.toString())).isEmpty()) {return;}

                if (Double.parseDouble(price.toString()) <0){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                // TODO: this is the line to put as Runnable
                List<Item> filteredItems = items.stream().filter(item -> item.getUnitPrice() == Double.parseDouble(price.toString())).collect(Collectors.toList());
                displayFilteredItems(filteredItems);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }
    }

    private void filterBySingleQuantity() {
        System.out.print("Enter quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            List<Item> filteredItems = items.stream()
                    .filter(item -> item.getQuantity() == quantity)
                    .collect(Collectors.toList());
            displayFilteredItems(filteredItems);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for quantity.");
        }
    }

    // Filter by Quantity Range
    private void filterByQuantityRange() {
        System.out.print("Enter starting range value: ");
        try {
            int start = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter ending range value: ");
            int end = Integer.parseInt(scanner.nextLine());
            List<Item> filteredItems = items.stream()
                    .filter(item -> item.getQuantity() >= start && item.getQuantity() <= end)
                    .collect(Collectors.toList());
            displayFilteredItems(filteredItems);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for range.");
        }
    }


    // Filter by Quantity TODO: modify
    private void filterByQuantity() {
        System.out.print("Enter quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            List<Item> filteredItems = items.stream()
                    .filter(item -> item.getQuantity() == quantity)
                    .collect(Collectors.toList());
            displayFilteredItems(filteredItems);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for quantity.");
        }
    }

    // Filter by Supplier ID
    private void filterBySupplierID() {
        System.out.print("Enter supplier ID: ");
        String query = scanner.nextLine();
        List<Item> filteredItems = items.stream()
                .filter(item -> item.getSupplier().getId().equals(query))
                .collect(Collectors.toList());
        displayFilteredItems(filteredItems);
    }

    // Filter by ItemCategory ID
    private void filterByItemCategoryID() {
        System.out.print("Enter item category ID: ");
        String query = scanner.nextLine();
        List<Item> filteredItems = items.stream()
                .filter(item -> item.getItemCategory().getId().equals(query))
                .collect(Collectors.toList());
        displayFilteredItems(filteredItems);
    }

    // Display the filtered items
    private void displayFilteredItems(List<Item> filteredItems) {filteredItems.forEach(item -> System.out.println(item.toString()));}

    private void quit() {exitFlag = true;}

    private void quitSearch() {exitAction = true;}
}