package repository.customer;

import database.utils.JPQueryBuilder;
import database.utils.Query;
import entities.Customer;

import java.util.List;
import java.util.function.Consumer;

public class CustomerRepositoryImpl implements CustomerRepository {
    @Override
    public Customer get(Long id) {
        return Query.inTransaction(em -> em.find(Customer.class, id));
    }

    @Override
    public List<Customer> getAll() {
        var query = new JPQueryBuilder("Customer").selectFrom().execute();
        return Query.inTransaction(em -> em.createQuery(query, Customer.class).getResultList());
    }

    @Override
    public List<Customer> getAllWhereFullName(Customer entity) {
        var query = new JPQueryBuilder("Customer")
                .selectFrom()
                .where("firstName")
                .execute();
        return Query.inTransaction(em -> {
            var cq = em.createQuery(query, Customer.class);
            cq.setParameter("firstName", entity.firstName());
            return cq.getResultList();
        });
    }

    @Override
    public void add(Customer entity) {
        Query.inTransactionVoid(em -> em.persist(entity));
    }

    @Override
    public void update(Customer entity) {
        Query.inTransactionVoid(em -> {
            Customer existingEntity = em.find(Customer.class, entity.id());
            if (existingEntity != null) {
                updateOfEntityValueNotNull(existingEntity::setFirstName, entity.firstName());
                updateOfEntityValueNotNull(existingEntity::setLastName, entity.lastName());
                updateOfEntityValueNotNull(existingEntity::setEmail, entity.email());
                updateOfEntityValueNotNull(existingEntity::setPhone, entity.phone());
                updateOfEntityValueNotNull(existingEntity::setUsername, entity.username());
                updateOfEntityValueNotNull(existingEntity::setPassword, entity.password());
            }
        });
    }

    @Override
    public void remove(Customer entity) {
        Query.inTransactionVoid(em -> {
            Customer mergedEntity = em.merge(entity);
            em.remove(mergedEntity);
        });
    }

    @Override
    public <T2> void updateOfEntityValueNotNull(Consumer<T2> consumer, T2 value) {
        if (value != null) consumer.accept(value);
    }
}
