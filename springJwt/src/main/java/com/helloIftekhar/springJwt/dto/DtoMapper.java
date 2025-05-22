package com.helloIftekhar.springJwt.dto;


import com.helloIftekhar.springJwt.dto.DepartementDTO;
import com.helloIftekhar.springJwt.dto.UserDTO;
import com.helloIftekhar.springJwt.model.Departement;
import com.helloIftekhar.springJwt.model.User;

public class DtoMapper {

    public static UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setMatricule(user.getMatricule());
        userDTO.setGrade(user.getGrade());
        userDTO.setEchelle(user.getEchelle());
        userDTO.setAdresse(user.getAdresse());
        userDTO.setDateNaissance(user.getDateNaissance());
        userDTO.setRole(user.getRole());
        userDTO.setNomArab(user.getNomArab());
        userDTO.setPrenomArab(user.getPrenomArab());
        userDTO.setGradeArab(user.getGradeArab());
        userDTO.setEchelleArab(user.getEchelleArab());

        // Ajouter la gestion du service par chef service
        if (user.getService() != null) {
            userDTO.setServiceId(user.getService().getId());
            userDTO.setServiceName(user.getService().getNomService());
        }
        if (user.getDepartement() != null) {
            userDTO.setDepartement(convertToDepartementDTO(user.getDepartement()));
        }

        return userDTO;
    }

    public static DepartementDTO convertToDepartementDTO(Departement departement) {
        DepartementDTO dto = new DepartementDTO();
        dto.setId(departement.getId());
        dto.setNomDepartement(departement.getNomDepartement());
        dto.setDescription(departement.getDescription());
        dto.setDate(departement.getDate());
        return dto;
    }

}