package repository.category;

import database.utils.JPQueryBuilder;
import database.utils.Query;
import entities.Category;

import java.util.List;
import java.util.function.Consumer;

public class CategoryRepositoryImpl implements CatergoryRepository {


    @Override
    public Category get(Long id) {
        return Query.inTransaction(em -> em.find(Category.class, id));
    }

    @Override
    public List<Category> getAll() {
        var query = new JPQueryBuilder("Category").selectFrom().execute();
        return Query.inTransaction(em -> em.createQuery(query, Category.class).getResultList());
    }

    @Override
    public void add(Category entity) {
        Query.inTransactionVoid(em -> em.persist(entity));
    }

    @Override
    public void update(Category entity) {
        Query.inTransactionVoid(em -> {
            Category existingEntity = em.find(Category.class, entity.id());
            if (existingEntity != null) {
                updateOfEntityValueNotNull(existingEntity::setName, entity.name());
                updateOfEntityValueNotNull(existingEntity::setDescription, entity.description());
            }
        });
    }

    @Override
    public void remove(Category entity) {
        Query.inTransactionVoid(em -> {
            Category mergedEntity = em.merge(entity);
            em.remove(mergedEntity);
        });
    }

    @Override
    public <T2> void updateOfEntityValueNotNull(Consumer<T2> consumer, T2 value) {
        if (value != null) consumer.accept(value);
    }
}
