package com.events.api.data.repository;

import com.events.api.domain.model.PastEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastEventsRepository extends MongoRepository<PastEvent, String> {}
