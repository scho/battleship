package pw.scho.battleship.persistence.mongo;

import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Restriction;
import pw.scho.battleship.persistence.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class MongoRepository<T> implements Repository<T> {

    protected final MongoSession session;

    protected MongoRepository(MongoSession session) {
        this.session = session;
    }

    @Override
    public T get(Object id) {
        return session.get(id, persistentType());
    }

    @Override
    public void delete(T entity) {
        session.delete(entity);
    }

    @Override
    public void add(T entity) {
        session.save(entity);
    }

    @Override
    public List<T> all() {
        return session.getAll(persistentType());
    }

    public List<T> findByRestriction(Restriction restriction) {
        Criteria<T> criteria = session.createCriteria(persistentType());

        criteria.add(restriction);

        return criteria.list();
    }

    public MongoSession getSession(){
        return session;
    }

    protected final Class<T> persistentType() {
        final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }
}