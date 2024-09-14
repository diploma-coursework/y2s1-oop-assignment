package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import java.util.HashMap;

public class MainMenu extends BaseMenu {
    private static final HashMap<String, Option> options = new HashMap<>();

    public MainMenu() {
        MainMenu.options.put("1", new Option("Staff Login", this::staffLogin));
        MainMenu.options.put("0", new Option("Exit", this::exit));

        exitFlag = false;
    }

    @Override
    public void start() {
        while (!exitFlag) {
            System.out.println("Welcome to XXX Furniture Store!\n===============================");
            displayOptions(MainMenu.options);
            getUserOptions(MainMenu.options);
        }
    }

    private void staffLogin() {
        System.out.println("login....");
        pause();
        clearScreen();
        pause();
    }


    private void exit() {
        System.out.println("Thank you for using ....");
        exitFlag = true;
    }
}
