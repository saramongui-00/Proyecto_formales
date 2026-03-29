package co.edu.uptc.lenguajesformales.view;

import java.util.Scanner;

public class MainWindow {
    
    private Scanner scanner;

    public MainWindow() {
        scanner = new Scanner(System.in);
    }

    public String requestString() {
        System.out.print("Enter a string: ");
        return scanner.nextLine();
    }

    public void showResult(boolean result) {
        System.out.println("Result: " + result);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
