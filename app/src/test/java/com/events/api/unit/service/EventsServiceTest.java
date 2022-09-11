package com.events.api.unit.service;

import com.events.api.data.cache.EventsCache;
import com.events.api.data.entity.EventsEntity;
import com.events.api.domain.gateway.EventGateway;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.PastEvent;
import com.events.api.domain.model.Room;
import com.events.api.domain.service.EventsService;
import com.events.api.domain.service.RoomsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventsServiceTest {
    @Mock
    EventGateway eventGateway;

    @Mock
    EventsCache eventsCache;

    @Mock
    RoomsService roomsService;

    @InjectMocks
    EventsService eventsService;

    @Test
    public void notCreateEventIfRoomDoesNotExist() {
        try {
            Event event = Event.builder().room(1).build();

            when(roomsService.get(event.getRoom())).thenReturn(Optional.empty());

            eventsService.create(event);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Room not found"));
        }
    }

    @Test
    public void notCreateEventIfRoomIsNotAvailable() {
        try {
            Room room = Room.builder().id(1).build();
            Event event = Event.builder().room(room.getId()).build();

            when(roomsService.get(event.getRoom())).thenReturn(Optional.of(room));
            when(eventGateway.listByRoom(room.getId())).thenReturn(Collections.emptyList());
            when(roomsService.validateAvailability(event, room, Collections.emptyList())).thenReturn(false);

            eventsService.create(event);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Room is not available"));
        }
    }

    @Test
    public void shouldCreateEvent() {
        Room room = Room.builder().id(1).build();
        Event event = Event.builder().room(room.getId()).build();

        when(roomsService.get(event.getRoom())).thenReturn(Optional.of(room));
        when(eventGateway.listByRoom(room.getId())).thenReturn(Collections.emptyList());
        when(roomsService.validateAvailability(event, room, Collections.emptyList())).thenReturn(true);
        when(eventGateway.save(event)).thenReturn(event);
        when(eventsCache.populate(event)).thenReturn(event);

        Event result = eventsService.create(event);

        assertThat(result, notNullValue());
        assertThat(result, equalToObject(event));
    }

    @Test
    public void shouldSaveEventInCacheAfterCreate() {
        Room room = Room.builder().id(1).build();
        Event event = Event.builder().room(room.getId()).build();

        when(roomsService.get(event.getRoom())).thenReturn(Optional.of(room));
        when(eventGateway.listByRoom(room.getId())).thenReturn(Collections.emptyList());
        when(roomsService.validateAvailability(event, room, Collections.emptyList())).thenReturn(true);
        when(eventGateway.save(event)).thenReturn(event);
        when(eventsCache.populate(event)).thenReturn(event);

        eventsService.create(event);

        verify(eventsCache).populate(argThat(newEvent -> {
            assertThat(newEvent, equalTo(event));
            return true;
        }));
    }

    @Test
    public void shouldSavePastEvents() {
        PastEvent event = PastEvent.builder().id(1).build();
        List<PastEvent> events = Collections.singletonList(event);

        when(eventGateway.savePastEvents(events)).thenReturn(events);

        List<PastEvent> result = eventsService.savePastEvents(events);

        assertThat(result, hasSize(events.size()));
        assertThat(result.get(0), equalTo(event));
    }

    @Test
    public void shouldReturnPastEventsFromDatabase() {
        PastEvent event = PastEvent.builder().id(1).build();
        List<PastEvent> events = Collections.singletonList(event);

        when(eventGateway.getPastEvents()).thenReturn(events);

        List<PastEvent> result = eventsService.getPastEvents();

        assertThat(result, hasSize(events.size()));
        assertThat(result.get(0), equalTo(event));
    }

    @Test
    public void shouldReturnCachedEvent() {
        int eventId = 1;
        Event event = Event.builder().id(eventId).build();

        when(eventsCache.get(eventId)).thenReturn(event);

        Optional<Event> result = eventsService.get(eventId);

        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalToObject(event));
    }

    @Test
    public void shouldReturnEventFromDatabase() {
        int eventId = 1;
        Event event = Event.builder().id(eventId).build();

        when(eventsCache.get(eventId)).thenReturn(null);
        when(eventGateway.findById(eventId)).thenReturn(Optional.of(event));

        Optional<Event> result = eventsService.get(eventId);

        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalToObject(event));
    }

    @Test
    public void shouldReturnEventsByRoom() {
        int eventId = 1;
        int roomId = 1;
        Event event = Event.builder().id(eventId).room(roomId).build();

        when(eventGateway.listByRoom(event.getRoom())).thenReturn(Collections.singletonList(event));

        List<Event> result = eventsService.listByRoom(roomId);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalToObject(event));
    }

    @Test
    public void shouldThrowErrorOnInvalidDate() {
        try {
            String invalidDate = "0000000000";

            eventsService.list(invalidDate);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("invalid date value"));
        }
    }
}
