package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
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
        if (itemFlowList == null || itemFlowList.isEmpty()) System.out.println("No item flow data is present.");
        else {
            // print all item flow details in table form
            System.out.printf("+%s+\n", "-".repeat(48));
            System.out.printf("|%-24s|%-11s|%20s|%8s|%5s|\n+%s+\n",
                    "Update Time",
                    "Supplier Id",
                    "Supplier Name",
                    "Item Id",
                    "Delta",
                    "-".repeat(54));

            for (ItemFlow itemFlow : itemFlowList) {
                System.out.printf("|%-24s|%-11s|%20s|%8s|%5d|\n",
                        itemFlow.getUpdateTimeIdAsString(),
                        itemFlow.getSupplier().getId(),
                        printShorten(itemFlow.getSupplier().getName(), 20),
                        itemFlow.getItem().getId(),
                        itemFlow.getDelta());
            }
            System.out.println("+" + "-".repeat(48) + "+");
        }
        pause();
    }

    private void updateItemFlow() {
        clearScreen();
        System.out.println(ansi().fgBrightGreen().a(" Enter New Flow Update Details:\n===============================\n").reset());

        ItemFlow newItemFlow = new ItemFlow();

        // item input
        System.out.print(ansi().a("""
                Enter id for the updating item:
                Enter delta of the quantity:
                """).cursorUp(2).cursorRight(32).fgBrightBlue());

        while (newItemFlow.getItem() == null) {
            String inputItemId = scanner.nextLine().trim();
            if (inputItemId.isEmpty()) {
                System.out.print(ansi().cursorUp(1).cursorRight(32));
                continue;
            }
            Item inputItem = Item.getFromDb(inputItemId);
            if (inputItem != null) {
                newItemFlow.setItem(inputItem);
                break;
            }
            System.out.print(ansi().cursorDown(2).bgRed().a("Invalid item id.").reset().cursorUp(3));
            System.out.print(ansi().cursorToColumn(0).eraseLine().a("Enter id for the updating item: "));
        }

        System.out.print(ansi().cursorRight(29).fgBrightBlue());

        while (newItemFlow.getDelta() == 0) {
            String inputDelta = scanner.nextLine().trim();
            if (inputDelta.isEmpty() || !inputDelta.matches("-?\\d+")) {
                System.out.print(ansi().reset().cursorUp(1).cursorToColumn(0).eraseLine().a("Enter delta of the quantity: ").fgBrightBlue());
                continue;
            }
            try {
                newItemFlow.setDelta(Integer.parseInt(inputDelta));
                break;
            } catch (NumberFormatException e) {
                System.out.print(ansi().cursorDown(1).cursorToColumn(0).eraseLine().bgRed().a("Number is too small / big.").reset()
                        .cursorUp(2).cursorToColumn(0).eraseLine().a("Enter delta of the quantity: ").fgBrightBlue());
            }
        }

        System.out.print(ansi().cursorDown(1).cursorToColumn(0).eraseLine());
        newItemFlow.setSupplier(newItemFlow.getItem().getSupplier());
        boolean createSuccessful = newItemFlow.addToDb();
        if (createSuccessful) System.out.println(ansi().fgBrightGreen().a("Item flow details updated.").reset());
        else System.out.println(ansi().bgRed().a("Item flow creation was unsuccessful, please try again later.").reset());
        pause();
    }

    private String printShorten(String originalString, int limit) {
        if (originalString.length() <= limit) return originalString;
        return originalString.substring(0, limit - 3) + "...";
    }

    private void returnToStaffMenu() {
        exitFlag = true;
    }
}
