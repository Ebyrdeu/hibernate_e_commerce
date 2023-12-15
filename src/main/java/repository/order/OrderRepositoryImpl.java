package repository.order;

import database.utils.JPQueryBuilder;
import database.utils.NativeQueryBuilder;
import database.utils.Query;
import entities.Order;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public Order get(Long id) {
        return Query.inTransaction(em -> em.find(Order.class, id));
    }

    @Override
    public Order getCustomerOrder(Long customerId, Long orderId) {
        var jpql = "SELECT o FROM Order o WHERE o.id = :orderId AND o.customer.id = :customerId";
        return Query.inTransaction(em -> {
            var cq = em.createQuery(jpql, Order.class);
            cq.setParameter("orderId", orderId);
            cq.setParameter("customerId", customerId);
            return cq.getSingleResult();
        });
    }


    @Override
    public List<Order> getAll() {
        var query = new JPQueryBuilder("Order").selectFrom().execute();
        return Query.inTransaction(em -> {
            var cq = em.createQuery(query, Order.class);
            return cq.getResultList();
        });
    }

    @Override
    public Set<Order> getOrdersForProduct(Long productId) {
        var jpql = new JPQueryBuilder("Order").selectFrom().join("products").where("productId").execute();
        return Query.inTransaction(em -> {
            var query = em.createQuery(jpql, Order.class);
            query.setParameter("productId", productId);
            return query.getResultStream().collect(Collectors.toSet());
        });
    }

    @Override
    public List<Order> getAllCustomerOrders(Long id) {
        var jpql = "SELECT o FROM Order o WHERE o.customer.id = :customerId";
        return Query.inTransaction(em -> {
            var cq = em.createQuery(jpql, Order.class);
            cq.setParameter("customerId", id);
            return cq.getResultList();
        });
    }

    @Override
    public void add(Order entity) {
        Query.inTransactionVoid(em -> em.persist(entity));
    }

    @Override
    public void addProductsToOrder(Long productId) {
        var nativeQuery = new NativeQueryBuilder("order_to_product").insert("product_id", "order_id").execute();
        System.out.println(nativeQuery);
        var lastIdQuery = new NativeQueryBuilder("product").select().function("LAST_INSERT_ID").execute();
        Query.inTransactionVoid(em -> {
            var cq = em.createNativeQuery(nativeQuery);
            var lastId = em.createNativeQuery(lastIdQuery);
            cq.setParameter(1, productId);
            cq.setParameter(2, lastId.getSingleResult());
            cq.executeUpdate();
        });
    }


    @Override
    public void update(Order entity) {
        Query.inTransactionVoid(em -> {
            var existingEntity = em.find(Order.class, entity.id());
            if (existingEntity != null) {
                existingEntity.setStatus(entity.status());
            }
        });
    }

    @Override
    public void remove(Order entity) {
        Query.inTransactionVoid(em -> {
            Order mergedEntity = em.merge(entity);
            em.remove(mergedEntity);
        });
    }

    @Override
    public <T> void updateOfEntityValueNotNull(Consumer<T> consumer, T value) {
    }
}
