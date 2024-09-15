package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.ItemCategory;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.HashMap;
import java.util.List;

public class ItemCategoryMenu extends BaseMenu {
    private static final HashMap<String, Option> options = new HashMap<>();

    // TODO: think of useful & suitable options
    public ItemCategoryMenu() {
        ItemCategoryMenu.options.put("1", new Option("See Item Categories", this::seeItemCategories));
        ItemCategoryMenu.options.put("2", new Option("Create a New Item Category", this::createNewItemCategory));
        ItemCategoryMenu.options.put("3", new Option("Update a Item Category Details", this::updateItemCategoryDetails));
        ItemCategoryMenu.options.put("4", new Option("Delete a Item Category", this::deleteItemCategory));
        ItemCategoryMenu.options.put("0", new Option(ansi().fgRed().a("Return to staff menu...").reset().toString(), this::returnToStaffMenu));

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
            displayOptions(ItemCategoryMenu.options);
            getUserOptions(ItemCategoryMenu.options);
        }
    }

    private void seeItemCategories() {
        clearScreen();
        List<ItemCategory> itemCategoryList = ItemCategory.getAllItemCategory();
        if (itemCategoryList == null) System.out.println("No item category is present.");
        else {
            for (ItemCategory itemCategory : itemCategoryList) {
                System.out.println(itemCategory);
            }
        }
        pause();
    }

    private void createNewItemCategory() {

    }

    private void updateItemCategoryDetails() {

    }

    private void deleteItemCategory() {

    }

    private void returnToStaffMenu() {
        exitFlag = true;
    }
}
