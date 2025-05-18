package ru.mdm.audit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.mdm.audit.controller.dto.EventDto;
import ru.mdm.audit.model.mapper.EventMapper;
import ru.mdm.audit.repository.EventRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl {

    private final EventMapper mapper;
    private final EventRepository eventRepository;

    public Flux<EventDto> getEvents(Pageable pageable) {
        return eventRepository.findAllBy(pageable)
                .map(mapper::toDto);
    }
}
