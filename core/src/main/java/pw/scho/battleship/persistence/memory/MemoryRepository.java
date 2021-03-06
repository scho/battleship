package pw.scho.battleship.persistence.memory;

import pw.scho.battleship.persistence.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MemoryRepository<T> implements Repository<T> {


    private final InMemoryCache cache;

    private final Function<T, Object> primaryKeyFunction;
    private final Function<Object, Boolean> typeCheckFunction;

    public MemoryRepository(Function<T, Object> primaryKeyFunction, Function<Object, Boolean> typeCheckFunction) {
        cache = InMemoryCache.getInstance();
        this.primaryKeyFunction = primaryKeyFunction;
        this.typeCheckFunction = typeCheckFunction;
    }

    public T get(Object id) {
        return (T) cache.get(id);
    }

    public void delete(T entity) {
        cache.remove(primaryKeyFunction.apply(entity));
    }

    public void update(Object id, T entity) {
        cache.set(primaryKeyFunction.apply(entity), entity);
    }

    public void insert(T entity) {
        cache.set(primaryKeyFunction.apply(entity), entity);
    }

    public List<T> all() {
        List<T> entities = new ArrayList<>();

        for (Object object : cache.all()) {
            if (typeCheckFunction.apply(object)) {
                entities.add((T) object);
            }
        }

        return entities;
    }
}
