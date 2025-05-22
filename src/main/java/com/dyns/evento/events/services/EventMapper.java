package com.dyns.evento.events.services;

import com.dyns.evento.events.Event;
import com.dyns.evento.events.dtos.EventDto;
import com.dyns.evento.events.dtos.EventPatchRequest;
import com.dyns.evento.events.dtos.EventPostRequest;
import com.dyns.evento.events.dtos.EventPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    EventDto toDto(Event entity);

    Event toEntity(EventPostRequest dto);

    Event toEntity(EventPatchRequest dto);

    Event toEntity(EventPutRequest dto);
}
