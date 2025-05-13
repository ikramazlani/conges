package com.helloIftekhar.springJwt.dto;

import com.helloIftekhar.springJwt.model.Departement;
import com.helloIftekhar.springJwt.model.ServiceDepartement;

public class ServiceDepartementMapper {

    public static ServiceDepartementDTO toDto(ServiceDepartement service) {
        ServiceDepartementDTO dto = new ServiceDepartementDTO();
        dto.setId(service.getId());
        dto.setNomService(service.getNomService());
        dto.setDepartementId(service.getDepartement().getId());
        dto.setNomDepartement(service.getDepartement().getNomDepartement());
        return dto;
    }

    public static ServiceDepartement toEntity(ServiceDepartementDTO dto, Departement departement) {
        ServiceDepartement service = new ServiceDepartement();
        service.setNomService(dto.getNomService());
        service.setDepartement(departement);
        return service;
    }
}