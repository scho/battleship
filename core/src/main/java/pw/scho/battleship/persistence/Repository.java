package pw.scho.battleship.persistence;

import java.util.List;

public interface Repository<T> {

    T get(Object id);

    void delete(T entity);

    void update(Object id, T entity);

    void insert(T entity);

    List<T> all();
}

