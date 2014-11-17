package pw.scho.battleship.persistence.mongo;

import org.jongo.MongoCollection;
import pw.scho.battleship.persistence.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MongoRepository<T> implements Repository<T> {

    protected final MongoCollection collection;

    protected MongoRepository(MongoCollection collection) {
        this.collection = collection;
    }

    public T get(Object id) {
        return collection.findOne("{ _id: '" + id.toString() + "' }").as(persistentType());
    }

    public void delete(Object id) {
        collection.remove("{ _id: '" + id.toString() + "' }");
    }

    public void insert(T entity) {
        collection.insert(entity);
    }

    public void update(Object id, T entity) {
        collection.update("{ _id: '" + id.toString() + "'}").with(entity);
    }

    public List<T> all() {
        Iterator<T> i = collection.find().as(persistentType());
        List<T> result = new ArrayList<>();
        while (i.hasNext()) {
            result.add(result.size(), i.next());
        }
        return result;
    }

    public List<T> findByRestriction(String restriction) {
        Iterator<T> i = collection.find(restriction).as(persistentType());
        List<T> result = new ArrayList<>();
        while (i.hasNext()) {
            result.add(result.size(), i.next());
        }
        return result;
    }

    protected final Class<T> persistentType() {
        final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }
}