package ui;

import entities.Category;
import entities.Product;
import repository.product.ProductRepository;
import repository.product.ProductRepositoryImpl;
import utils.Utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ProductMenu implements Menu {
    private static final String options = """
            ===================
            1 - Create product
            2 - Find product
            3 - Find all products
            4 - Update product
            5 - Delete product
            6 - Show products that have been ordered
            7 - Back to main menu
            """;
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    private void printTableHeader() {
        System.out.printf("| %-2s | %-15s | %-40s | %-30s | %-10s | %-7s | %-21s | %-21s |%n", "ID", "Category", "Name", "Description", "Price", "Stock", "Date Added", "Date Modified");
    }

    private void printTableContent(Product entity) {
        System.out.printf("| %-2d | %-15s | %-40s | %-30s | %-10s | %-7s | %-21s | %-21s |%n", entity.id(), entity.category().name(), entity.name(), entity.description(), entity.price(), entity.stock(), entity.createdAt(), entity.updatedAt());
    }

    private String checkOnEmpty(String string) {
        return string.isEmpty() ? null : string;
    }


    private void createProduct(Scanner scanner) {
        Product product = new Product();

        Utils.log("Enter Category id");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Product name");
        var name = scanner.nextLine();

        Utils.log("Enter Product description");
        var description = scanner.nextLine();

        Utils.log("Enter Product price");
        double price = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Product stock");
        int stock = scanner.nextInt();
        scanner.nextLine();

        product.setCategory(new Category().setId((long) categoryId));
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);

        productRepository.add(product);

        List<Product> products = productRepository.getAll();

        printTableHeader();
        products.forEach(this::printTableContent);
    }

    private void findProduct(Scanner scanner) {
        Product product = new Product();

        Utils.log("Enter Product Id");
        int id = scanner.nextInt();
        scanner.nextLine();

        product.setId((long) id);

        Product result = productRepository.get(product.id());

        printTableHeader();
        printTableContent(result);
    }

    private void findAllProducts() {
        List<Product> products = productRepository.getAll();

        printTableHeader();
        products.forEach(this::printTableContent);
    }

    private void updateProduct(Scanner scanner) {
        Product product = new Product();

        Utils.log("Enter id to select Customer you want to change");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Category id");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Product name");
        var name = scanner.nextLine();

        Utils.log("Enter Product description");
        var description = scanner.nextLine();

        Utils.log("Enter Product price");
        double price = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Product stock");
        int stock = scanner.nextInt();
        scanner.nextLine();

        product.setId((long) productId);
        product.setCategory(new Category().setId((long) categoryId));
        product.setName(checkOnEmpty(name));
        product.setDescription(checkOnEmpty(description));
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);

        productRepository.update(product);

        List<Product> products = productRepository.getAll();

        printTableHeader();
        products.forEach(this::printTableContent);
    }

    private void deleteProduct(Scanner scanner) {
        Product product = new Product();

        Utils.log("Enter Product Id");
        int id = scanner.nextInt();
        scanner.nextLine();

        product.setId((long) id);

        productRepository.remove(product);

        List<Product> products = productRepository.getAll();

        printTableHeader();
        products.forEach(this::printTableContent);
    }

    private void showProductThatHaveBeenOrdered(Scanner scanner) {
        Utils.log("Enter Order id for existing Product");
        int id = scanner.nextInt();
        scanner.nextLine();

        Set<Product> result = productRepository.getProductsForOrder((long) id);

        result.forEach(product -> product.orders().forEach(order -> {
            System.out.printf("| %-2s | %-40s | %-21s | %-10s | %-11s |%n", "ID", "Name", "Order Date", "Status", "Total Price");
            System.out.printf("| %-2d | %-40s | %-21s | %-10s | %-11s |%n",
                    product.id(),
                    product.name(),
                    order.orderDate(),
                    order.status(),
                    order.total()
            );
        }));
    }

    @Override
    public void start(Scanner scanner) {
        boolean cycle = true;
        do {
            Utils.log(options);
            switch (scanner.nextLine()) {
                case "1" -> createProduct(scanner);
                case "2" -> findProduct(scanner);
                case "3" -> findAllProducts();
                case "4" -> updateProduct(scanner);
                case "5" -> deleteProduct(scanner);
                case "6" -> showProductThatHaveBeenOrdered(scanner);
                case "7" -> cycle = false;
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
