package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemFlow;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.HashMap;
import java.util.List;

public class ItemFlowMenu extends BaseMenu {
    private static final HashMap<String, Option> options1 = new HashMap<>();

    public ItemFlowMenu() {
        ItemFlowMenu.options1.put("1", new Option("See Item Flow Statistics", this::seeItemFlowStatistics));
        ItemFlowMenu.options1.put("2", new Option("Update Item Flow", this::updateItemFlow));
        ItemFlowMenu.options1.put("0", new Option(ansi().fgRed().a("Return to item menu...").reset().toString(), this::returnToStaffMenu));

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
            String tableEdge = "+" + "-".repeat(24) + "+"
                    + "-".repeat(11) + "+"
                    + "-".repeat(20) + "+"
                    + "-".repeat(8) + "+"
                    + "-".repeat(20) + "+"
                    + "-".repeat(5) + "+";
            System.out.println(tableEdge);

            // table labels
            System.out.printf("|%-24s|%-11s|%-20s|%-8s|%-20s|%-5s|\n",
                    "Update Time",
                    "Supplier Id",
                    "Supplier Name",
                    "Item Id",
                    "Item Name",
                    "Delta");
            System.out.println(tableEdge);


            for (ItemFlow itemFlow : itemFlowList) {
                String rowData = String.format("|%-24s|%-11s|%-20s|%-8s|%-20s|%-5s|",
                        itemFlow.getUpdateTimeIdAsString(),
                        itemFlow.getSupplier().getId(),
                        printShorten(itemFlow.getSupplier().getName(), 20),
                        itemFlow.getItem().getId(),
                        printShorten(itemFlow.getItem().getName(), 20),
                        itemFlow.getDelta());

                if (itemFlow.getDelta() > 0) System.out.println(ansi().bgGreen().a(rowData).reset());
                else System.out.println(ansi().bgRed().a(rowData).reset());
            }
            System.out.println(tableEdge);
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

        // print item and go to next input
        System.out.print(ansi().reset().cursorDown(2).eraseLine().cursorDown(1)
                .a(newItemFlow.getItem()).cursor(3, 30).fgBrightBlue());

        // delta input
        while (newItemFlow.getDelta() == 0) {
            String inputDelta = scanner.nextLine().trim();
            if (inputDelta.isEmpty() || !inputDelta.matches("-?\\d+")) {
                System.out.print(ansi().reset().cursorUp(1).cursorToColumn(0).eraseLine().a("Enter delta of the quantity: ").fgBrightBlue());
                continue;
            }

            try {
                newItemFlow.setDelta(Integer.parseInt(inputDelta));
                // error if negative delta is more than available quantity (cant ship out more than we store)
                if (newItemFlow.getDelta() == 0) throw new IllegalArgumentException("Delta cannot be zero.");

                if (newItemFlow.getDelta() < 0 && Math.abs(newItemFlow.getDelta()) > newItemFlow.getItem().getQuantity()) {
                    newItemFlow.setDelta(0);
                    throw new IllegalArgumentException("Cannot ship out more than available stock.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print(ansi().reset().cursorDown(1).cursorToColumn(0).eraseLine().bgRed().a("Number is too small / big.").reset()
                        .cursor(3, 0).eraseLine().a("Enter delta of the quantity: ").fgBrightBlue());
            } catch (IllegalArgumentException e) {
                System.out.print(ansi().reset().cursorDown(1).cursorToColumn(0).eraseLine().bgRed().a(e.getMessage()).reset()
                        .cursor(3, 0).eraseLine().a("Enter delta of the quantity: ").fgBrightBlue());
            }
        }

        System.out.print(ansi().cursorDown(1).cursorToColumn(0).eraseLine());
        newItemFlow.setSupplier(newItemFlow.getItem().getSupplier());
        boolean createSuccessful = newItemFlow.addToDb();

        if (createSuccessful) {
            int oldQuantity = newItemFlow.getItem().getQuantity();
            boolean adjustStockSuccessful = newItemFlow.getItem().adjustStock(newItemFlow.getDelta());
            if (!adjustStockSuccessful) System.out.println("Adjustment of quantity of item was unsuccessful...");
            else {
                clearScreen();
                System.out.println(ansi().fgBrightGreen().a("Item flow details updated.").reset());
                System.out.printf("Quantity for item %s %s from %d to %d.\n",
                        newItemFlow.getItem().getId(),
                        newItemFlow.getDelta() > 0 ? "increased" : "decreased",
                        oldQuantity,
                        newItemFlow.getItem().getQuantity());
            }
        }
        else System.out.println(ansi().bgRed().a("Item flow creation was unsuccessful, please try again later.").reset());
        pause();
    }

    private void returnToStaffMenu() {
        exitFlag = true;
    }
}
