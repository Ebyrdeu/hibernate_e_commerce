package repository.order;

import entities.Order;
import repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface OrderRepository extends Repository<Order> {
    Order getCustomerOrder(Long customerId, Long orderI);

    List<Order> getAllCustomerOrders(Long id);

    Set<Order> getOrdersForProduct(Long productId);

    void addProductsToOrder(Long productId);

    @Override
    <T> void updateOfEntityValueNotNull(Consumer<T> consumer, T value);
}
