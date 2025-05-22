package com.dyns.evento.registrations.services;

import com.dyns.evento.registrations.Registration;
import com.dyns.evento.registrations.dtos.RegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RegistrationMapper {
    RegistrationDto toDto(Registration entity);
}
