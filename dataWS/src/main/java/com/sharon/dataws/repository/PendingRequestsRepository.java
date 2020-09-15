package com.sharon.dataws.repository;

public interface PendingRequestsRepository {
    void put(String key, Long ts);
    Long get(String key);
    void remove(String key);
}
