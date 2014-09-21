package pw.scho.battleship.persistence;

import org.mongolink.domain.criteria.Restriction;

import java.util.List;

public interface Repository<T> {

    T get(Object id);

    void delete(T entity);

    void add(T entity);

    List<T> all();

    List<T> findByRestriction(Restriction restriction);
}

