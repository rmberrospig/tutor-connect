package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.entities.UserType;
import upc.edu.pe.tutorconnect.mappers.ITutorMapper;
import upc.edu.pe.tutorconnect.mappers.IUserMapper;
import upc.edu.pe.tutorconnect.mappers.IUserTypeMapper;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;
import upc.edu.pe.tutorconnect.repositories.IUserTypeRepository;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.List;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserTypeRepository useTypeRepositoryRepository;
    @Autowired
    private ITutorRepository tutorRepository;
    @Autowired
    private IUserMapper userMapper;

    @Override
    public UserDTO saveUser(UserDTO userDTO) throws ServiceException {
        User userEntity = this.userMapper.toEntity(userDTO);
        UserType userTypeEntity = this.useTypeRepositoryRepository.findById(userEntity.getUserType().getId()).orElse(null);
        userEntity.setUserType(userTypeEntity);
        if(userDTO.getUserTypeDTO().getId() == 2) {
            Tutor tutor = userEntity.getTutor();
            tutor.setUser(userEntity);
            userEntity.setTutor(tutor);
        }
        User result = this.userRepository.save(userEntity);
        return this.userMapper.toDTO(result);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) throws ServiceException {
        User userEntity = this.userRepository.findById(id).orElse(null);
        if(userEntity == null)  return null;
        userDTO.setId(id);
        User updatedUser = this.userMapper.toEntity(userDTO);
        UserType userTypeEntity = this.useTypeRepositoryRepository.findById(userDTO.getUserTypeDTO().getId()).orElse(null);
        updatedUser.setUserType(userTypeEntity);
        if(userDTO.getUserTypeDTO().getId() == 2) {
            Tutor tutor = this.tutorRepository.findByUserId(userDTO.getId());
            tutor.setDescripcion(updatedUser.getTutor().getDescripcion());
            tutor.setPricePerHour(updatedUser.getTutor().getPricePerHour());
            tutor.setUser(updatedUser);
            updatedUser.setTutor(tutor);
        }
        User result = this.userRepository.save(updatedUser);
        return this.userMapper.toDTO(result);
    }

    @Override
    public List<UserDTO> findAllUser() {
        List<User> lst = this.userRepository.findAll();
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public List<UserDTO> findAllStudents() throws ServiceException {
        List<User> lst = this.userRepository.findAllByUserTypeIdEquals(1L);
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public List<UserDTO> findAllTutor() throws ServiceException {
        List<User> lst = this.userRepository.findAllByUserTypeIdEquals(2L);
        return this.userMapper.getUsersDTO(lst);
    }
}
