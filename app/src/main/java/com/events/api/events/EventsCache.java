package com.events.api.events;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EventsCache {
    private final String cacheCollection = "events";
    private final CacheManager cacheManager;

    public EventsCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = cacheCollection, key = "#id")
    public EventsEntity get(int id) {
        return null;
    }

    @CachePut(cacheNames = cacheCollection, key = "#dto.id")
    public EventsEntity populate(EventsEntity dto) {
        return dto;
    }
}
