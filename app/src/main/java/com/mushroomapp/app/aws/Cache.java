package com.mushroomapp.app.aws;

import java.time.Instant;
import java.util.*;

public class Cache<K, V> {

    private Map<K, V> values = new LinkedHashMap<>();
    private Map<K, Date> expirations = new LinkedHashMap<>();

    public Optional<V> get(K key) {
        Optional<Date> expiry = Optional.ofNullable(this.expirations.get(key));

        if(expiry.isEmpty()) {
            System.out.println("Cache miss " + key);
            return Optional.empty();
        }
        if(expiry.get().compareTo(Date.from(Instant.now())) < 0) {
            System.out.println("Expired object " + key);
            return Optional.empty();
        }

        Optional<V> value = Optional.ofNullable(this.values.get(key));

        if(value.isPresent()) System.out.println("Cache hit: " + key);

        return value;
    }
    public void add(K key, V value, Date expiration) {
        this.values.put(key, value);
        this.expirations.put(key, expiration);
    }
}
