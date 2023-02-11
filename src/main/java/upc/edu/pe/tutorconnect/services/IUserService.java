package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.UserDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAllUser();
}
