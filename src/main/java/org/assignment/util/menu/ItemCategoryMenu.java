package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.ItemCategory;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.HashMap;
import java.util.List;

public class ItemCategoryMenu extends BaseMenu {
    private static final HashMap<String, Option> options1 = new HashMap<>(), options2 = new HashMap<>();

    public ItemCategoryMenu() {
        ItemCategoryMenu.options1.put("1", new Option("See Item Categories", this::seeItemCategories));
        ItemCategoryMenu.options1.put("2", new Option("Create a New Item Category", this::createNewItemCategory));
        ItemCategoryMenu.options1.put("3", new Option("Update Item Category Details", this::updateItemCategoryDetails));
        ItemCategoryMenu.options1.put("4", new Option("Delete a Item Category", this::deleteItemCategory));
        ItemCategoryMenu.options1.put("0", new Option(ansi().fgRed().a("Return to staff menu...").reset().toString(), this::returnToStaffMenu));

        ItemCategoryMenu.options2.put("1", new Option("Edit Name", () -> {}));
        ItemCategoryMenu.options2.put("2", new Option("Edit Description", () -> {}));
        ItemCategoryMenu.options2.put("0", new Option(ansi().fgBrightGreen().a("Save").reset().toString(), () -> {}));

        exitFlag = false;
    }

