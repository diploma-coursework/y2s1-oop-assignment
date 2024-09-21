package org.assignment;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;
import org.assignment.util.menu.*;
import java.util.Scanner;

// FIXME: testing Concurrent

public class Main extends BaseMenu {

    public static void main(String[] args) {
        new ItemCategoryMenu().start();
    }
}
