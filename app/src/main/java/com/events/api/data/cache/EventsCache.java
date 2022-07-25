package com.events.api.data.cache;

import com.events.api.domain.model.Event;
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
    public Event get(int id) {
        return null;
    }

    @CachePut(cacheNames = cacheCollection, key = "#dto.id")
    public Event populate(Event dto) {
        return dto;
    }
}
