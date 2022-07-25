package com.events.api.data.repository;

import com.events.api.web.dto.PastEventDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastEventsRepository extends MongoRepository<PastEventDTO, String> {}
