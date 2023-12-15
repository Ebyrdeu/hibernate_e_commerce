package repository.product;

import database.utils.JPQueryBuilder;
import database.utils.NativeQueryBuilder;
import database.utils.Query;
import entities.Product;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Product get(Long id) {
        return Query.inTransaction(em -> em.find(Product.class, id));
    }

    @Override
    public List<Product> getAll() {
        var pql = new JPQueryBuilder("Product").selectFrom().execute();
        return Query.inTransaction(em -> {
            var cq = em.createQuery(pql, Product.class);
            return cq.getResultList();
        });
    }

    @Override
    public Set<Product> getProductsForOrder(Long orderId) {
        var jpql = new JPQueryBuilder("Product").selectFrom().join("orders").where("orderId").execute();
        return Query.inTransaction(em -> {
            var query = em.createQuery(jpql, Product.class);
            query.setParameter("orderId", orderId);
            return query.getResultStream().collect(Collectors.toSet());
        });
    }

    @Override
    public void add(Product entity) {
        Query.inTransactionVoid(em -> em.persist(entity));
    }

    @Override
    public void addProductsToOrder(Long productId, Long orderId) {
        var nativeQuery = new NativeQueryBuilder("order_to_product").insert("product_id", "order_id").execute();
        Query.inTransactionVoid(em -> {
            var cq = em.createNativeQuery(nativeQuery);
            cq.setParameter(1, productId);
            cq.setParameter(2, orderId);
            cq.executeUpdate();
        });
    }

    @Override
    public void update(Product entity) {
        Query.inTransactionVoid(em -> {
            Product existingEntity = em.find(Product.class, entity.id());
            if (existingEntity != null) {
                updateOfEntityValueNotNull(existingEntity::setName, entity.name());
                updateOfEntityValueNotNull(existingEntity::setDescription, entity.description());
                updateOfEntityValueNotNull(existingEntity::setPrice, entity.price());
                updateOfEntityValueNotNull(existingEntity::setStock, entity.stock());
            }
        });
    }

    @Override
    public void remove(Product entity) {
        Query.inTransactionVoid(em -> {
            Product mergedEntity = em.merge(entity);
            em.remove(mergedEntity);
        });
    }

    @Override
    public <T> void updateOfEntityValueNotNull(Consumer<T> consumer, T value) {
        if (value != null) consumer.accept(value);
    }
}
