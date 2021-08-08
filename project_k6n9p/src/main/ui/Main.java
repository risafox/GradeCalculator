package ui;

import java.io.FileNotFoundException;

// Runs application, creating a new calculator app instance
public class Main {
    public static void main(String[] args) {
        try {
            new CalculatorAppGUI(); // switch this to CalculatorAppTerminal() to run terminal application
            //new CalculatorAppTerminal();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
