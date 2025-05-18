package ru.mdm.audit.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.audit.model.entity.Event;
import ru.mdm.audit.repository.EventRepository;
import ru.mdm.kafka.model.MdmKafkaHeaders;
import ru.mdm.kafka.model.MdmKafkaMessage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditKafkaListener {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Обработка полученных сообщений кафки.
     *
     * @param messages список сообщений кафки в пачке
     */
    @KafkaListener(topicPattern = "${spring.kafka.consumer.properties.topicPattern}", batch = "true",
            containerFactory = "auditKafkaListenerContainerFactory")
    public void receive(List<Message<MdmKafkaMessage<Object>>> messages, Acknowledgment acknowledgment) {
        Mono.just(messages)
                .doOnNext(messageList -> log.info("receive: Обработка пачки {} событий из kafka", messageList.size()))
                .flatMap(this::processMessages)
                .retry(5)
                .doOnError(throwable -> log.error("receive: Ошибка обработки пачки событий из kafka", throwable))
                .onErrorComplete()
                .doFinally(signalType -> {
                    acknowledgment.acknowledge();
                    log.info("receive: Завершена обработка пачки {} событий из kafka", messages.size());
                })
                .subscribe();
    }

    private Mono<Object> processMessages(List<Message<MdmKafkaMessage<Object>>> objects) {
        return Flux.fromIterable(objects)
                .map(object -> {
                    var payload = object.getPayload();
                    var headers = object.getHeaders();

                    var entity = new Event();

                    entity.setServiceName(Optional.ofNullable(headers.get(MdmKafkaHeaders.SERVICE_NAME))
                            .map(Object::toString)
                            .orElse("Неизвестный сервис"));
                    entity.setUserLogin(Optional.ofNullable(headers.get(MdmKafkaHeaders.SUBJECT))
                            .map(Object::toString)
                            .orElse("Неизвестный пользователь"));
                    entity.setDateTime(Optional.ofNullable(headers.get(MdmKafkaHeaders.DATE_TIME))
                            .map(Object::toString)
                            .map(this::getDateTime)
                            .orElse(LocalDateTime.now()));
                    entity.setEventType(payload.getEventType());
                    entity.setObject(objectMapper.convertValue(payload.getEventData(), new TypeReference<>() {}));

                    return entity;
                })
                .flatMap(eventRepository::save)
                .then(Mono.just(objects));
    }

    private LocalDateTime getDateTime(String input) {
        Instant instant = Instant.parse(input);

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
