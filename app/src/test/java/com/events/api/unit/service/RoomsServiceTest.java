package com.events.api.unit.service;

import com.events.api.data.cache.RoomsCache;
import com.events.api.domain.gateway.RoomGateway;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.Room;
import com.events.api.domain.service.RoomsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoomsServiceTest {
    @Mock
    RoomGateway roomGateway;

    @Mock
    RoomsCache roomsCache;

    @InjectMocks
    RoomsService roomsService;

    @Test
    public void shouldReturnCachedRoom() {
        int roomId = 1;
        Room room = Room.builder().id(roomId).name("foo").build();

        when(roomsCache.get(roomId)).thenReturn(room);

        Optional<Room> result = roomsService.get(roomId);

        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalToObject(room));
    }

    @Test
    public void shouldReturnRoomFromDatabase() {
        int roomId = 1;
        Room room = Room.builder().id(roomId).name("foo").build();

        when(roomsCache.get(roomId)).thenReturn(null);
        when(roomsService.get(roomId)).thenReturn(Optional.of(room));

        Optional<Room> result = roomsService.get(roomId);

        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalToObject(room));
    }

    @Test
    public void shouldCreateRoom(){
        int roomId = 1;
        Room room = Room.builder().id(roomId).name("foo").build();

        when(roomGateway.save(room)).thenReturn(room);
        when(roomsCache.populate(room)).thenReturn(room);

        Room result = roomsService.create(room);

        assertThat(result, notNullValue());
        assertThat(result, equalToObject(room));
    }

    @Test
    public void shouldSaveRoomInCacheAfterCreate(){
        int roomId = 1;
        Room room = Room.builder().id(roomId).name("foo").build();

        when(roomGateway.save(room)).thenReturn(room);
        when(roomsCache.populate(room)).thenReturn(room);

        roomsService.create(room);

        verify(roomsCache).populate(argThat(newRoom -> {
            assertThat(newRoom, equalTo(room));
            return true;
        }));
    }

    @Test
    public void shouldThrowExceptionIfRoomNotExistOnUpdate() {
        try {
            Room room = Room.builder().id(1).name("foo").build();

            when(roomGateway.exist(room.getId())).thenReturn(false);

            roomsService.update(room);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Room not found"));
        }
    }

    @Test
    public void shouldUpdateRoom() {
        Room room = Room.builder().id(1).name("foo").build();

        when(roomGateway.exist(room.getId())).thenReturn(true);
        when(roomGateway.save(room)).thenReturn(room);
        when(roomsCache.populate(room)).thenReturn(room);

        Room result = roomsService.update(room);

        assertThat(result, notNullValue());
        assertThat(result, equalToObject(room));
    }

    @Test
    public void shouldSaveUpdatedRoomInCache() {
        Room room = Room.builder().id(1).name("foo").build();

        when(roomGateway.exist(room.getId())).thenReturn(true);
        when(roomGateway.save(room)).thenReturn(room);
        when(roomsCache.populate(room)).thenReturn(room);

        roomsService.update(room);

        verify(roomsCache).populate(argThat(newRoom -> {
            assertThat(newRoom, equalTo(room));
            return true;
        }));
    }

    @Test
    public void shouldGetRoomsList() {
        Room room = Room.builder().id(1).name("foo").build();

        when(roomGateway.findAll()).thenReturn(Collections.singletonList(room));

        List<Room> result = roomsService.list();

        assertThat(result, hasSize(1));
    }

    @Test
    public void shouldNotValidateAvailabilityIfRoomAlreadyHasEvent() {
        LocalDateTime roomOpen = LocalDateTime.now();
        LocalDateTime roomClose = roomOpen.plusHours(2);
        LocalDateTime eventFrom = roomOpen.plusMinutes(30);
        LocalDateTime eventTo = eventFrom.plusHours(1);

        Room room = Room.builder().open(roomOpen).close(roomClose).build();
        Event event = Event.builder().from(eventFrom).to(eventTo).build();
        List<Event> roomEvents = Collections.singletonList(event);

        boolean result = roomsService.validateAvailability(event, room, roomEvents);

        assertThat(result, equalTo(false));
    }

    @Test
    public void shouldValidateAvailability() {
        LocalDateTime roomOpen = LocalDateTime.now();
        LocalDateTime roomClose = roomOpen.plusHours(4);
        LocalDateTime eventFrom = roomOpen.plusMinutes(1);
        LocalDateTime eventTo = eventFrom.plusHours(1);
        LocalDateTime nextEventFrom = eventTo.plusMinutes(1);
        LocalDateTime nextEventTo = nextEventFrom.plusHours(1);

        Room room = Room.builder().open(roomOpen).close(roomClose).build();
        Event event = Event.builder().from(eventFrom).to(eventTo).build();
        Event nextEvent = Event.builder().from(nextEventFrom).to(nextEventTo).build();
        List<Event> roomEvents = Collections.singletonList(event);

        boolean result = roomsService.validateAvailability(nextEvent, room, roomEvents);

        assertThat(result, equalTo(true));
    }
}
