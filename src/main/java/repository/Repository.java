package repository;

import java.util.List;
import java.util.function.Consumer;

public interface Repository<T> {
    T get(Long id);

    List<T> getAll();

    void add(T entity);


    void update(T entity);

    void remove(T entity);

    <T2> void updateOfEntityValueNotNull(Consumer<T2> consumer, T2 value);
}
