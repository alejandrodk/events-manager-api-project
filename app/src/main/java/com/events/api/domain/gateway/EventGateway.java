package com.events.api.domain.gateway;

import com.events.api.data.entity.EventsEntity;
import com.events.api.data.repository.EventsRepository;
import com.events.api.data.repository.PastEventsRepository;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.PastEvent;
import com.events.api.domain.utils.ModelMapperUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class EventGateway {
    private final EventsRepository eventsRepository;
    private final PastEventsRepository pastEventsRepository;

    public EventGateway(EventsRepository eventsRepository, PastEventsRepository pastEventsRepository) {
        this.eventsRepository = eventsRepository;
        this.pastEventsRepository = pastEventsRepository;
    }

    public Event save(Event event) {
        EventsEntity entity = ModelMapperUtils.mapToClass(event, EventsEntity.class);
        EventsEntity result = this.eventsRepository.save(entity);
        return toModel(result);
    }

    public Optional<Event> findById(int eventId) {
        return this.eventsRepository.findById(eventId).map(this::toModel);
    }

    public List<PastEvent> savePastEvents(List<PastEvent> events) {
        return this.pastEventsRepository.saveAll(events);
    }

    public List<PastEvent> getPastEvents() {
        return this.pastEventsRepository.findAll();
    }

    public List<Event> listByFilter(Predicate<EventsEntity> filter) {
        return this.eventsRepository.findAll().stream()
                .filter(filter).map(this::toModel).toList();
    }

    public List<Event> listBetweenDates(LocalDateTime from, LocalDateTime to) {
        return this.eventsRepository.findBetweenDates(from, to)
                .stream().map(this::toModel).toList();
    }

    public List<Event> listByRoom(int roomId) {
        return this.eventsRepository.findByRoom(roomId)
                .stream().map(this::toModel).toList();
    }

    private Event toModel(EventsEntity entity) {
        return Event.builder()
                .id(entity.getId())
                .name(entity.getName())
                .room(entity.getRoom())
                .price(entity.getPrice())
                .from(entity.getFrom())
                .to(entity.getTo())
                .build();
    }
}
