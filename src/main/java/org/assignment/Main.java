package org.assignment;

import org.assignment.util.menu.ItemMenu;
import org.assignment.util.menu.MainMenu;

public class Main {
    public static void main(String[] args) {
        new MainMenu().start();

        ItemMenu itemMenu = new ItemMenu();
        itemMenu.start();

    }

}
