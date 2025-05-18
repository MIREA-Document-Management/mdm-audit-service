package ru.mdm.audit.model.mapper;

import org.mapstruct.Mapper;
import ru.mdm.audit.controller.dto.EventDto;
import ru.mdm.audit.model.entity.Event;

@Mapper
public interface EventMapper {

    EventDto toDto(Event entity);

}
