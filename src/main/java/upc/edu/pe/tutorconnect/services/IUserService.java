package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.List;

public interface IUserService {

    UserDTO saveUser(UserDTO userDTO) throws ServiceException;
    UserDTO updateUser(Long id, UserDTO userDTO) throws ServiceException;
    List<UserDTO> findAllUser() throws ServiceException;
    List<UserDTO> findAllStudents() throws ServiceException;
    List<UserDTO> findAllTutor() throws ServiceException;
}
