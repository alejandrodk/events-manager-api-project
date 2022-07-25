package com.events.api.events;

import com.events.api.events.dtos.PastEventDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastEventsRepository extends MongoRepository<PastEventDTO, String> {}
