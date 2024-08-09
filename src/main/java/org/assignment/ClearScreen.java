package org.assignment;

public class ClearScreen {
    // only works on terminal
    public static void main(String[] args) {
        System.out.print("Hello");
        try {
            Thread.sleep(2000); // Pause to see "Hello" before clearing
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Screen cleared!");
    }
}