package repository.category;

import entities.Category;
import repository.Repository;

import java.util.function.Consumer;

public interface CatergoryRepository extends Repository<Category> {

    @Override
    <T> void updateOfEntityValueNotNull(Consumer<T> consumer, T value);
}
