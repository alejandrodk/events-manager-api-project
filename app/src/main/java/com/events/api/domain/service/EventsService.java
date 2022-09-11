package com.events.api.domain.service;

import com.events.api.data.cache.EventsCache;
import com.events.api.data.entity.EventsEntity;
import com.events.api.domain.gateway.EventGateway;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.PastEvent;
import com.events.api.domain.model.Room;
import com.events.api.domain.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class EventsService {
    private final EventGateway eventGateway;
    private final RoomsService roomsService;
    private final EventsCache cache;

    public EventsService(EventGateway eventGateway, RoomsService roomsService, EventsCache cache) {
        this.eventGateway = eventGateway;
        this.roomsService = roomsService;
        this.cache = cache;
    }

    public Event create(Event event) {
        Room room = this.getEventRoom(event.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        List<Event> currentRoomEvents = this.listByRoom(event.getRoom());

        boolean available = this.roomsService.validateAvailability(event, room, currentRoomEvents);

        if (!available) throw new RuntimeException("Room is not available");
        Event result = this.eventGateway.save(event);
        return this.cache.populate(result);
    }

    public List<PastEvent> savePastEvents(List<PastEvent> events) {
        return this.eventGateway.savePastEvents(events);
    }

    public List<PastEvent> getPastEvents() {
        return this.eventGateway.getPastEvents();
    }

    public Optional<Event> get(int event) {
        Event cached = this.cache.get(event);
        if (cached != null) return Optional.of(cached);
        return this.eventGateway.findById(event);
    }

    public List<Event> list(String date) {
        return this.eventGateway.listByFilter(this.getFilterByParam(date));
    }

    public List<Event> findBetweenDates(LocalDateTime from, LocalDateTime to) {
        return this.eventGateway.listBetweenDates(from, to);
    }

    public List<Event> listByRoom(int room) {
        return this.eventGateway.listByRoom(room);
    }
    public Optional<Room> getEventRoom(int roomId) {
        return this.roomsService.get(roomId);
    }

    private Predicate<EventsEntity> getFilterByParam(String date) {
        LocalDateTime now = LocalDateTime.now();

        if (date.length() > 8) throw new RuntimeException("invalid date value");

        if (date.length() == 8 && !date.equals("tomorrow") && Integer.parseInt(date) > 0) {
            LocalDateTime parsedDate = DateUtils.dateParamToTime(date);
            return event -> event.getFrom().getDayOfYear() == parsedDate.getDayOfYear();
        }

        return switch (date) {
            case "today" -> event -> event.getFrom().getDayOfYear() == now.getDayOfYear();
            case "tomorrow" -> event -> event.getFrom().getDayOfYear() == now.plusDays(1).getDayOfYear();
            default -> event -> event.getFrom().isAfter(now.withHour(0).withMinute(0).withSecond(1));
        };
    }
}
