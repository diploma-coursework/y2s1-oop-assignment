package org.assignment.util.menu;

import org.assignment.util.DbMethods;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;
import org.assignment.util.validationCheck;
import java.util.*;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class ItemMenu extends BaseMenu {
    private static final List<Item> items = Item.getAllItem(); // FIXME: testing
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

        queryOptionsUnitPrice.put("1", new Option("Filter Unit Price By Value",this::filterByUnitPriceValue));
        queryOptionsUnitPrice.put("2", new Option("Filter Unit Price By Range",this::filterByUnitPriceRange));
        queryOptionsUnitPrice.put("0",new Option(ansi().fgRed().a("Exit").reset().toString(), () -> {}));

        queryOptionsQuantity.put("1", new Option("Filter Quantity by Value",this::filterByQuantityValue));
        queryOptionsQuantity.put("2", new Option("Filter Quantity by Range", this::filterByQuantityRange));
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

    // Menu
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
        while (!exitAction){
            clearScreen();
            displayOptions(queryOptions);
            getUserOptions(queryOptions);
            pause();
        }
    }

    // CRUD
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
                if (scannerValue.isEmpty() || !scannerValue.toString().matches("-1|\\d+")) {
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
                if (acceptExitAction(scannerValue.toString()).isEmpty()){return;}
                if (!DbMethods.isValidCode("SUP",scannerValue.toString()) || Supplier.getFromDb(scannerValue.toString()) == null){
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
                if (acceptExitAction(scannerValue.toString()).isEmpty()){return;}
                if (!DbMethods.isValidCode("CAT",scannerValue.toString()) || ItemCategory.getFromDb(scannerValue.toString()) == null){
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

             System.out.println("Enter the Indexes to remove from List, Enter 0 to Confirm, Enter -1 to Exit");
             System.out.print("> ");
             // ----
             String input = scanner.nextLine().trim();

             if (input.isEmpty()) {
                 System.out.print(ansi().cursorUp(1).eraseLine());
                 System.out.print("> ");
                 continue;
             }
             if (acceptExitAction(input).isEmpty()) return;

             if (input.matches("-1|\\d+")) {
                 option = Integer.parseInt(input);

                 if (option > 0 && option <= removeList.size()) {
                     removeList.remove(option - 1);
                 } else if (option == 0) {

                     for (Item item : removeList) {
                         item.deleteFromDb();
                     }

                     removeList.clear();
                 } else if (option == -1) {
                     break;
                 } else {
                     System.out.print(ansi().cursorUp(1).eraseLine());
                     System.out.print("> ");
                 }
             } else {

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
            boolean updateSuccess = true;
            if (selectedOption == modifyOptions.get("1")) { // TODO: When invalidName, it doesnt go back to starting position
                String newName = "";
                System.out.print("Enter new Name: ");

                while (newName.isEmpty() || !validationCheck.isValidName(newName) || newName.equals(item.getName())) {
                    newName = scanner.nextLine().trim();
                    if (newName.isEmpty() || !validationCheck.isValidName(newName) || newName.equals(item.getName())) {
                        System.out.print(ansi().cursorUp(1).eraseLine());
                        System.out.print("Enter new Name: ");
                    }
                }

                if (newName.compareToIgnoreCase("exit") == 0) {return;}

                System.out.println("\nAre you sure about the change? (y / n)");
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = item.setName(newName, true);
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
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = item.setUnitPrice(Double.parseDouble(scannerValue.toString()), true);
            } else if (selectedOption == modifyOptions.get("3")) {
                while (true) {
                    scannerValue.setLength(0);
                    System.out.print("Enter new Quantity: ");
                    if (scanner.hasNextLine()){
                        scannerValue.append(scanner.nextLine().trim());

                        if (scannerValue.isEmpty() || !scannerValue.toString().matches("-1|\\d+")) {
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
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = item.setQuantity(Integer.parseInt(scannerValue.toString()), true);
            } else if (selectedOption == modifyOptions.get("4")){
                while (true) {
                    scannerValue.setLength(0);
                    System.out.print("Supplier ID         :");
                    if (scanner.hasNext()) {
                        scannerValue.append(scanner.next());
                        if (acceptExitAction(scannerValue.toString()).isEmpty()){
                            return;
                        }
                        if (!DbMethods.isValidCode("SUP",scannerValue.toString()) || Supplier.getFromDb(scannerValue.toString()) == null){
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
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = item.setSupplier(Supplier.getFromDb(scannerValue.toString()), true);
            } else {
                while (true) {
                    scannerValue.setLength(0);
                    System.out.print("ItemCategory ID     :");
                    if (scanner.hasNext()) {
                        scannerValue.append(scanner.next());
                        if (acceptExitAction(scannerValue.toString()).isEmpty()){
                            return;
                        }
                        if (!DbMethods.isValidCode("CAT",scannerValue.toString()) || ItemCategory.getFromDb(scannerValue.toString()) == null){
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
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = item.setItemCategory(ItemCategory.getFromDb(scannerValue.toString()), true);
            }

            if (!updateSuccess) {
                System.out.println(ansi().fgRed().a("Something went wrong when updating, please try again later..."));
                pause();
            }

            clearScreen();
            System.out.println(ansi().fgBrightGreen().a(" Update an Item :\n=========================\n").reset());
            System.out.println(item);
        }
    }

    private void viewItems() {
        clearScreen();
        List<Item> itemList = Item.getAllItem();
        if (itemList == null) System.out.println("No item is present.");
        else {
            for (Item item : itemList) {
                System.out.println(item);
            }
        }
        pause();
    }

    // Filter by Name
    private void filterByName() {
        System.out.print("Enter item name: ");
        String query = scanner.nextLine().trim();

        if (query.isEmpty()) {System.out.print(ansi().cursorUp(1).eraseLine());};

        List<Item> filteredItems = items.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        displayFilteredItems(filteredItems);
    }

    // Filter by Unit Price
    private void filterByUnitPriceValue() {
        StringBuilder price = new StringBuilder();
        while (true) {
            price.setLength(0);
            System.out.print("Enter Unit Price     :");
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

                List<Item> filteredItems = items.stream().filter(item -> item.getUnitPrice() == Double.parseDouble(price.toString())).collect(Collectors.toList());
                displayFilteredItems(filteredItems);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }
    }

    private void filterByUnitPriceRange() {
        StringBuilder price = new StringBuilder();
        double start, end;
        while (true) {
            price.setLength(0);
            System.out.print("Enter Starting Range     :");

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

                start = Double.parseDouble(price.toString());
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        while (true) {
            price.setLength(0);
            System.out.print("Enter Ending Range       :");
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

                end = Double.parseDouble(price.toString());
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        List<Item> filteredItems = items.stream().filter(item -> item.getUnitPrice() >= start && item.getUnitPrice() <= end).collect(Collectors.toList());
        displayFilteredItems(filteredItems);
    }

    // Filter by Quantity
    private void filterByQuantityValue() {
        StringBuilder scannerValue = new StringBuilder();
        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Quantity       :");
            if (scanner.hasNextLine()){
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.isEmpty() || !scannerValue.toString().matches("-1|\\d+")) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()) {return;}

                if (Integer.parseInt(scannerValue.toString()) <0){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                List<Item> filteredItems = items.stream().filter(item -> item.getQuantity() == Integer.parseInt(scannerValue.toString())).collect(Collectors.toList());
                displayFilteredItems(filteredItems);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }
    }

    // Filter by Quantity Range
    private void filterByQuantityRange() {
        StringBuilder scannerValue = new StringBuilder();
        int start,end;
        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Quantity       :");
            if (scanner.hasNextLine()){
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.isEmpty() || !scannerValue.toString().matches("-1|\\d+")) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()) {return;}

                if (Integer.parseInt(scannerValue.toString()) <0){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                start = Integer.parseInt(scannerValue.toString());
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Quantity       :");
            if (scanner.hasNextLine()){
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.isEmpty() || !scannerValue.toString().matches("-1|\\d+")) {
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()) {return;}

                if (Integer.parseInt(scannerValue.toString()) <0){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                end = Integer.parseInt(scannerValue.toString());
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }

        List<Item> filteredItems = items.stream().filter(item -> item.getQuantity() >= start && item.getQuantity() <= end).collect(Collectors.toList());
        displayFilteredItems(filteredItems);
    }

    // Filter by Supplier ID
    private void filterBySupplierID() {
        StringBuilder scannerValue = new StringBuilder();

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Supplier ID         :");
            if (scanner.hasNextLine()) {
                scannerValue.append(scanner.nextLine().trim());
                if (scannerValue.isEmpty()){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(scannerValue.toString()).isEmpty()){return;}

                if (!DbMethods.isValidCode("SUP",scannerValue.toString()) || Supplier.getFromDb(scannerValue.toString()) == null){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                List<Item> filteredItems = items.stream()
                        .filter(item -> item.getSupplier().getId().contentEquals(scannerValue))
                        .collect(Collectors.toList());
                displayFilteredItems(filteredItems);
                break;
            } else{System.out.print(ansi().cursorUp(1).eraseLine());}
        }
    }

    // Filter by ItemCategory ID
    private void filterByItemCategoryID() {
        StringBuilder scannerValue = new StringBuilder();

        while (true) {
            scannerValue.setLength(0);
            System.out.print("ItemCategory ID         :");
            if (scanner.hasNextLine()) {
                scannerValue.append(scanner.nextLine().trim());

                if (scannerValue.isEmpty()){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }

                if (acceptExitAction(scannerValue.toString()).isEmpty()){return;}

                if (!DbMethods.isValidCode("CAT",scannerValue.toString()) || ItemCategory.getFromDb(scannerValue.toString()) == null){
                    System.out.print(ansi().cursorUp(1).eraseLine());
                    continue;
                }
                List<Item> filteredItems = items.stream()
                        .filter(item -> item.getItemCategory().getId().contentEquals(scannerValue))
                        .collect(Collectors.toList());
                displayFilteredItems(filteredItems);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.nextLine();
            }
        }
    }

    // Display the filtered items
    private void displayFilteredItems(List<Item> filteredItems) {filteredItems.forEach(item -> System.out.println(item.toString()));}

    private void quit() {exitFlag = true;}

    private void quitSearch() {exitAction = true;}
}