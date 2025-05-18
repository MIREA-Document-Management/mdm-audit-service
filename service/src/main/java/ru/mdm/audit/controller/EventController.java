package ru.mdm.audit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.mdm.audit.controller.dto.EventDto;
import ru.mdm.audit.service.EventServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventServiceImpl eventService;

    @GetMapping
    public Flux<EventDto> getEvents(Pageable pageable) {
        return eventService.getEvents(pageable);
    }
}
