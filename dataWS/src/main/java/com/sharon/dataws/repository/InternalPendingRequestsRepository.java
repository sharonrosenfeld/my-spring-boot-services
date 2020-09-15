package com.sharon.dataws.repository;

import java.util.HashMap;
import java.util.Map;

public class InternalPendingRequestsRepository implements PendingRequestsRepository{

    //TODO redis shared cache
    Map<String, Long> cache = new HashMap<>();

    @Override
    public void put(String key, Long ts) {
        cache.put(key,ts);
    }

    @Override
    public Long get(String key) {
        return cache.get(key);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }
}
