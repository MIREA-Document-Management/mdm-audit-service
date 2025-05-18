package ru.mdm.audit.model.entity;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность события.
 */
@Data
@Table("mdm_audit_events")
public class Event {

    /**
     * Идентификатор класса.
     */
    @Id
    @Column("id")
    private UUID id;

    /**
     * Наименование события.
     */
    @Column("event_type")
    private String eventType;

    /**
     * Логин пользователя.
     */
    @Column("user_login")
    private String userLogin;

    /**
     * Наименование сервиса.
     */
    @Column("service_name")
    private String serviceName;

    /**
     * Дата и время создания.
     */
    @CreatedDate
    @Column("date_time")
    private LocalDateTime dateTime;

    /**
     * Вложение в событие.
     */
    @Column("object")
    private Map<String, Object> object;
}
