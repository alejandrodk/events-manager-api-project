package com.events.api.data.cache;

import com.events.api.domain.model.Room;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RoomsCache {
    private final String cacheCollection = "rooms";
    private final CacheManager cacheManager;

    public RoomsCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = cacheCollection, key = "#id")
    public Room get(int id) {
        return null;
    }

    @CachePut(cacheNames = cacheCollection, key = "#dto.id")
    public Room populate(Room dto) {
        return dto;
    }
}
