package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.dto.ServiceDepartementDTO;
import com.helloIftekhar.springJwt.dto.ServiceDepartementMapper;
import com.helloIftekhar.springJwt.model.Departement;
import com.helloIftekhar.springJwt.model.ServiceDepartement;
import com.helloIftekhar.springJwt.repository.DepartementRepository;
import com.helloIftekhar.springJwt.repository.ServiceDepartementRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceDepartementService {

    private final ServiceDepartementRepository serviceRepository;
    private final DepartementRepository departementRepository;
    private final UserRepository userRepository;

    public ServiceDepartementService(ServiceDepartementRepository serviceRepository,
                                     DepartementRepository departementRepository,UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.departementRepository = departementRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ServiceDepartementDTO createService(ServiceDepartementDTO dto) {
        Departement departement = departementRepository.findById(dto.getDepartementId())
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));

        if (serviceRepository.existsByNomServiceAndDepartementId(dto.getNomService(), dto.getDepartementId())) {
            throw new RuntimeException("Ce service existe déjà dans ce département");
        }

        ServiceDepartement service = ServiceDepartementMapper.toEntity(dto, departement);
        service = serviceRepository.save(service);
        return ServiceDepartementMapper.toDto(service);
    }

    public List<ServiceDepartementDTO> getAllServicesByDepartement(Long departementId) {
        return serviceRepository.findByDepartementId(departementId)
                .stream()
                .map(service -> {
                    ServiceDepartementDTO dto = ServiceDepartementMapper.toDto(service);
                    // Compter le nombre d'utilisateurs pour ce service
                    Long count = userRepository.countByServiceId(service.getId());
                    dto.setNombreUtilisateurs(count);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ServiceDepartementDTO> getServicesByDepartement(Long departementId) {
        return serviceRepository.findByDepartementId(departementId)
                .stream()
                .map(ServiceDepartementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceDepartementDTO updateService(Long serviceId, ServiceDepartementDTO dto) {
        // 1. Vérifier que le service existe
        ServiceDepartement existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service non trouvé avec l'ID: " + serviceId));

        // 2. Vérifier que le nouveau département existe
        Departement newDepartement = departementRepository.findById(dto.getDepartementId())
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé avec l'ID: " + dto.getDepartementId()));

        // 3. Vérifier si le nouveau nom existe déjà dans le même département
        if (!existingService.getNomService().equals(dto.getNomService()) &&
                serviceRepository.existsByNomServiceAndDepartementId(dto.getNomService(), dto.getDepartementId())) {
            throw new IllegalStateException("Ce nom de service existe déjà dans ce département");
        }

        // 4. Mettre à jour l'entité
        existingService.setNomService(dto.getNomService());
        existingService.setDepartement(newDepartement);

        // 5. Sauvegarder et retourner le DTO
        ServiceDepartement updatedService = serviceRepository.save(existingService);
        return ServiceDepartementMapper.toDto(updatedService);
    }




    @Transactional
    public void deleteService(Long serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException("Service non trouvé");
        }
        serviceRepository.deleteById(serviceId);
    }

    public List<ServiceDepartementDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceDepartementMapper::toDto)
                .collect(Collectors.toList());
    }

}

