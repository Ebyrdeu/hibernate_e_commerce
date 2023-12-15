package ui;

import entities.Customer;
import entities.Order;
import entities.Status;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryImpl;
import repository.product.ProductRepository;
import repository.product.ProductRepositoryImpl;
import utils.Utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class OrderMenu implements Menu {
    private static final String options = """
            ===================
            1 - Create order
            2 - Find order
            3 - Find all orders
            4 - Update order
            5 - Delete order
            6 - Show ordered products
            8 - Back to main menu
            """;
    private final OrderRepository orderRepository = new OrderRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    private void printTableHeader() {
        System.out.printf("| %-2s | %-15s | %-21s | %-15s | %-8s | %-21s | %-21s |%n", "ID", "User Full Name", "Ordered Date", "Shipping Status", "Total Price", "Date Added", "Date Modified");
    }

    private void printTableContent(Order entity) {
        System.out.printf("| %-2d | %-15s | %-21s | %-15s | %-8s | %-21s | %-21s |%n", entity.id(), entity.customer().firstName() + " " + entity.customer().lastName(), entity.orderDate(), entity.status(), entity.total(), entity.createdAt(), entity.updatedAt());
    }


    private Status chosenStatus(int value) {
        return switch (value) {
            case 0 -> Status.PROCESSING;
            case 1 -> Status.PENDING;
            case 2 -> Status.SHIPPED;
            case 3 -> Status.DELIVERED;
            default -> null;
        };
    }

    private void createOrder(Scanner scanner) {
        Order order = new Order();

        Utils.log("Enter Customer id");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Product id");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter total price");
        int totalPrice = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Shipping status");
        int status = scanner.nextInt();
        scanner.nextLine();

        order.setCustomer(new Customer().setId((long) customerId));
        order.setTotal(BigDecimal.valueOf(totalPrice));
        order.setStatus(chosenStatus(status));

        orderRepository.add(order);

        orderRepository.addProductsToOrder((long) productId);

        List<Order> orders = orderRepository.getAll();

        printTableHeader();
        orders.forEach(this::printTableContent);
    }

    private void findOrder(Scanner scanner) {
        Order order = new Order();

        Utils.log("Enter Order id");
        int id = scanner.nextInt();
        scanner.nextLine();

        order.setId((long) id);

        Order result = orderRepository.get(order.id());

        printTableHeader();
        printTableContent(result);
    }

    private void findAllOrders() {
        List<Order> orders = orderRepository.getAll();

        printTableHeader();
        orders.forEach(this::printTableContent);
    }

    private void updateOrder(Scanner scanner) {
        Order order = new Order();

        Utils.log("Enter Order id");
        int status = scanner.nextInt();
        scanner.nextLine();

        order.setStatus(chosenStatus(status));

        orderRepository.update(order);

        List<Order> result = orderRepository.getAll();

        printTableHeader();
        result.forEach(this::printTableContent);
    }

    private void deleteOrder(Scanner scanner) {
        Order order = new Order();

        Utils.log("Enter Order id");
        int id = scanner.nextInt();
        scanner.nextLine();

        order.setId((long) id);

        orderRepository.remove(order);

        List<Order> result = orderRepository.getAll();

        printTableHeader();
        result.forEach(this::printTableContent);
    }

    private void showOrderedProducts(Scanner scanner) {

    }

    @Override
    public void start(Scanner scanner) {
        boolean cycle = true;
        do {
            Utils.log(options);
            switch (scanner.nextLine()) {
                case "1" -> createOrder(scanner);
                case "2" -> findOrder(scanner);
                case "3" -> findAllOrders();
                case "4" -> updateOrder(scanner);
                case "5" -> deleteOrder(scanner);
                case "7" -> showOrderedProducts(scanner);
                case "8" -> cycle = false;
                default -> Utils.log("Wrong Key");
            }
            if (cycle) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println();
        } while (cycle);
    }
}
