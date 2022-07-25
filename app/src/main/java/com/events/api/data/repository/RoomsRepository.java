package com.events.api.data.repository;

import com.events.api.data.entity.RoomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomsRepository extends JpaRepository<RoomsEntity, Integer> {}
