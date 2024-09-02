package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import java.util.HashMap;

public class MainMenu extends BaseMenu {
    private final HashMap<String, Option> option1, option2C, option2S;

    public MainMenu() {
        this.option1 = new HashMap<>();
        this.option2C = new HashMap<>();
        this.option2S = new HashMap<>();

        this.option1.put("1", new Option("Customer Account", this::customerActionSelection));
        this.option1.put("2", new Option("Staff Account", this::staffActionSelection));
        this.option1.put("0", new Option("Exit", () -> {}));

        // TODO: provide correct option runnable
        this.option2C.put("1", new Option("Login", this::customerActionSelection));
        this.option2C.put("2", new Option("Signup", this::staffActionSelection));
        this.option2C.put("0", new Option("Exit", () -> {}));

        this.option2S.put("1", new Option("Login", this::customerActionSelection));
        this.option2S.put("0", new Option("Exit", () -> {}));
    }

    @Override
    public void start() {
        System.out.println("Welcome to XXX Furniture Store!\n===============================");
        displayOptions(this.option1);
        getUserOptions(this.option1);
    }

    private void customerActionSelection() {
        // TODO
    }


    private void staffActionSelection() {
        // TODO
    }



}
