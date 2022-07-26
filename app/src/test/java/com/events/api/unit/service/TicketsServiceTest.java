package com.events.api.unit.service;

import com.events.api.domain.gateway.TicketGateway;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.Room;
import com.events.api.domain.model.Ticket;
import com.events.api.domain.model.User;
import com.events.api.domain.service.EventsService;
import com.events.api.domain.service.TicketsService;
import com.events.api.domain.service.UsersService;
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
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketsServiceTest {
    @Mock
    TicketGateway ticketGateway;

    @Mock
    EventsService eventsService;

    @Mock
    UsersService usersService;

    @InjectMocks
    TicketsService ticketsService;

    @Test
    public void shouldCreateTicket() {
        Ticket ticket = Ticket.builder().id(1).eventId(10).build();

        when(ticketGateway.save(ticket)).thenReturn(ticket);

        Ticket result = ticketsService.create(ticket);

        assertThat(result, equalToObject(ticket));
    }

    @Test
    public void shouldReturnTicketsByEvent() {
        int eventId = 1;
        Ticket ticket = Ticket.builder().id(1).eventId(10).build();

        when(ticketGateway.findByEvent(eventId))
                .thenReturn(Collections.singletonList(ticket));

        List<Ticket> result = ticketsService.findByEvent(eventId);

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo(ticket)));
    }

    @Test
    public void shouldCancelTicketWithSuccess() {
        Ticket ticket = Ticket.builder().id(1).eventId(10).build();

        when(ticketGateway.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        ticketsService.cancel(ticket.getId());

        verify(ticketGateway).save(argThat(updatedTicket -> {
            assertThat(updatedTicket.getCancelled(), equalTo(true));
            return true;
        }));
    }

    @Test
    public void shouldThrowExceptionIfTicketNotFound() {
        try {
            int ticketId = 1;

            when(ticketGateway.findById(ticketId)).thenReturn(Optional.empty());

            ticketsService.cancel(ticketId);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Ticket not found"));
        }
    }

    @Test
    public void shouldNotValidateAvailabilityIfCantFindEvent() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.empty());

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Event not found"));
        }
    }

    @Test
    public void shouldNotValidateAvailabilityIfCantFindRoom() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();
            Event event = Event.builder().room(1000).build();

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.of(event));
            when(eventsService.getEventRoom(event.getRoom())).thenReturn(Optional.empty());

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Room not found"));
        }
    }

    @Test
    public void shouldNotValidateAvailabilityIfCantFindUser() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();
            Event event = Event.builder().room(1000).build();
            Room room = Room.builder().build();

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.of(event));
            when(eventsService.getEventRoom(event.getRoom())).thenReturn(Optional.of(room));
            when(usersService.get(ticket.getClient())).thenReturn(Optional.empty());

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("User not found"));
        }
    }

    @Test
    public void shouldNotValidateAvailabilityIfRoomIsNotAvailable() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();
            Event event = Event.builder().id(1).room(1000).build();
            Room room = Room.builder().availability(10).build();
            User user = User.builder().id(1).build();

            long purchasedTickets = room.getAvailability();

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.of(event));
            when(eventsService.getEventRoom(event.getRoom())).thenReturn(Optional.of(room));
            when(usersService.get(ticket.getClient())).thenReturn(Optional.of(user));
            when(ticketGateway.countByEvent(event.getId())).thenReturn(purchasedTickets);

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("room not available"));
        }
    }

    @Test
    public void shouldNotValidateAvailabilityIfUserAlreadyHasTickets() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();
            Event event = Event.builder().id(1).room(1000).build();
            Room room = Room.builder().availability(10).build();
            User user = User.builder().id(1).build();

            long purchasedTickets = 1;
            long currentUserTickets = 1;

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.of(event));
            when(eventsService.getEventRoom(event.getRoom())).thenReturn(Optional.of(room));
            when(usersService.get(ticket.getClient())).thenReturn(Optional.of(user));
            when(ticketGateway.countByEvent(event.getId())).thenReturn(purchasedTickets);
            when(ticketGateway.countByClient(user.getId())).thenReturn(currentUserTickets);

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("User already has a ticket"));
        }
    }

    @Test
    public void shouldNotValidateAvailabilityIfEventEnds() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();
            Event event = Event.builder().id(1).room(1000).to(LocalDateTime.now().minusDays(5)).build();
            Room room = Room.builder().availability(10).build();
            User user = User.builder().id(1).build();

            long purchasedTickets = 1;
            long currentUserTickets = 0;

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.of(event));
            when(eventsService.getEventRoom(event.getRoom())).thenReturn(Optional.of(room));
            when(usersService.get(ticket.getClient())).thenReturn(Optional.of(user));
            when(ticketGateway.countByEvent(event.getId())).thenReturn(purchasedTickets);
            when(ticketGateway.countByClient(user.getId())).thenReturn(currentUserTickets);

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("Event already finished"));
        }
    }

    @Test
    public void shouldValidateAvailability() {
        try {
            Ticket ticket = Ticket.builder().id(1).eventId(10).client(100).build();
            Event event = Event.builder().id(1).room(1000).to(LocalDateTime.now().plusDays(5)).build();
            Room room = Room.builder().availability(10).build();
            User user = User.builder().id(1).build();

            long purchasedTickets = 1;
            long currentUserTickets = 0;

            when(eventsService.get(ticket.getEventId())).thenReturn(Optional.of(event));
            when(eventsService.getEventRoom(event.getRoom())).thenReturn(Optional.of(room));
            when(usersService.get(ticket.getClient())).thenReturn(Optional.of(user));
            when(ticketGateway.countByEvent(event.getId())).thenReturn(purchasedTickets);
            when(ticketGateway.countByClient(user.getId())).thenReturn(currentUserTickets);

            ticketsService.validateAvailability(ticket);
        } catch (Exception e) {
            assertThat(e, nullValue());
        }
    }
}
