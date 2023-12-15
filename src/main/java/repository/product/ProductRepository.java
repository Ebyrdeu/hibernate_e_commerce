package repository.product;

import entities.Product;
import repository.Repository;

import java.util.Set;
import java.util.function.Consumer;

public interface ProductRepository extends Repository<Product> {

    Set<Product> getProductsForOrder(Long orderId);

    void addProductsToOrder(Long productId, Long orderId);

    @Override
    <T> void updateOfEntityValueNotNull(Consumer<T> consumer, T value);

}