    @Override
    public void start() {
        String[] asciiArt = """
                   __________.
                  /_/-----/_/|   __
                  ( ( ' ' ( (| /'--'\\
                  (_( ' ' (_(|/.    .\\
                  / /=====/ /|  '||'
                 /_//____/_/ |   ||
                (o|:.....|o) |   ||
                |_|:_____|_|/'  _||_
                 '        '    /____\\
                """.split("\\r?\\n|\\r");

        while (!exitFlag)
        {
            clearScreen();
            // print ascii art on the right
            for (int i = 0; i < asciiArt.length; i++) {
                System.out.print(ansi().cursor(i + 3, 45).a(asciiArt[i]).reset());
            }

            // print the user menu on 0, 0
            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(" Item Category Menu\n====================").reset());
            displayOptions(ItemCategoryMenu.options1);
            getUserOptions(ItemCategoryMenu.options1);
        }
    }

    private void seeItemCategories() {
        clearScreen();
        List<ItemCategory> itemCategoryList = ItemCategory.getAllItemCategory();
        if (itemCategoryList == null) System.out.println("No item category is present.");
        else {
            for (ItemCategory itemCategory : itemCategoryList) {
                System.out.print(itemCategory);
                System.out.printf("Items      : %s\n\n", ansi().fgCyan().a(itemCategory.getAllItems().size()).reset());
            }
        }
        pause();
    }

    private void createNewItemCategory() {
        clearScreen();
        System.out.println(ansi().fgBrightGreen().a(" Create a new Item Category:\n============================\n").reset());

        // print form prompts
        System.out.print(ansi().a("Category Name       :\nBrief Description   :").cursorUp(1).cursorRight(1).a(ansi().fgBlue()));

        ItemCategory newItemCategory = new ItemCategory("", "");

        // get both inputs and retry if input is empty
        while (newItemCategory.getName().isEmpty()) {
            newItemCategory.setName(scanner.nextLine().trim());
            if (newItemCategory.getName().isEmpty()) System.out.print(ansi().cursorUp(1).cursorRight(22));
        }

        System.out.print(ansi().cursorRight(22).a(ansi().fgBrightMagenta()));

        while (newItemCategory.getDescription().isEmpty()) {
            newItemCategory.setDescription(scanner.nextLine().trim());
            if (newItemCategory.getDescription().isEmpty()) System.out.print(ansi().cursorUp(1).cursorRight(22));
        }

        // get confirmation from user
        System.out.println(ansi().reset().cursor(6, 0)
                .fgBrightGreen().a("\nCreate the following item category? (y / n) ").reset());

        boolean userConfirm = getYesNoOption();
        if (userConfirm) {
            boolean createSuccessful = newItemCategory.addToDb();

            if (createSuccessful) System.out.println("\nItem category created successfully!");
            else System.out.println(ansi().fgRed().a("\nFailed to created item category, please try again later.").reset());
        } else {
            System.out.println("\nCanceled process...");
        }
        pause();
    }

    private void updateItemCategoryDetails() {
        clearScreen();

        System.out.println(ansi().fgBrightGreen().a(" Update an Item Category:\n=========================\n").reset());

        // get category id
        String inputCategoryId = "";
        System.out.println("Category Id:");
        System.out.print(ansi().a("(Type 'exit' to exit)").cursorUp(1).cursorLeft(8));
        while (inputCategoryId.isEmpty()) {
            inputCategoryId = scanner.nextLine().trim();

            if (inputCategoryId.isEmpty()) System.out.print(ansi().cursorUp(1).cursorRight(13));
        }
        // exit if requested
        if (inputCategoryId.equalsIgnoreCase("exit")) return;

        // get ItemCategory
        ItemCategory itemCategory = ItemCategory.getFromDb(inputCategoryId);
        System.out.println(ansi().eraseLine());

        // exit if category id input is invalid
        if (itemCategory == null) {
            System.out.println(ansi().bgBrightRed().a("Invalid category id.").reset());
            pause();
            return;
        }

        // show current details
        System.out.println(ansi().cursorUp(2).eraseLine().a(itemCategory));

        while (true) {
            // get user action (edit / save / exit)
            displayOptions(ItemCategoryMenu.options2);
            Option selectedOption = getUserOptions(ItemCategoryMenu.options2);

            // "0" for exit
            if (selectedOption == ItemCategoryMenu.options2.get("0")) return;

            // "1" for edit name
            boolean updateSuccess = true;
            if (selectedOption == ItemCategoryMenu.options2.get("1")) {
                String newName = "";
                System.out.print(ansi().eraseLine().newline().a("Enter new Name: "));

                while (newName.isEmpty()) {
                    newName = scanner.nextLine().trim();
                    if (newName.isEmpty()) System.out.print(ansi().cursorUp(1).cursorRight(15));
                }

                System.out.println("\nAre you sure about the change? (y / n)");
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = itemCategory.setName(newName, true);
            } else {
                // "2" for edit description
                String newDescription = "";
                System.out.print(ansi().eraseLine().newline().a("Enter new Description: "));

                while (newDescription.isEmpty()) {
                    newDescription = scanner.nextLine().trim();
                    if (newDescription.isEmpty()) System.out.print(ansi().cursorUp(1).cursorRight(15));
                }

                System.out.println("\nAre you sure about the change? (y / n)");
                boolean updateConfirmation = getYesNoOption();
                if (updateConfirmation) updateSuccess = itemCategory.setDescription(newDescription, true);
            }

            if (!updateSuccess) {
                System.out.println(ansi().fgRed().a("Something went wrong when updating, please try again later..."));
                pause();
            }

            clearScreen();
            System.out.println(ansi().fgBrightGreen().a(" Update an Item Category:\n=========================\n").reset());
            System.out.println(itemCategory);
        }
    }

    private void deleteItemCategory() {
        clearScreen();

        System.out.println(ansi().fgBrightGreen().a(" Delete an Item Category:\n=========================\n").reset());

        // get category id
        String inputCategoryId = "";
        System.out.println("Category Id:");
        System.out.print(ansi().a("(Type 'exit' to exit)").cursorUp(1).cursorLeft(8));
        while (inputCategoryId.isEmpty()) {
            inputCategoryId = scanner.nextLine().trim();

            if (inputCategoryId.isEmpty()) System.out.print(ansi().cursorUp(1).cursorRight(13));
        }
        // exit if requested
        if (inputCategoryId.equalsIgnoreCase("exit")) return;

        // get ItemCategory
        ItemCategory itemCategory = ItemCategory.getFromDb(inputCategoryId);
        System.out.println(ansi().eraseLine());

        // exit if category id input is invalid
        if (itemCategory == null) {
            System.out.println(ansi().bgBrightRed().a("Invalid category id.").reset());
            pause();
            return;
        }

        // show current details
        System.out.print(ansi().cursorUp(2).eraseLine().a(itemCategory));
        System.out.printf("Items      : %s\n\n", ansi().fgCyan().a(itemCategory.getAllItems().size()).reset());

        // reject deletion if there's item in the category
        if (!itemCategory.getAllItems().isEmpty()) {
            System.out.println(ansi()
                    .bgRed()
                    .a("You are not allowed to delete this item category, there are still items under the category.")
                    .reset());
            pause();
            return;
        }

        System.out.println("Delete the following item category? (y / n)");
        boolean deleteConfirmation = getYesNoOption();
        if (deleteConfirmation) {
            boolean deleteSuccessful = itemCategory.deleteFromDb();
            if (deleteSuccessful) System.out.println(ansi().fgBrightGreen().a("Deletion successful!").reset());
            else System.out.println(ansi().fgRed().a("Something went wrong during deletion, please try again later..."));
        } else {
            System.out.println("Process canceled...");
        }

        pause();
    }

    private void returnToStaffMenu() {
        exitFlag = true;
    }
}
