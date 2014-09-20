package pw.scho.battleship.core.persistence;

import java.util.List;

public interface Repository<T> {

    T get(Object id);

    void delete(T entity);

    void add(T entity);

    List<T> all();
}

