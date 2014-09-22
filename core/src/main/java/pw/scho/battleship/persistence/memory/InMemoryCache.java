package pw.scho.battleship.persistence.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryCache {

    private final HashMap<Object, Object> hashMap = new HashMap();

    public static InMemoryCache getInstance() {
        return Singleton.INSTANCE.cache;
    }

    public Object get(Object id) {
        return hashMap.get(id);
    }

    public void set(Object id, Object value) {
        hashMap.put(id, value);
    }

    public void remove(Object id) {
        hashMap.remove(id);
    }

    public Collection<Object> all() {
        return new ArrayList(hashMap.values());
    }

    private enum Singleton {

        INSTANCE;

        private final InMemoryCache cache;

        private Singleton() {
            cache = new InMemoryCache();
        }
    }
}