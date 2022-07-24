package com.events.api.events;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventsTasks {
    private final EventsService eventsService;
    private final EventsCache cache;

    public EventsTasks(EventsService eventsService, EventsCache cache) {
        this.eventsService = eventsService;
        this.cache = cache;
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void cachingTodayEvents() {
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime to = LocalDateTime.now().withHour(23).withMinute(59);
        List<EventsEntity> events = this.eventsService.findBetweenDates(from, to);

        events.forEach(this.cache::populate);
    }
}