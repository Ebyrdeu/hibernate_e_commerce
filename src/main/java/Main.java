import ui.AppMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppMenu appMenu = new AppMenu();

        appMenu.start(scanner);
    }
}
