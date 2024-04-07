package org.example.repository;

import org.example.model.kafka.BookingMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingMessageRepository extends ReactiveMongoRepository<BookingMessage, String> {
}
