package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.entities.UserType;
import upc.edu.pe.tutorconnect.mappers.ISubjectMapper;
import upc.edu.pe.tutorconnect.mappers.ITutorMapper;
import upc.edu.pe.tutorconnect.mappers.IUserMapper;
import upc.edu.pe.tutorconnect.mappers.IUserTypeMapper;
import upc.edu.pe.tutorconnect.repositories.ISubjectRepository;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;
import upc.edu.pe.tutorconnect.repositories.IUserTypeRepository;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.*;

@Service
public class UserService implements UserDetailsService, IUserService{

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO saveUser(UserDTO userDTO) throws ServiceException {
        User userEntity = this.userMapper.toEntity(userDTO);
        UserType userTypeEntity = this.useTypeRepositoryRepository.findById(userEntity.getUserType().getId()).orElse(null);
        userEntity.setUserType(userTypeEntity);
        if(userDTO.getUserTypeDTO().getId() == 1) {
            List<Subject> subjects = new ArrayList<>();
            for (SubjectDTO dto: userDTO.getTutorDTO().getSubjectsDTO()) {
                subjects.add(this.subjectRepository.findById(dto.getId()).orElse(null));
            }
            Tutor tutor = userEntity.getTutor();
            tutor.setUser(userEntity);
            tutor.setSubjects(subjects);

            userEntity.setTutor(tutor);
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        User result = this.userRepository.save(userEntity);
        result.setPassword(null);

        UserDTO resultDTO = this.userMapper.toDTO(result);
        return resultDTO;
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
        lst.stream().toList().forEach(user ->  user.setPassword(null));
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public List<UserDTO> findAllStudents() throws ServiceException {
        List<User> lst = this.userRepository.findAllByUserTypeIdEquals(2L);
        lst.stream().toList().forEach(user ->  user.setPassword(null));
        return this.userMapper.getUsersDTO(lst);
    }

    @Override
    public List<UserDTO> findAllTutor() throws ServiceException {
        List<User> lst = this.userRepository.findAllByUserTypeIdEquals(1L);
        lst.stream().toList().forEach(user ->  user.setPassword(null));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException("Usuario o Password inválido");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private List<SimpleGrantedAuthority> getAuthority(User user) {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getUserType().getText()));
    }
}
