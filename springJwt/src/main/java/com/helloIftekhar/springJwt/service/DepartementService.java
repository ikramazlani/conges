package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.dto.DepartementDTO;
import com.helloIftekhar.springJwt.model.Departement;
import com.helloIftekhar.springJwt.repository.DepartementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartementService {

    private final DepartementRepository departementRepository;

    public DepartementService(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    public DepartementDTO createDepartement(DepartementDTO departementDTO) {
        // Vérifier si le département existe déjà
        if (departementRepository.existsByNomDepartement(departementDTO.getNomDepartement())) {
            throw new RuntimeException("Un département avec ce nom existe déjà");
        }

        Departement departement = new Departement();
        departement.setNomDepartement(departementDTO.getNomDepartement());
        departement.setDescription(departementDTO.getDescription());
        departement.setDate(departementDTO.getDate() != null ? departementDTO.getDate() : new Date());

        departement = departementRepository.save(departement);
        return convertToDTO(departement);
    }

    public List<DepartementDTO> getAllDepartements() {
        return departementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DepartementDTO getDepartementById(Long id) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        return convertToDTO(departement);
    }




    @Transactional
    public DepartementDTO updateDepartement(Long id, DepartementDTO departementDTO) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'ID : " + id));

        // Vérifier si le nouveau nom existe déjà (sauf pour le département actuel)
        if (departementDTO.getNomDepartement() != null &&
                !departementDTO.getNomDepartement().equals(departement.getNomDepartement())){
            if (departementRepository.existsByNomDepartement(departementDTO.getNomDepartement())) {
                throw new RuntimeException("Un département avec ce nom existe déjà");
            }
            departement.setNomDepartement(departementDTO.getNomDepartement());
        }

        if (departementDTO.getDescription() != null) {
            departement.setDescription(departementDTO.getDescription());
        }

        if (departementDTO.getDate() != null) {
            departement.setDate(departementDTO.getDate());
        }

        departement = departementRepository.save(departement);
        return convertToDTO(departement);
    }


    @Transactional
    public void deleteDepartement(Long id) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'ID : " + id));

        // Vérifier si le département a des utilisateurs avant suppression
        if (!departement.getUsers().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : le département a des utilisateurs associés");
        }

        departementRepository.delete(departement);
    }


    private DepartementDTO convertToDTO(Departement departement) {
        DepartementDTO dto = new DepartementDTO();
        dto.setId(departement.getId());
        dto.setNomDepartement(departement.getNomDepartement());
        dto.setDescription(departement.getDescription());
        dto.setDate(departement.getDate());
        return dto;
    }



    public List<DepartementDTO> getAllDepartementsWithDetails() {
        return departementRepository.findAllByOrderByNomDepartementAsc()
                .stream()
                .map(departement -> {
                    DepartementDTO dto = new DepartementDTO();
                    dto.setId(departement.getId());
                    dto.setNomDepartement(departement.getNomDepartement());
                    dto.setDescription(departement.getDescription());
                    dto.setDate(departement.getDate());
                    dto.setNombreUtilisateurs(departement.getUsers() != null ? departement.getUsers().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private DepartementDTO convertToDetailedDTO(Departement departement) {
        DepartementDTO dto = new DepartementDTO();
        dto.setId(departement.getId());
        dto.setNomDepartement(departement.getNomDepartement());
        dto.setDescription(departement.getDescription());
        dto.setDate(departement.getDate());
        dto.setNombreUtilisateurs(departement.getUsers() != null ? departement.getUsers().size() : 0);

        return dto;
    }



    public int getNombreEmployesByDepartementId(Long id) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'ID : " + id));
        return departement.getUsers() != null ? departement.getUsers().size() : 0;
    }




}