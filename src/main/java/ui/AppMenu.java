package ui;

import utils.Utils;

import java.util.Scanner;

public class AppMenu implements Menu {
    private static final String options = """
            ===================
            1 - Categories
            2 - Customers
            3 - Products
            4 - Orders
            """;


    @Override
    public void start(Scanner scanner) {
        boolean cycle = true;
        CategoryMenu categoryMenu = new CategoryMenu();
        CustomerMenu customerMenu = new CustomerMenu();
        ProductMenu productMenu = new ProductMenu();
        OrderMenu orderMenu = new OrderMenu();
        do {
            Utils.log(options);
            switch (scanner.nextLine()) {
                case "1" -> categoryMenu.start(scanner);
                case "2" -> customerMenu.start(scanner);
                case "3" -> productMenu.start(scanner);
                case "4" -> orderMenu.start(scanner);
                case "E", "e" -> cycle = false;
                default -> Utils.log("Wrong Key");
            }
        } while (cycle);
    }
}
