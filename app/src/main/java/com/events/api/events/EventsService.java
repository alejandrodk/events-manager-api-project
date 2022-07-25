package com.events.api.events;

import com.events.api.events.dtos.PastEventDTO;
import com.events.api.rooms.RoomsEntity;
import com.events.api.rooms.RoomsService;
import com.events.api.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class EventsService {
    private final EventsRepository repository;
    private final PastEventsRepository pastEventsRepository;
    private final RoomsService roomsService;
    private final EventsCache cache;

    public EventsService(EventsRepository repository, PastEventsRepository pastEventsRepository, RoomsService roomsService, EventsCache cache) {
        this.repository = repository;
        this.pastEventsRepository = pastEventsRepository;
        this.roomsService = roomsService;
        this.cache = cache;
    }

    public EventsEntity create(EventsEntity event) {
        RoomsEntity room = this.getEventRoom(event.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        List<EventsEntity> currentRoomEvents = this.listByRoom(event.getRoom());

        boolean available = this.roomsService.validateAvailability(event, room, currentRoomEvents);

        if (!available) throw new RuntimeException("Room is not available");
        EventsEntity result = this.repository.save(event);
        return this.cache.populate(result);
    }

    public List<PastEventDTO> savePastEvents(List<PastEventDTO> events) {
        return this.pastEventsRepository.saveAll(events);
    }

    public List<PastEventDTO> getPastEvents() {
        return this.pastEventsRepository.findAll();
    }

    public Optional<EventsEntity> get(int event) {
        EventsEntity cached = this.cache.get(event);
        if (cached != null) return Optional.of(cached);
        return this.repository.findById(event);
    }

    public List<EventsEntity> list(String date) {
        return this.repository.findAll().stream()
                .filter(this.getFilterByParam(date))
                .toList();
    }

    public List<EventsEntity> findBetweenDates(LocalDateTime from, LocalDateTime to) {
        return this.repository.findBetweenDates(from, to);
    }

    public List<EventsEntity> listByRoom(int room) {
        return this.repository.findByRoom(room);
    }
    public Optional<RoomsEntity> getEventRoom(int roomId) {
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
