package com.events.api.domain.gateway;

import com.events.api.data.entity.RoomsEntity;
import com.events.api.data.repository.RoomsRepository;
import com.events.api.domain.model.Room;
import com.events.api.domain.utils.ModelMapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomGateway {
    private final RoomsRepository roomsRepository;

    public RoomGateway(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    public Room save(Room room) {
        RoomsEntity entity = ModelMapperUtils.mapToClass(room, RoomsEntity.class);
        RoomsEntity result = this.roomsRepository.save(entity);
        return toModel(result);
    }

    public List<Room> findAll() {
        return this.roomsRepository.findAll().stream()
                .map(this::toModel).toList();
    }

    public Optional<Room> findById(int roomId) {
        return this.roomsRepository.findById(roomId).map(this::toModel);
    }

    public boolean exist(int id) {
        return this.roomsRepository.existsById(id);
    }

    private Room toModel(RoomsEntity entity) {
        return Room.builder()
                .id(entity.getId())
                .name(entity.getName())
                .availability(entity.getAvailability())
                .open(entity.getOpen())
                .close(entity.getClose())
                .build();
    }
}
