package ru.otus.cachehw;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new CopyOnWriteArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        if (value != null) {
            notifyListeners(key, value, "remove");
        }
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            notifyListeners(key, value, "get");
        }
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (HwListener<K, V> listener : listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                logger.info("Listener failed to process notification: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
