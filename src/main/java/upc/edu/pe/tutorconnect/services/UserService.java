package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.entities.UserType;
import upc.edu.pe.tutorconnect.mappers.ISubjectMapper;
import upc.edu.pe.tutorconnect.mappers.IUserMapper;
import upc.edu.pe.tutorconnect.repositories.ISubjectRepository;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;
import upc.edu.pe.tutorconnect.repositories.IUserTypeRepository;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.ArrayList;
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
    private ISubjectRepository subjectRepository;
    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private ISubjectMapper subjectMapper;

    @Override
    public UserDTO saveUser(UserDTO userDTO) throws ServiceException {
        User userEntity = this.userMapper.toEntity(userDTO);
        UserType userTypeEntity = this.useTypeRepositoryRepository.findById(userEntity.getUserType().getId()).orElse(null);
        userEntity.setUserType(userTypeEntity);
        if(userDTO.getUserTypeDTO().getId() == 1) {
            List<Subject> subjects = getListSubjects(userDTO.getTutorDTO().getSubjectsDTO());
            Tutor tutor = userEntity.getTutor();
            tutor.setUser(userEntity);
            tutor.setSubjects(subjects);
            userEntity.setTutor(tutor);
        }
        User result = this.userRepository.save(userEntity);
        result.setPassword(null);
        return this.userMapper.toDTO(result);
    }

    public List<Subject> getListSubjects(List<SubjectDTO> subjectDTOS) throws ServiceException {
        List<Subject> subjects = new ArrayList<>();
        for (SubjectDTO dto: subjectDTOS) {
            subjects.add(this.subjectRepository.findById(dto.getId()).orElse(null));
        }
        return subjects;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) throws ServiceException {
        User userEntity = this.userRepository.findById(id).orElse(null);
        if(userEntity == null)  return null;
        userDTO.setId(id);
        User updatedUser = this.userMapper.toEntity(userDTO);
        UserType userTypeEntity = this.useTypeRepositoryRepository.findById(userDTO.getUserTypeDTO().getId()).orElse(null);
        updatedUser.setUserType(userTypeEntity);
        if(userDTO.getUserTypeDTO().getId() == 1) {
            Tutor tutor = this.tutorRepository.findByUserId(userDTO.getId());
            tutor.setProductImageUrl(userDTO.getTutorDTO().getProductImageUrl());
            tutor.setProfileImageUrl(userDTO.getTutorDTO().getProfileImageUrl());
            tutor.setDescription(updatedUser.getTutor().getDescription());
            tutor.setPricePerHour(updatedUser.getTutor().getPricePerHour());
            tutor.setUser(updatedUser);
            updatedUser.setTutor(tutor);
        }
        User result = this.userRepository.save(updatedUser);
        result.setPassword(null);
        return this.userMapper.toDTO(result);
    }

    @Override
    public List<UserDTO> findAllUser() {
        List<User> lst = this.userRepository.findAll();
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public List<UserDTO> findAllStudents() throws ServiceException {
        List<User> lst = this.userRepository.findAllByUserTypeIdEquals(2L);
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public List<UserDTO> findAllTutor() throws ServiceException {
        List<User> lst = this.userRepository.findAllByUserTypeIdEquals(1L);
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public UserDTO findByUsername(String username) throws ServiceException {
        return this.userMapper.toDTO(this.userRepository.findByUsername(username).orElse(null));
    }

    @Override
    public UserDTO findByEmail(String email) throws ServiceException {
        return this.userMapper.toDTO(this.userRepository.findByEmail(email).orElse(null));
    }

    @Override
    public UserDTO findTutorById(Long id) throws ServiceException {
        User result = this.userRepository.findByIdAndUserTypeId(id, 1L).orElse(null);
        return this.userMapper.toDTO(result);
    }

    @Override
    public UserDTO findStudentById(Long id) throws ServiceException {
        User result = this.userRepository.findByIdAndUserTypeId(id, 2L).orElse(null);
        return this.userMapper.toDTO(result);
    }
}
