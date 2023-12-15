package repository.customer;

import entities.Customer;
import repository.Repository;

import java.util.List;
import java.util.function.Consumer;

public interface CustomerRepository extends Repository<Customer> {
    List<Customer> getAllWhereFullName(Customer sort);

    @Override
    <T> void updateOfEntityValueNotNull(Consumer<T> consumer, T value);
}
