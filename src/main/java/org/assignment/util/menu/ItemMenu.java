package org.assignment.util.menu;

import org.assignment.util.DbMethods;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;
import org.assignment.util.validationCheck;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class ItemMenu extends BaseMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Option> menuOptions = new HashMap<>();

    public ItemMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Add Item", this::addItem));
        menuOptions.put("2", new Option("Remove Item", this::removeItem));
        menuOptions.put("3", new Option("View Items", this::viewItems));
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
                scanner.next();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Unit Price     :");
            if (scanner.hasNextDouble()) {
                scannerValue.append(scanner.nextDouble());
//                acceptExitAction(scanner.nextDouble()).map(Number::doubleValue).orElse(-1.0)
                if (acceptExitAction(Double.valueOf(scannerValue.toString())).isEmpty()){
                    return;
                }
                item.setUnitPrice(Double.parseDouble(scannerValue.toString()));
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.next();
            }
        }

        while (true) {
            scannerValue.setLength(0);
            System.out.print("Item Quantity       :");
            if (scanner.hasNextInt()) {
                scannerValue.append(scanner.nextInt());
//                acceptExitAction(scanner.nextInt()).map(Number::intValue).orElse(-1)
                if (acceptExitAction(Integer.valueOf(scannerValue.toString())).isEmpty()){
                    return;
                }
                item.setQuantity(Integer.parseInt(scannerValue.toString()));
                scannerValue.setLength(0);
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.next();
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
                scanner.next();
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
                scanner.next();
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
                    clearScreen();
                    System.out.println(item);
                    System.out.print("To Confirm Adding [Enter 1] else [Enter 0]: ");
                    continue;
                }
                if (i == 0) {
                    System.out.println("Action Terminated..");
                    pause();
                    break;
                }
                item.addToDb();
                pause();
                break;
            } else{
                System.out.print(ansi().cursorUp(1).eraseLine());
                scanner.next();
            }
        }
    }

    private void removeItem() {
        System.out.println("Removing an item...");
        // TODO: choose by id
        List<Item> removeList = new ArrayList<>();

        System.out.println("Enter the IDs' of the Items to Remove: ");
        while (scanner.hasNext()) {
            String input = scanner.next();
            if ("exit".equalsIgnoreCase(input)) {
                break; // Exit the loop if 'exit' is entered
            }
            if (DbMethods.isValidCode("ITEM",input) && Item.getFromDb(input)!= null){
                removeList.add(Item.getFromDb(input));
            }
        }
        int option;
         do {
             // TODO: print ArrayList properly
             // accept IDs'
             int counter = 0;
             System.out.println("These are the Items that will be Deleting");
             System.out.println("Enter the Indexes to remove from List, Enter 0 to Confirm");
             option = -1;
             for (Item index: removeList){
                 counter++;
                 System.out.println(counter + ") " + index);
             }

             // logic for removing from removeList
             System.out.println("> ");
             option = scanner.nextInt();
             if (option <= counter && option != 0){
                 removeList.remove(counter-1);
             }

             // logic for removing
             if (option == 0){
                 for (Item item: removeList){
                     item.deleteFromDb();
                 }
             }
        } while(option == 0);
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