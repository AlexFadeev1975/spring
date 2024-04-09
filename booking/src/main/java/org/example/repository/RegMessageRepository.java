package org.example.repository;

import org.example.model.kafka.RegMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegMessageRepository extends ReactiveMongoRepository<RegMessage, String> {
}
