package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Staff;
import org.mindrot.jbcrypt.BCrypt;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.HashMap;

public class MainMenu extends BaseMenu {
    private static final HashMap<String, Option> options = new HashMap<>();

    public MainMenu() {
        MainMenu.options.put("1", new Option("Staff Login", this::staffLogin));
        MainMenu.options.put("0", new Option(ansi().fgRed().a("Exit").reset().toString(), this::exit));

        exitFlag = false;
    }

    @Override
    public void start() {
        String welcomeArt = """
                 __          __  _                            _                    \s
                 \\ \\        / / | |                          | |                   \s
                  \\ \\  /\\  / /__| | ___ ___  _ __ ___   ___  | |_ ___              \s
                   \\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\             \s
                    \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) |            \s
                   __\\/  \\/_\\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/             \s
                  / __ \\ / __ \\|  __ \\  |  ____|              (_) |                \s
                 | |  | | |  | | |__) | | |__ _   _ _ __ _ __  _| |_ _   _ _ __ ___\s
                 | |  | | |  | |  ___/  |  __| | | | '__| '_ \\| | __| | | | '__/ _ \\
                 | |__| | |__| | |      | |  | |_| | |  | | | | | |_| |_| | | |  __/
                  \\____/ \\____/|_|      |_|_  \\__,_|_|  |_| |_|_|\\__|\\__,_|_|  \\___|
                  / ____| |               | |                                      \s
                 | (___ | |_ ___  _ __ ___| |                                      \s
                  \\___ \\| __/ _ \\| '__/ _ \\ |                                      \s
                  ____) | || (_) | | |  __/_|                                      \s
                 |_____/ \\__\\___/|_|  \\___(_)                                      \s
                                                                                   \s
                                                                                   \s
                """;

        while (!exitFlag)
        {
            clearScreen();
            System.out.println(ansi().fgBrightGreen().a(welcomeArt).reset());
            displayOptions(MainMenu.options);
            getUserOptions(MainMenu.options);
        }
    }

    //TODO: check the password hashing methods for STF0016,STF0017
    // password: pop12345@,Lee234@#56
    private void staffLogin() {
        clearScreen();

        System.out.println(ansi().fgBrightGreen().a("Staff Login\n=============").reset());

        System.out.print(ansi().fgRgb(8552916).a("Staff Id: "));
        String inputStaffId = scanner.nextLine();

        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();
        System.out.print(ansi().reset());

        Staff loginStaff = Staff.getFromDb(inputStaffId);
        if (loginStaff != null && BCrypt.checkpw(inputPassword, loginStaff.getPassword())) {
            new StaffMenu(loginStaff).start();
        } else {
            System.out.println(ansi().bgBrightRed().a("Invalid credentials.").reset());
            pause();
        }
    }

    private void exit() {
        System.out.println("Thank you for using OOP Furniture Store Inventory System!");
        exitFlag = true;
    }
}
