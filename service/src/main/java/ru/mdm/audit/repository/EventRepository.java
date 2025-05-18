package ru.mdm.audit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.mdm.audit.model.entity.Event;

import java.util.UUID;

/**
 * Репозиторий для работы с событиями.
 */
@Repository
public interface EventRepository extends ReactiveSortingRepository<Event, UUID>, ReactiveCrudRepository<Event, UUID> {

    Flux<Event> findAllBy(Pageable pageable);

}
