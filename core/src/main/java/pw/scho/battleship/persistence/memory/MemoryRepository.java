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

    @Override
    public T get(Object id) {
        return (T) cache.get(id);
    }

    @Override
    public void delete(T entity) {
        cache.remove(primaryKeyFunction.apply(entity));
    }

    @Override
    public void add(T entity) {
        cache.set(primaryKeyFunction.apply(entity), entity);
    }

    @Override
    public List<T> all() {
        List<T> entities = new ArrayList();

        for (Object object : cache.all()) {
            if (typeCheckFunction.apply(object)) {
                entities.add((T) object);
            }
        }

        return entities;
    }
}
