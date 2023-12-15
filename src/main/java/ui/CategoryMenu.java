package ui;

import entities.Category;
import repository.category.CategoryRepositoryImpl;
import repository.category.CatergoryRepository;
import utils.Utils;

import java.util.List;
import java.util.Scanner;

public class CategoryMenu implements Menu {
    private static final String options = """
            ===================
            1 - Create category
            2 - Find category
            3 - Find all categories
            4 - Update category
            5 - Delete category
            6 - Back to main menu
            """;
    private final CatergoryRepository categoryRepository = new CategoryRepositoryImpl();

    private void printTableHeader() {
        System.out.printf("| %-5s | %-10s | %-15s | %-25s | %-25s | %n", "ID", "Category", "Description", "Date Added", "Date Modified");
    }

    private void printTableContent(Category entity) {
        System.out.printf("%-5d | %-10s | %-15s | %-25s | %-25s | %n", entity.id(), entity.name(), entity.description(), entity.createdAt(), entity.updatedAt());
    }

    private String checkOnEmpty(String string) {
        return string.isEmpty() ? null : string;
    }

    private void createCategory(Scanner scanner) {
        Category category = new Category();

        Utils.log("Enter Category Name");
        var name = scanner.nextLine();

        Utils.log("Enter Category Description");
        var description = scanner.nextLine();

        category.setName(name);
        category.setDescription(checkOnEmpty(description));

        categoryRepository.add(category);
    }


    private void findCategory(Scanner scanner) {
        Category category = new Category();

        Utils.log("Enter Category Id");
        int id = scanner.nextInt();

        category.setId((long) id);

        Category result = categoryRepository.get(category.id());

        printTableHeader();
        printTableContent(result);
    }

    private void findAllCategory() {
        List<Category> categories = categoryRepository.getAll();

        printTableHeader();
        categories.forEach(this::printTableContent);
    }

    private void updateCategory(Scanner scanner) {
        Category category = new Category();

        Utils.log("Enter id to select Category you want to change");
        int id = scanner.nextInt();
        scanner.nextLine();

        Utils.log("Enter Category Name");
        var name = scanner.nextLine();

        Utils.log("Enter Category Description");
        var description = scanner.nextLine();

        category.setId((long) id);
        category.setName(checkOnEmpty(name));
        category.setDescription(checkOnEmpty(description));

        categoryRepository.update(category);

        var result = categoryRepository.get(category.id());

        printTableHeader();
        printTableContent(result);
    }

    private void deleteCategory(Scanner scanner) {
        Category category = new Category();

        Utils.log("Enter id to select Category you want to delete");
        int id = scanner.nextInt();

        category.setId((long) id);
        scanner.nextLine();

        categoryRepository.remove(category);

        List<Category> categories = categoryRepository.getAll();

        printTableHeader();
        categories.forEach(this::printTableContent);
    }

    @Override
    public void start(Scanner scanner) {
        boolean cycle = true;
        do {
            Utils.log(options);
            switch (scanner.nextLine()) {
                case "1" -> createCategory(scanner);
                case "2" -> findCategory(scanner);
                case "3" -> findAllCategory();
                case "4" -> updateCategory(scanner);
                case "5" -> deleteCategory(scanner);
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
