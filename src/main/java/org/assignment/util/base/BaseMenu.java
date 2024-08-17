package org.assignment.util.base;

import java.util.HashMap;
import java.util.Scanner;

public abstract class BaseMenu {
    protected static final Scanner scanner = new Scanner(System.in);

    public void start() {};

    protected void displayOptions(HashMap<Character, Option> options) {
        options.forEach((optionIdentifier, option) -> {
            System.out.printf("%c - %s\n", optionIdentifier, option.description);
        });
    }

    protected void getUserOptions(HashMap<Character, Option> options) {
        System.out.print("> ");
        final String enterValue = scanner.next();
        // ....
    }

    protected record Option(String description, Runnable execute) {}
}
