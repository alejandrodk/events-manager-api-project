package com.events.api.domain.gateway;

import com.events.api.data.entity.TicketsEntity;
import com.events.api.data.repository.TicketsRepository;
import com.events.api.domain.model.Ticket;
import com.events.api.domain.utils.ModelMapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TicketGateway {
    private final TicketsRepository ticketsRepository;

    public TicketGateway(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    public Ticket save(Ticket ticket) {
        TicketsEntity entity = ModelMapperUtils.mapToClass(ticket, TicketsEntity.class);
        TicketsEntity result = this.ticketsRepository.save(entity);
        return toModel(result);
    }

    public Optional<Ticket> findById(int ticketId) {
        return this.ticketsRepository.findById(ticketId).map(this::toModel);
    }

    public List<Ticket> findByEvent(int eventId) {
        return this.ticketsRepository.findByEventId(eventId)
                .stream().map(this::toModel).toList();
    }

    public long countByEvent(int eventId) {
        return this.ticketsRepository.countByEventIdAndCancelledFalse(eventId);
    }

    public long countByClient(int clientId) {
        return this.ticketsRepository.countByClient(clientId);
    }

    private Ticket toModel(TicketsEntity entity) {
        return Ticket.builder()
                .id(entity.getId())
                .client(entity.getClient())
                .eventId(entity.getEventId())
                .cancelled(entity.getCancelled())
                .build();
    }
}
