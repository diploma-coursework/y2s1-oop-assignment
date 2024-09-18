package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.ItemFlow;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.HashMap;
import java.util.List;

public class ItemFlowMenu extends BaseMenu {
    private static final HashMap<String, Option> options1 = new HashMap<>();

    public ItemFlowMenu() {
        ItemFlowMenu.options1.put("1", new Option("See Item Flow Statistics", this::seeItemFlowStatistics));
        ItemFlowMenu.options1.put("2", new Option("Update Item Flow", this::updateItemFlow));
        ItemFlowMenu.options1.put("3", new Option("Other stuff", this::updateItemFlow));
        ItemFlowMenu.options1.put("4", new Option("Generate XXX report", this::updateItemFlow));
        ItemFlowMenu.options1.put("0", new Option(ansi().fgRed().a("Return to staff menu...").reset().toString(), this::returnToStaffMenu));

        exitFlag = false;
    }

    @Override
    public void start() {
        while (!exitFlag)
        {
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a(" Item Flow Management Menu\n===========================").reset());
            displayOptions(ItemFlowMenu.options1);
            getUserOptions(ItemFlowMenu.options1);
        }
    }

    private void seeItemFlowStatistics() {
        clearScreen();
        List<ItemFlow> itemFlowList = ItemFlow.getAllItemFlow();
        if (itemFlowList == null) System.out.println("No item flow data is present.");
        else {
            // TODO: do stuff, calculate, categorize by month / supplier watever
        }
        pause();
    }

    private void updateItemFlow() {
        clearScreen();
        System.out.println(ansi().fgBrightGreen().a(" Create a new Item Category:\n============================\n").reset());


        pause();
    }



    private void returnToStaffMenu() {
        exitFlag = true;
    }
}
