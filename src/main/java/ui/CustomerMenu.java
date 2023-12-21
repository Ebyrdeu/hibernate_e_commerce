package ui;

import entities.Customer;
import repository.customer.CustomerRepository;
import repository.customer.CustomerRepositoryImpl;
import utils.Utils;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu implements Menu {
    private static final String options = """
            ===================
            1 - Create customer
            2 - Find customer
            3 - Find all customers
            4 - Update customer
            5 - Delete customer
            6 - Back to main menu
            """;
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();

    private void printTableHeader() {
        System.out.printf("| %-2s | %-10s | %-10s | %-30s | %-15s | %-15s | %-21s | %-21s |%n",
                "ID",
                "First name",
                "Last name",
                "Email",
                "Username",
                "Phone",
                "Date Added",
                "Date Modified"
        );
    }

    private void printTableContent(Customer entity) {
        System.out.printf("| %-2d | %-10s | %-10s | %-30s | %-15s | %-15s | %-21s | %-21s |%n",
                entity.id(),
                entity.firstName(),
                entity.lastName(),
                entity.email(),
                entity.username(),
                entity.phone(),
                entity.createdAt(),
                entity.updatedAt()
        );
    }

    private String checkOnEmpty(String string) {
        return string.isEmpty() ? null : string;
    }

    private void createCustomer(Scanner scanner) {
        Customer customer = new Customer();

        Utils.log("Enter first name");
        var firstName = scanner.nextLine();

        Utils.log("Enter last name");
        var lastName = scanner.nextLine();

        Utils.log("Enter email");
        var email = scanner.nextLine();

        Utils.log("Enter username");
        var username = scanner.nextLine();

        Utils.log("Enter Password");
        var password = Utils.hashPassword(scanner.nextLine());


        Utils.log("Enter phone");
        var phone = scanner.nextLine();

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setUsername(checkOnEmpty(username));
        customer.setPassword(password);
        customer.setPhone(checkOnEmpty(phone));

        customerRepository.add(customer);
    }

    private void findCustomer(Scanner scanner) {
        Customer customer = new Customer();

        Utils.log("Enter Category Id");
        int id = scanner.nextInt();
        scanner.nextLine();

        customer.setId((long) id);

        Customer result = customerRepository.get(customer.id());

        printTableHeader();
        printTableContent(result);
    }

    private void findAllCustomers() {
        List<Customer> customers = customerRepository.getAll();

        printTableHeader();
        customers.forEach(this::printTableContent);
    }

    private void updateCustomer(Scanner scanner) {
        Customer customer = new Customer();

        Utils.log("Enter id to select Customer you want to change");
        int id = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter first name");
        var firstName = scanner.nextLine();

        Utils.log("Enter last name");
        var lastName = scanner.nextLine();

        Utils.log("Enter email");
        var email = scanner.nextLine();

        Utils.log("Enter username");
        var username = scanner.nextLine();


        Utils.log("Enter Password");
        var password = Utils.hashPassword(scanner.nextLine());

        Utils.log("Enter phone");
        var phone = scanner.nextLine();

        customer.setId((long) id);
        customer.setFirstName(checkOnEmpty(firstName));
        customer.setLastName(checkOnEmpty(lastName));
        customer.setEmail(checkOnEmpty(email));
        customer.setUsername(checkOnEmpty(username));
        customer.setPhone(checkOnEmpty(phone));
        customer.setPassword(checkOnEmpty(password));

        customerRepository.update(customer);

        Customer result = customerRepository.get(customer.id());

        printTableHeader();
        printTableContent(result);
    }

    private void deleteCustomer(Scanner scanner) {
        Customer customer = new Customer();
        Utils.log("Enter id to select Customer you want to delete");
        int id = scanner.nextInt();

        customer.setId((long) id);
        scanner.nextLine();

        customerRepository.remove(customer);

        List<Customer> customers = customerRepository.getAll();

        printTableHeader();
        customers.forEach(this::printTableContent);
    }

    @Override
    public void start(Scanner scanner) {
        boolean cycle = true;
        do {
            Utils.log(options);
            switch (scanner.nextLine()) {
                case "1" -> createCustomer(scanner);
                case "2" -> findCustomer(scanner);
                case "3" -> findAllCustomers();
                case "4" -> updateCustomer(scanner);
                case "5" -> deleteCustomer(scanner);
                case "6" -> cycle = false;
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
